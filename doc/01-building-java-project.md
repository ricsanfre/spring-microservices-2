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
#### Manage global dependencies

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
  ```
  ```xml
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
  
  Example lombok library

  ```xml
  <dependencies>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
  </dependencies>
  ```

#### Manage global build 

Manage build global configuration within build.pluginManagement section.

build.pluginManagement section only declares a default configuration for a plugin. To be used need to be specified in build.plugins section
By adding the plugin in the pluginManagement section, it becomes available to this POM, and all inheriting child POMs.
This means that any child POMs will inherit the plugin executions simply by referencing the plugin in their plugin section. All we need to do is add the relevant groupId and artifactId, without having to duplicate the configuration or manage the version.
Default configuration can be defined in parent POM, in submodules's POM only it is needed to reference it.
See details about [maven-plugin-management](https://www.baeldung.com/maven-plugin-management)

- Remove plugins from build.pluginManagement section that has been created by create maven project command

- Add maven-compiler-plugin to build.pluginManagement section

  Specify source and target java versions and plugin version to be used through properties.
  
  ```xml
  <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <maven-compiler-plugin.version>3.11.0</maven-compiler-plugin.version>
  </properties>
  ```
  ```xml
  <build>
    <pluginManagement>
        <plugins>
            <!-- Maven compiler -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>${maven-compiler-plugin.version}</version>
            </plugin>
        </plugins>
    </pluginManagement> 
  </build> 
  ```
  
- Add maven-sunfire-plugin to execute unit tests

  ```xml
  <properties>
        <maven-sunfire-plugin.version>2.22.1</maven-sunfire-plugin.version>
  </properties>
  ```
  ```xml
  <build>
    <pluginManagement>
        <plugins>
            <!-- Maven sunfire plugin (Unit Testing)  -->
            <plugin>
                 <artifactId>maven-surefire-plugin</artifactId>
                 <version>${maven-sunfire-plugin.version}</version>
            </plugin>
        </plugins>
    </pluginManagement> 
  </build>   
  ```

- Add Spring boot maven plugin.

  The plugin can create executable archives (jar files and war files) that contain all of an application’s dependencies and can then be run with java -jar
  plugin's `repackage` goal generates this executable jar file with the command:

  ```shell
  mvn spring-boot:repackage
  ```

  Configure plugin's `repackage` goal to be executed as part of mvn's `package` build lifecycle phase.
  https://docs.spring.io/spring-boot/docs/current/maven-plugin/reference/htmlsingle/#packaging


  ```xml
  <build>
    <pluginManagement>
      <plugins>
        <plugin>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-maven-plugin</artifactId>
          <executions>
                <execution>
                    <goals>
                        <goal>repackage</goal>
                    </goals>
                </execution>
          </executions>  
        </plugin>
      </plugins>
    </pluginManagement>
  </build> 
  ```

- Add Jib maven plugin.

  [Jib](https://github.com/GoogleContainerTools/jib) can be used to generate automatically docker images without generating any Dockerfile
  Jib builds optimized Docker and OCI images for Java applications without a Docker daemon.

  
  It can be used to generate docker images locally in development environment where there is available a Docker daemon
  ```shell
  mvn jib:dockerBuild
  ```
  It can also be used to directly push the docker image to Docker Hub. In this case a Docker daemon is not needed
  
  ```shell
  mvn jib:build
  ```

  NOTE: Jib reads Docker Hub credentials from `$HOME/.docker/config.json` file which is created when executing `docker login` command
  Manual login into docker hub is required before using Jib to push images to DockerHub.

  Jib plugin configuration:
  
  - Configure plugin to generate locally docker images using Docker daemon.
  - Base image `eclipse-temurin:17-jre` supporting multi-architecture (amd64/arm64)
  - docker image name and tag specified through properties in child pom, configuration
  - jib build or dockerBuild specified by property application.jib.defaultGoal
  - jib:build or jib:dockerBuild goals added to `package` maven lifecycle phase

  ```xml
      <properties>
        <jib.version>3.4.1</jib.version>

        <!-- Jib properties -->
        <application.jib.defaultGoal>dockerBuild</application.jib.defaultGoal> <!-- dockerBuild for local only; build to push also -->
        <application.docker.skip>false</application.docker.skip>
        <application.docker.image>${application.docker.image.name}:${application.docker.image.tag}
        </application.docker.image>
        <!-- application.docker.image.name MUST be set by child modules that use jib-maven-plugin. -->
        <application.docker.image.name>INVALID</application.docker.image.name>
        <application.docker.image.tag>latest</application.docker.image.tag>
    </properties>
  ```
  ```xml
  <build>
      <pluginManagement>
          <plugins>
              <plugin>
                  <groupId>com.google.cloud.tools</groupId>
                  <artifactId>jib-maven-plugin</artifactId>
                  <version>${jib.version}</version>
                  <configuration>
                      <from>
                          <!-- Make sure to use the hash of the manifest list (multi-arch) vs the hash of a particular manifest (single-arch) when updating
                               See https://github.com/docker/roadmap/issues/262#issuecomment-1161515179 for an example on how to do this -->
                          <image>
                              eclipse-temurin:17-jre@sha256:c61c6a48364d05ea89504489aecddd8af8c32dba6df2257da4131fe244284f50
                          </image>
                      </from>
                      <!--suppress MavenModelInspection -->
                      <skip>${application.docker.skip}</skip>
                      <to>
                          <image>${application.docker.image}</image>
                      </to>
                  </configuration>
                  <executions>
                      <execution>
                          <id>build-docker-image</id>
                          <goals>
                              <goal>${application.jib.defaultGoal}</goal>
                          </goals>
                          <!-- Must be in package phase before docker runs-->
                          <phase>package</phase>
                      </execution>
                  </executions>
              </plugin>
          </plugins>
      </pluginManagement>
  </build> 
  ```

  Add profiles (`arch-amd64` and `arch-arm64`) automatically actived that detect the architecture where building is happening
   - Configure jib to generate a docker image using the proper architecture
   - Generate images with current time stamp
  Configure a maven profile `cicd` to push the docker images directly to Docker Hub
   - Docker images pushed to DockerHub supports multi-architecture (amd64/arm64)
  ```xml
  <profiles>
    <profile>
      <id>cicd</id>
      <activation>
        <activeByDefault>false</activeByDefault>
      </activation>
      <properties>
        <!-- Build AND push the built image -->
        <application.jib.defaultGoal>build</application.jib.defaultGoal>
        <jib.container.creationTime>USE_CURRENT_TIMESTAMP</jib.container.creationTime>
        <jib.from.platforms>linux/amd64,linux/arm64</jib.from.platforms>
      </properties>
    </profile>
  
    <profile>
      <id>arch-amd64</id>
      <activation>
        <os>
          <arch>x86_64</arch>
        </os>
      </activation>
      <properties>
        <jib.from.platforms>linux/amd64</jib.from.platforms>
        <jib.container.creationTime>USE_CURRENT_TIMESTAMP</jib.container.creationTime>
      </properties>
    </profile>
    <profile>
      <id>arch-arm64</id>
      <activation>
        <os>
          <arch>aarch64</arch>
        </os>
      </activation>
      <properties>
        <jib.from.platforms>linux/arm64</jib.from.platforms>
        <jib.container.creationTime>USE_CURRENT_TIMESTAMP</jib.container.creationTime>
      </properties>
    </profile>
  </profiles>
  ```

  To generate docker images locally (dev environment):

  ```shell
  mvn package
  ```

  To push docker images direcly to docker hub

  ```shell
  mvn package -Pcicd
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


