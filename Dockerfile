FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY src /app/src
COPY pom.xml /app
RUN mvn -f /app/pom.xml clean package

FROM openjdk:21-jdk-slim
COPY --from=build app/target/social-platform-1.0.0.jar app/app.jar
ENTRYPOINT ["java", "-jar", "app/app.jar"]