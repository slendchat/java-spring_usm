# Use official Maven image for building
FROM maven:3.8.8-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app



# Copy only the POM first, to cache dependencies efficiently
COPY app/pom.xml .

# Download dependencies separately (this will be cached)
RUN mvn dependency:go-offline

# Copy the rest of the project files
COPY app/. .

# Build the application, skipping tests (they can be run separately)
RUN mvn clean package -DskipTests

# Use a smaller JDK runtime for running the app
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

RUN apt-get update && apt-get install -y curl jq

# Copy the built JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

COPY testapi.sh /app/testapi.sh
RUN chmod +x /app/testapi.sh


# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
