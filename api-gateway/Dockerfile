# --- Build Stage ---
# Use a specific JDK version matching your project requirements
FROM openjdk:17-jdk-slim as builder

# Set the working directory inside the container
WORKDIR /app

# Copy Maven wrapper files (if you use ./mvnw)
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download dependencies to leverage Docker cache layers
# Use 'dependency:go-offline' for potentially faster builds if network is slow
RUN ./mvnw dependency:resolve

# Copy the project source code
COPY src ./src

# Package the application (skip tests during build for faster Docker builds)
# Ensure Maven uses settings that don't require user input if any
RUN ./mvnw package -DskipTests

# --- Runtime Stage ---
# Use a JRE image for a smaller final image size
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the executable JAR file from the builder stage
# Adjust the path if your JAR name is different
COPY --from=builder /app/target/api-gateway-*.jar app.jar

# Expose the port the application runs on (must match server.port in application.yml)
EXPOSE 9000

# Environment variables needed by the application
# These will be overridden by docker-compose environment section, but good to have defaults
ENV USER_SERVICE_URL="lb://user-service" \
    PRODUCT_SERVICE_URL="lb://product-service" \
    INQUIRY_SERVICE_URL="lb://inquiry-service" \
    JWT_SECRET="Default_Weak_Secret_Must_Be_Overridden"

# Command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]

# Optional: Add a healthcheck specific to Spring Boot Actuator
# HEALTHCHECK --interval=15s --timeout=3s --start-period=30s \
#  CMD curl --fail http://localhost:9000/actuator/health || exit 1