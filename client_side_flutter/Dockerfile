# Use the official Flutter image
FROM ghcr.io/cirruslabs/flutter:3.24.5

# Set the working directory
WORKDIR /app

# Copy the Flutter project files
COPY . /app

# Get dependencies
RUN flutter pub get

# Build the Flutter web app
RUN flutter build web

# Expose port 8081 (since 8000 is taken)
EXPOSE 8081

# Serve the app with a simple HTTP server
CMD ["flutter", "run", "-d", "web-server", "--web-port", "8081", "--web-hostname", "0.0.0.0"]
