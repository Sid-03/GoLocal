# --- Build Stage ---
    FROM openjdk:17-jdk-slim as builder
    WORKDIR /app
    
    # Copy Maven wrapper
    COPY .mvn/ .mvn
    COPY mvnw pom.xml ./
    
    # Download dependencies
    RUN ./mvnw dependency:resolve
    
    # Copy source code
    COPY src ./src
    
    # Package application
    RUN ./mvnw package -DskipTests
    
    # --- Runtime Stage ---
    FROM openjdk:17-jdk-slim
    WORKDIR /app
    
    # Copy the JAR from builder stage
    COPY --from=builder /app/target/product-service-*.jar app.jar
    
    # Expose the port Spring Boot runs on (defined in application.properties)
    EXPOSE 8081
    
    # Environment variables (will be set by docker-compose)
    ENV DB_HOST="postgres-db" \
        DB_PORT="5432" \
        DB_NAME="product_db" \
        DB_USER="postgres" \
        DB_PASS="password"
    
    # Run the JAR file
    ENTRYPOINT ["java", "-jar", "app.jar"]