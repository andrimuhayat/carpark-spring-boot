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

## Scheduler
- The **dataset(Car information)** that provides from https://data.gov.sg/dataset/hdb-carpark-information will fetching automatically at the midnight then the data will convert from SVY21ToLatLng and do insert/update to the database. but at the first will process when system is started
- The **Car availability** will fetch automatically from API in every 2 minutes and insert/update to database. but at the first will process when system is started

## Redis Cache
- The response data from API(/carparks/nearest?latitude=1.2855825549923094&longitude=103.80321526415918) already store in redis cache, when calling the get API then data will serve from the cache of redis
- Data expire in redis in will 2 Minutes and will call to the db and store it again to the redis

## How to run this project
open terminal cd to this project's root folder and run script below

    docker-compose up