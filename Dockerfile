# Use a multi-stage build for efficiency and to reduce image size
# Stage 1: Build stage with Maven and JDK 17
FROM maven:3.8.4-openjdk-17 AS MAVEN_BUILD

# Copy the pom.xml and source code
COPY pom.xml /build/
COPY src /build/src/
COPY tokens /build/tokens/

# Set the working directory for Maven build
WORKDIR /build/

# Build the application with Maven, skipping tests
RUN mvn clean install -U -Dmaven.test.skip=true

# Stage 2: Final stage with JDK 17 JRE
FROM openjdk:17-jdk-slim

# Set up the application directory
WORKDIR /opt/ticketapp

# Copy the JAR file from the Maven build stage to the final image
COPY --from=MAVEN_BUILD /build/target/ticketapp-0.0.1.jar /opt/ticketapp/ticketapp.jar

# Expose port 8092g
EXPOSE 8080

# Set the command to run the application
CMD ["java", "-jar", "/opt/ticketapp/ticketapp.jar", "--server.port=8080"]
