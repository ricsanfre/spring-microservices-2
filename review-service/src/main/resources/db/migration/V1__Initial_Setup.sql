CREATE TABLE review (
                               id BIGSERIAL PRIMARY KEY,
                               product_id integer NOT NULL,
                               review_id integer NOT NULL,
                               version integer NOT NULL,
                               author character varying(255),
                               content character varying(255),
                               subject character varying(255)
);

--
-- Adding unique constraint
--

ALTER TABLE ONLY review
    ADD CONSTRAINT reviews_unique_idx UNIQUE (product_id, review_id);