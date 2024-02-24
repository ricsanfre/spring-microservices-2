# Building Java Project

## Installing Java dev tools

- Install JDK 17

  ```shell
  sudo apt install openjdk-17-jdk
  ```

- Install Maven

  ```shell
  sudo apt install maven
  ```

- Install Intellij

  Ultimate

  ```shell
  sudo snap install intellij-idea-ultimate --classic
  ```

  Community Edition

  ```shell
  sudo snap install intellij-idea-community --classic
  ```


## Initialize Maven multi-module project


See [maven multi-module-gui](https://www.baeldung.com/maven-multi-module)

### Create Maven parent project

Create Maven parent project: `microservices`, containing Spring-boot plugins and dependencies to be inherited by other submodules
  
- Create, basic project scaffolding through command line
 
  ```shell
  mvn archetype:generate -DgroupId=com.ricsanfre.microservices -DartifactId=microservices -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4 -DinteractiveMode=false
  ```

- Edit parent project pom.xml file

  Add the following line
  
  ```xml
  <packaging>pom</packaging>
  ```

- Add Spring boot framework dependencies declaration
  
  Spring-boot applications usually us as parent `spring-boot-starter-parent` containing all spring boot curated list of dependencies

  ```xml
  <parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.2.0</version>
    <relativePath/> <!-- lookup parent from repository -->
  </parent>
  ```
  This artifact uses as parent pom `spring-boot-dependencies` artifact containing list of [Springboot curated dependencies](https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#using.build-systems.dependency-management)  

  In multi-project case, we are using our own parent pom. In this case Springboot curated dependencies can be imported as a Bill of Materials (BOM) in the <dependencyManagement/> section of the pom.xml.
  See more details in [spring boot multiple modules(https://www.baeldung.com/spring-boot-multiple-modules)

  NOTE: dependencies defined in <dependencyManagement> are only declarations. 
   dependencyManagement tag to avoid repeating the version and scope tags when we define our dependencies in the dependencies tag.
   In this way, the required dependency is declared in a central POM file
   In order to be used by the submodules it has to be specified module's pom <dependencies> section, but scope and version does not need to be defined.
   All modules use same version
   See more details in [maven dependency management vs dependency](https://www.baeldung.com/maven-dependencymanagement-vs-dependencies-tags)

  ```xml
  <dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-dependencies</artifactId>
        <version>3.2.0</version>
        <scope>import</scope>
        <type>pom</type>
      </dependency>
    </dependencies>
  </dependencyManagement>  
  ```
- Add Spring Cloud dependencies declaration

  ```xml
  <properties>
      <spring-cloud.version>2023.2.0</spring-cloud.version>
  </properties>
  <dependencyManagement>
      <dependencies>
          <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-dependencies</artifactId>
              <version>${spring-cloud.version}</version>
              <type>pom</type>
              <scope>import</scope>
          </dependency>
      </dependencies>
  </dependencyManagement>
  ```


- Add common dependencies for all sub-modules under `dependencies`

  ```xml
  <dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
  </dependencies>
  ```

- Add Spring boot maven plugin
  
  Empty <build><pluginManagement> section and add only Spring-boot plugin.
  
  NOTE:
    <pluginManagement> section only declares a plugin. To be used need to be specified in <build><plugins> section
    By adding the plugin in the pluginManagement section, it becomes available to this POM, and all inheriting child POMs.
    This means that any child POMs will inherit the plugin executions simply by referencing the plugin in their plugin section. All we need to do is add the relevant groupId and artifactId, without having to duplicate the configuration or manage the version.
    Default configuration can be defined in parent POM, in submodules's POM only it is needed to reference it.
  See details about [maven-plugin-management](https://www.baeldung.com/maven-plugin-management)

  ```xml
  <build>
    <pluginManagement>
      <plugins>
        <plugin>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
      </plugins>
    </pluginManagement>
  </build> 
  ```

#### Initial Parent POM


```xml
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>com.ricsanfre.microservices</groupId>
  <artifactId>microservices</artifactId>
  <version>1.0-SNAPSHOT</version>
  <packaging>pom</packaging>

  <name>microservices</name>
  <!-- FIXME change it to the project's website -->
  <url>http://www.example.com</url>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
  </properties>

  <!--
    Spring boot dependencies. Curated list
  -->
  <dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-dependencies</artifactId>
        <version>3.2.0</version>
        <scope>import</scope>
        <type>pom</type>
      </dependency>
    </dependencies>
  </dependencyManagement>

  <!--
    Common dependencies for all sub modules
  -->
  <dependencies>
    <!-- Webflux -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-webflux</artifactId>
    </dependency>
    <!-- Actuator -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <!-- Testing -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <pluginManagement><!-- lock down plugins versions to avoid using Maven defaults (may be moved to parent pom) -->
      <plugins>
        <!-- Spring-boot Maven plugin -->
        <plugin>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
      </plugins>
    </pluginManagement>
  </build>
</project>
```


# Create project submodules

- Create new directory 
  
  ```shell
  mkdir submodule-name
  ```
  
- Add new module to parent pom.xml

  ```xml
  <modules>
    <module>submodule-name</module>
  </modules>  
  ```
- Create submodule pom.xml

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <project xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.ricsanfre.microservices</groupId>
        <artifactId>microservices</artifactId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <artifactId>submodule-name</artifactId>
    <groupId>com.ricsanfre.microservices.submodule</groupId>
    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
  </project>  
  ```

>> NOTE: Alternatively, module can be created using IntelliJ


