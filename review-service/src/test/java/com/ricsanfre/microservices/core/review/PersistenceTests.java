package com.ricsanfre.microservices.core.review;

import com.ricsanfre.microservices.core.review.db.entity.Review;
import com.ricsanfre.microservices.core.review.db.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DataJpaTest
// https://www.baeldung.com/spring-transactional-propagation-isolation
// If a current transaction exists, first Spring suspends it, and then the business logic is executed without a transaction:
@Transactional(propagation = Propagation.NOT_SUPPORTED)
// Disable embedded database and use Testcontainer extending from AbstractTestcontainersUnitTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PersistenceTests extends PostgreBaseTest {

    @Autowired
    private ReviewRepository reviewRepository;

    private Review savedReview;

    @BeforeEach
    void setUp() {

        // Delete previous reviews
        reviewRepository.deleteAll();

        // Save new review
        Review review = new Review(1,2,"author1", "subject1", "content1");
        savedReview = reviewRepository.save(review);

        assertEqualsReview(review, savedReview);
    }

    @Test
    void create() {

        Review newEntity = new Review(1, 3, "a", "s", "c");
        reviewRepository.save(newEntity);

        Optional<Review> actual= reviewRepository.findById(newEntity.getId());
        assertThat(actual).isPresent();
        assertEqualsReview(newEntity, actual.get());
        assertThat(reviewRepository.count()).isEqualTo(2);
    }

    @Test
    void update() {

        savedReview.setAuthor("newAuthor");
        reviewRepository.save(savedReview);
        Optional<Review> actual= reviewRepository.findById(savedReview.getId());
        assertThat(actual).isPresent().hasValueSatisfying( r -> {
            assertThat(r.getVersion()).isEqualTo(1);
            assertThat(r.getAuthor()).isEqualTo("newAuthor");
        });

    }

    @Test
    void delete() {

        reviewRepository.delete(savedReview);

        Optional<Review> actual= reviewRepository.findById(savedReview.getId());
        assertThat(actual).isNotPresent();

    }

    @Test
    void getByProductId() {
        List<Review> entityList = reviewRepository.findByProductId(savedReview.getProductId());

        assertThat(entityList).hasSize(1);
        assertEqualsReview(savedReview, entityList.get(0));
    }

    @Test
    void duplicateError() {

        Review entity = new Review(1, 2, "a", "s", "c");

        assertThatThrownBy(()->reviewRepository.save(entity))
                .isInstanceOf(DataIntegrityViolationException.class);

    }

    @Test
    void optimisticLockError() {

        // Store the saved entity in two separate entity objects
        Review entity1 = reviewRepository.findById(savedReview.getId()).orElseThrow();
        Review entity2 = reviewRepository.findById(savedReview.getId()).orElseThrow();

        // Update the entity using the first entity object
        entity1.setAuthor("a1");
        reviewRepository.save(entity1);

        // Update the entity using the second entity object.
        // This should fail since the second entity now holds an old version number, i.e. an Optimistic Lock Error
        entity2.setAuthor("a2");

        assertThatThrownBy(()->reviewRepository.save(entity2))
                .isInstanceOf(OptimisticLockingFailureException.class);


        // Get the updated entity from the database and verify its new state
        Review updatedEntity = reviewRepository.findById(savedReview.getId()).orElseThrow();

        assertThat(updatedEntity.getVersion()).isEqualTo(1);
        assertThat(updatedEntity.getAuthor()).isEqualTo("a1");
    }
    private void assertEqualsReview(Review expected, Review actual) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getVersion()).isEqualTo(expected.getVersion());
        assertThat(actual.getProductId()).isEqualTo(expected.getProductId());
        assertThat(actual.getReviewId()).isEqualTo(expected.getReviewId());
        assertThat(actual.getAuthor()).isEqualTo(expected.getAuthor());
        assertThat(actual.getSubject()).isEqualTo(expected.getSubject());
        assertThat(actual.getContent()).isEqualTo(expected.getContent());
    }

}
