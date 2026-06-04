# Step 1: Use an official lightweight OpenJDK image as the foundation
FROM eclipse-temurin:21-jre-jammy

# Step 2: Set the internal directory inside the Docker container where our app will live
WORKDIR /app

# Step 3: Copy the compiled executable JAR file from Gradle's build folder into the container
COPY build/libs/*.jar app.jar

# Step 4: Expose port 8080 so we can access the API from outside the container
EXPOSE 8080

# Step 5: The exact command to boot up your Spring Boot application inside the container
ENTRYPOINT ["java", "-jar", "app.jar"]