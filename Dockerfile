# Stage 1: Build the Spring Boot app
FROM gradle:jdk17 as builder
WORKDIR /app
COPY build.gradle .
COPY settings.gradle .
COPY gradlew .
COPY gradle /gradle
RUN chmod +x ./gradlew
RUN gradle wrapper
RUN ./gradlew dependencies --no-daemon

# Set JVM arguments for Gradle daemon
ENV GRADLE_OPTS="-Xmx256m -Xms256m"

# Copying only the necessary files to leverage Docker cache
COPY src /app/src
RUN ./gradlew build -PusePostgres -x test --no-daemon

# Stage 2: Create the final image
FROM openjdk:17-oracle as runtime
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/build/libs/youtube-feed-api-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the port the app will run on
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
