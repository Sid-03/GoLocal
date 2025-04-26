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
    COPY --from=builder /app/target/user-service-*.jar app.jar
    
    # Expose the port Spring Boot runs on (default 8080)
    EXPOSE 8080
    
    # Environment variables (will be set by docker-compose)
    ENV DB_HOST="postgres-db" \
        DB_PORT="5432" \
        DB_NAME="user_db" \
        DB_USER="postgres" \
        DB_PASS="password" \
        JWT_SECRET="Default_Weak_Secret_Must_Be_Overridden" \
        JWT_EXPIRATION_MS="86400000"
    
    # Run the JAR file
    ENTRYPOINT ["java", "-jar", "app.jar"]