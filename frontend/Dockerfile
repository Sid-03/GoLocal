# --- Stage 1: Build the React application ---
    FROM node:18-alpine as build-stage
    # Use a specific Node version appropriate for your Create React App version
    
    WORKDIR /app
    
    # Copy package.json and package-lock.json (or yarn.lock)
    COPY package*.json ./
    
    # Install dependencies
    # Use 'npm ci' for potentially faster and more reliable installs in CI/CD
    RUN npm install
    
    # Copy the rest of the application source code
    COPY . .
    
    # Define ARG for API Gateway URL (can be overridden during docker build)
    # The default value here is less important as it's set in docker-compose build args
    ARG REACT_APP_API_GATEWAY_URL=http://localhost:9000/api
    
    # Set ENV variable from ARG so it's baked into the build
    ENV REACT_APP_API_GATEWAY_URL=$REACT_APP_API_GATEWAY_URL
    
    # Build the React application for production
    # This creates an optimized build in the /app/build directory
    RUN npm run build
    
    # --- Stage 2: Serve the application using Nginx ---
    FROM nginx:1.25-alpine
    # Use a specific stable Nginx version
    
    # Copy the static build files from the build stage to Nginx's default serve directory
    COPY --from=build-stage /app/build /usr/share/nginx/html
    
    # Copy the custom Nginx configuration file
    # This file handles routing for single-page applications (SPA)
    COPY nginx.conf /etc/nginx/conf.d/default.conf
    
    # Expose port 80 (Nginx default HTTP port)
    EXPOSE 80
    
    # Command to start Nginx in the foreground
    # 'daemon off;' prevents the container from exiting immediately
    CMD ["nginx", "-g", "daemon off;"]