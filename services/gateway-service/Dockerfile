RUN ./gradlew :services:gateway-service:bootJar --no-daemon
COPY --from=build /app/services/gateway-service/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
