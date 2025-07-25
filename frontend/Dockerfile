# ---------- BUILD STAGE ----------
FROM node:18-alpine AS build-stage

# Set working directory
WORKDIR /app

# Install pnpm globally
RUN npm install -g pnpm

# Copy project files
COPY . .

# Install dependencies using pnpm
RUN pnpm install

# Build the project for production
RUN pnpm run build

# ---------- PRODUCTION STAGE ----------
FROM nginx:stable-alpine AS production-stage

# Copy built frontend files to nginx public folder
COPY --from=build-stage /app/dist /usr/share/nginx/html

# Optional: custom nginx config to support Vue router history mode
#COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose port 80
EXPOSE 80

# Start nginx
CMD ["nginx", "-g", "daemon off;"]