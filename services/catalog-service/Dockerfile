# ---------- Stage 1: Build ----------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy Gradle wrapper and configuration
COPY gradlew .
COPY gradle gradle
COPY settings.gradle .
COPY gradle.properties .

# Copy shared modules
COPY platform ./platform
COPY starters ./starters
COPY services ./services

# Build only the target service
RUN ./gradlew :services:catalog-service:bootJar --no-daemon

# ---------- Stage 2: Run ----------
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/services/catalog-service/build/libs/*.jar app.jar

# Internal Spring Boot port (container-level)
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
