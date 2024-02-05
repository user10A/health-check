# First stage: Build stage
FROM openjdk:17 as build
WORKDIR /app
COPY . ./

# Copy the Maven wrapper configuration file
COPY .mvn/wrapper/maven-wrapper.properties .mvn/wrapper/

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Second stage: Runtime stage
FROM openjdk:17.0.2-jdk-slim
WORKDIR /app
COPY --from=build /app/target/health-check-java11-0.0.1-SNAPSHOT.jar .
EXPOSE 2024
CMD ["java", "-jar", "health-check-java11-0.0.1-SNAPSHOT.jar"]
