# Car Park
### version: 1.0.0 (September 5, 2022)

## Project Description

This service made to containerize the car park service make a clear separation
of concern within the architecture with [Uncle Bob Clean Architecture](https://www.baeldung.com/spring-boot-clean-architecture) interpretation.

## Build with

- [Java 11 - 18.0.2](https://adoptopenjdk.net/) Java 11 
- [Maven 4.0.0](https://maven.apache.org/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/)

## Package Description:
* **config** use this package to store any base configuration class e.g. WebSecurity, Http Configs, and so on.
* **controller** use this package to store any rest controller class for making request to this project.
* **dto** use this package to store any DTO used to transport object between layer read _Uncle Bob Clean Architecture_ for more information on this.
* **model** use this package to store any DAO.
* **repository** use this package to create new repository interface.
* **service** use this package to create new service to compute every business process needed from front-end or other backend who need this service.
* **util** use this package to create new helper class that can be used in any class.

## Standardization used to build this project

- [Google Java Format](https://github.com/google/google-java-format) used for code style formatting
- [Sonarlint](https://www.sonarlint.org/) used to check code cleanness and help prevent NPE
- [Resource Naming Guide](https://restfulapi.net/resource-naming/) used to make a clear resource naming

## Tools that might help to run or build this project

- [SDKMAN](https://sdkman.io/) used to help you get needed JDK or SDK with ease

## How to run this project
open terminal cd to this project's root folder and run script below

    docker-compose up

## 
