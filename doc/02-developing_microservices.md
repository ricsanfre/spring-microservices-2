# Developing Microservices

## Servlet Stack vs Reactive Stack

Using Spring boot there is two ways to develop microservices

- Using [Servlet Stack]()
  
  Servlet API is designed to handle blocking I/O, where a thread is blocked until a response is received from a database or another service.
  **Spring Web** is well-suited for traditional web applications that require synchronous communication.

- Using [Reactive Stack](https://docs.spring.io/spring-framework/reference/web-reactive.html)

  A reactive web framework that is built on top of Reactive Streams. It is designed to handle non-blocking I/O, where a thread is not blocked while waiting for a response from a database or another service. Instead, the application can continue to process other requests while waiting for the response.
  **Spring Webflux** is well-suited for applications that require high concurrency, such as streaming applications or real-time data processing.

![mvc_vs_webflux](MVC_vs_Webflux.webp)


In both cases, the same spring annotations (@RestController, @GetMapping, etc) can be used.


### Dependencies to be used

- Servlet Stack

  Spring MVC dependency has to be added to the pom file of the project

  ```xml
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
  </dependency>
  ```

- Reactive Stack

  Spring Webflux dependency has to be added to the pom file of the project

  ```xml
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
  </dependency>
  ```
  
## Implementing REST API

- Define DTOs
  Data Transfer Object (DTO) as it is used to transfer data between the API implementation and the caller of the API
  It is a POJO class.

  ```java
  package com.ricsanfre.microservices.api.core.product;
  
  public class Product {
  private final int productId;
  private final String name;
  private final int weight;
  private final String serviceAddress;
  
      public Product(int productId, String name, int weight, String serviceAddress) {
          this.productId = productId;
          this.name = name;
          this.weight = weight;
          this.serviceAddress = serviceAddress;
      }
  
      public int getProductId() {
          return productId;
      }
  
      public String getName() {
          return name;
      }
  
      public int getWeight() {
          return weight;
      }
  
      public String getServiceAddress() {
          return serviceAddress;
      }
  }  
  ```
- Define API interfaces
  Java interfaces defining the API. This interface need to be implemented by the RestController class.

  Methods need to be annotated with corresponding [mapping request annotations](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-requestmapping.html)
  - @GetMapping
  - @PostMapping
  - @PutMapping
  - @DeleteMapping
  - @PatchMapping

  ```java
  package com.ricsanfre.microservices.api.core.product;

  import org.springframework.web.bind.annotation.GetMapping;
  import org.springframework.web.bind.annotation.PathVariable;

  public interface ProductService {

    /**
     * Sample usage: "curl $HOST:$PORT/product/1".
     *
     * @param productId Id of the product
     * @return the product, if found, else null
     */

    @GetMapping(
            value = "/product/{productId}",
            produces = "application/json"
    )
    Product getProduct(@PathVariable("productId") int productId);
  }
  ```
  
- Define RestController implementation

  Java class with @RestControler annotation implementing the API interface

  ```java
  package com.ricsanfre.microservices.core.product.services;
  
  import com.ricsanfre.microservices.api.core.product.Product;
  import com.ricsanfre.microservices.api.core.product.ProductService;
  import org.springframework.web.bind.annotation.RestController;
  
  @RestController
  public class ProductServiceImpl implements ProductService {
 
      @Override
      public Product getProduct(int productId) {
          return new Product(productId, "name-" + productId, 123,
                  "serviceAddress");
      }
  }

  ```

## Adding Logging

Spring Boot, as a framework, incorporates a flexible and efficient [logging system](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.logging) based on SLF4J, Simple Logging Facade for Java.
SLF4J isn’t a logging implementation in itself but serves as a facade or an abstraction layer for various logging libraries like Logback, Log4J,

Spring boot uses Logbak as default logging when adding `spring-boot-starter-web` dependency (Springboot MVC)

Default configuration outputs logs to console. This default configuration is useful in Kuberentes environments.
See [Springboot Logging Reference documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.logging)


- Adding logs within Java Classes
  ```java
  
  
  import org.slf4j.Logger;
  import org.slf4j.LoggerFactory;
  import org.springframework.web.bind.annotation.GetMapping;
  import org.springframework.web.bind.annotation.PathVariable;
  import org.springframework.web.bind.annotation.RestController;
  
  @RestController
  public class HelloController {
  
      // Get the SLF4J logger interface, default Logback, a SLF4J implementation
      private static final Logger logger = LoggerFactory.getLogger(HelloController.class);
  
      @GetMapping("/")
      public String hello() {
  
          // log to console (default)
  
          logger.debug("Debug level - Hello Logback");
  
          logger.info("Info level - Hello Logback");
          
          logger.error("Error level - Hello Logback");
  
          return "Hello SLF4J";
      }
  
  }
  ```
  
- Configure log levels

  Different logs levels can be configured in `application.yaml` file

  logging.level.<logger-name>=<level> 
  where <level> is one of TRACE, DEBUG, INFO, WARN, ERROR, FATAL, or OFF.
  and <logger-name> is a package or class name
  The root logger can be configured by using logging.level.root.

  ```yaml
  # Logging configuration
  
  # Default logging level is INFO
  # logging level for classes within package com.ricsanfre.microservices is DEBUG
  logging:
    level:
      root: INFO
      com.ricsanfre.microservices: DEBUG
  ```
  
## Implementing REST Client

Use [Spring Cloud OpenFeign](https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/html/) for implementing declarative REST Clients

- Add OpenFeign dependencies to pom.xml

  ```xml
  <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-openfeign</artifactId>
  </dependency>  
  ```
  
-  Define OpenFeign client Interface
   
   The interface can extend the one used for defining the service.
   See [Open Feign inheritance support](https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/html/#spring-cloud-feign-inheritance)

   ```java
   package com.ricsanfre.microservices.api.core.product;

   import org.springframework.cloud.openfeign.FeignClient;
    
   @FeignClient(
       name = "product",
       url = "${app.product.url}"
   )
   public interface ProductClient extends ProductService{
   }
   ```
  - Add @EnableFeignClients annotation to main application

    ```java
    package com.ricsanfre.microservices.composite.product;
    
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    import org.springframework.cloud.openfeign.EnableFeignClients;
    
    @SpringBootApplication
    @EnableFeignClients(
        basePackages = "com.ricsanfre.microservices.api"
    )
    public class ProductCompositeServiceApplication {
    public static void main(String[] args) {
    
            SpringApplication.run(ProductCompositeServiceApplication.class, args);
        }
    } 
    ```
    
  - Add clients urls to `application.yaml` 
  
    ```yaml
    # Microservices URL
    app:
      product:
        url: http://localhost:8081
    ```