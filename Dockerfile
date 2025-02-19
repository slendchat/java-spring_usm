# Use OpenJDK 17 slim image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# # Copy the built JAR from Ubuntu VM
# COPY app/target/library-0.0.1-SNAPSHOT.jar app.jar

# # Run the application
# ENTRYPOINT ["java", "-jar", "app.jar"]

# Copy Maven Wrapper first
COPY app/mvnw .
COPY app/.mvn .mvn

# Download dependencies and build the application
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B
RUN ./mvnw package -DskipTests


# Expose the port your application runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "target/library-0.0.1-SNAPSHOT.jar"]