# Multi-stage Dockerfile for HRMS Spring Boot app
# Build stage: uses the Maven wrapper present in the repo to produce the fat jar
FROM maven:3.8.8-openjdk-11 AS build
WORKDIR /workspace

# copy mvnw and .mvn first to leverage layer caching for dependency download
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN chmod +x mvnw
# download dependencies (offline) to speed up subsequent builds
RUN ./mvnw -B -DskipTests dependency:go-offline

# copy source and build
COPY src ./src
RUN ./mvnw -B -DskipTests package

# Runtime image
FROM openjdk:11-jre-slim
ARG JAR_FILE=target/*.jar
COPY --from=build /workspace/target/*.jar /app.jar

# optional environment variable for JVM options
ENV JAVA_OPTS="-Xms256m -Xmx512m"

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app.jar"]
