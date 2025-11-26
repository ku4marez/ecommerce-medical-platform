# ---------- Stage 1: Build ----------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# GitHub Packages credentials
ARG GPR_USER
ARG GPR_TOKEN

ARG CACHE_BUSTER=1

# Gradle wrapper & config
COPY gradlew .
COPY gradle gradle
COPY settings.gradle .
COPY build.gradle .
COPY gradle.properties .

COPY platform ./platform
COPY starters ./starters
COPY ./services/catalog-service ./services/catalog-service

RUN GPR_USER="$GPR_USER" GPR_TOKEN="$GPR_TOKEN" ./gradlew :services:catalog-service:bootJar --no-daemon --stacktrace

# ---------- Stage 2: Run ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/services/catalog-service/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
