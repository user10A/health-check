# Stage 1: Build Stage
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY health-check-java11 .
RUN mvn clean package -DskipTests

# Stage 2: Production Stage
FROM openjdk:17.0.2-jdk-slim
WORKDIR /app
COPY --from=build /app/target/health-check-java11-0.0.1-SNAPSHOT.jar .
EXPOSE 2024
CMD ["java", "-jar", "health-check-java11-0.0.1-SNAPSHOT.jar"]
