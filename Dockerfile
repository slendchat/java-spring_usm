# # Use OpenJDK 17 slim image
# FROM openjdk:17-jdk-slim

# # Set working directory
# WORKDIR /app

# # # Copy the built JAR from Ubuntu VM
# # COPY app/target/library-0.0.1-SNAPSHOT.jar app.jar

# # # Run the application
# # ENTRYPOINT ["java", "-jar", "app.jar"]

# # Copy Maven Wrapper first
# COPY app/pom.xml .
# COPY app/mvnw .
# COPY app/.mvn .mvn

# # Download dependencies and build the application
# RUN chmod +x mvnw && ./mvnw dependency:go-offline -B
# RUN ./mvnw package -DskipTests


# # Expose the port your application runs on
# EXPOSE 8080

# # Run the application
# ENTRYPOINT ["java", "-jar", "target/library-0.0.1-SNAPSHOT.jar"]
# Use an official Maven image as the build stage
FROM maven:3.8.8-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy the project files
COPY app/* .
COPY app/pom.xml .
COPY app/src ./src
# Download dependencies (to cache them in Docker layers)
RUN mvn dependency:go-offline

# Build the application
RUN mvn clean package

# Use a smaller JDK runtime for running the app
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
