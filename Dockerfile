FROM gradle:9-jdk-25-and-26-alpine
WORKDIR /app
COPY build.gradle settings.gradle ./
run gradle dependencies --no-daemon
copy src ./src
RUN gradle bootJar --no-daemon -x test


# Stage 2: Run
FROM eclipse-temurin:26-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]