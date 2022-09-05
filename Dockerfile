#FROM openjdk:18-alpine
#ENV TZ="Asia/Jakarta"
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} car-park.jar
#EXPOSE 8090
#ENTRYPOINT ["java","-jar","car-park.jar","-Dspring.config.location=file:application.properties"]


FROM maven:3.5-jdk-8-alpine as builder
WORKDIR /app
COPY . .
RUN mvn package -DskipTests

FROM openjdk:8-jre-alpine
#ARG JAR_FILE=target/*.jar
COPY --from=builder target/*.jar CarPark-0.0.1-SNAPSHOT.jar
# Run the web service on container startup.
EXPOSE 9090
ENTRYPOINT ["java","-jar","CarPark-0.0.1-SNAPSHOT.jar","-Dspring.config.location=file:application.properties"]