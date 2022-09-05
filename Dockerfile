FROM maven:3.6.3-jdk-11-slim AS build
ENV TZ="Asia/Jakarta"

WORKDIR /app
#copy pom
COPY pom.xml .
#copy source
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:11-jre-slim
#ARG JAR_FILE=target/*.jar
WORKDIR /app

COPY --from=build app/target/*.jar CarPark-0.0.1-SNAPSHOT.jar
# Run the web service on container startup.
EXPOSE 9090
ENTRYPOINT ["java","-jar","CarPark-0.0.1-SNAPSHOT.jar","-Dspring.config.location=file:application.properties"]