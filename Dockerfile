FROM openjdk:17 as build
WORKDIR /app
COPY . ./
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

FROM openjdk:17.0.2-jdk-slim
WORKDIR /app
COPY --from=build /app/target/health-check-java11-0.0.1-SNAPSHOT.jar .
EXPOSE 2024
CMD ["java", "-jar", "health-check-java11-0.0.1-SNAPSHOT.jar"]
