# Use a base image with Java and Maven pre-installed
FROM maven:3.8.1-openjdk-11-slim AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml file
COPY pom.xml .

# Copy the entire source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Create a new stage for the final image
FROM openjdk:11-jre-slim

# Set the working directory
WORKDIR /app

# Copy the compiled artifact from the build stage
COPY --from=build "/app/target/ticketapp-0.0.1.jar" ./app.jar

# Expose the port your app runs on
EXPOSE 8092

# Define the startup command
CMD ["java", "-jar", "app.jar"]
