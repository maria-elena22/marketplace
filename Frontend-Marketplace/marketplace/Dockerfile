# Stage 1: Build the Angular app
FROM node:19.7.0-alpine3.17 as builder

WORKDIR /app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application files
COPY . .

# Build the Angular app
RUN npm run build --prod

# Stage 2: Serve the Angular app using Nginx
FROM nginx:1.23.4-alpine

# Remove default nginx configurations
RUN rm -rf /usr/share/nginx/html/*

# Copy the built Angular app from the previous stage
COPY --from=builder /app/dist/* /usr/share/nginx/html/

# Copy custom nginx configuration (if needed)
# COPY nginx.conf /etc/nginx/nginx.conf

# Expose port 80
EXPOSE 80 8080

# Start Nginx server
CMD ["nginx", "-g", "daemon off;"]
