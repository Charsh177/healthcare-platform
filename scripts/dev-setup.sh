#!/bin/bash

# Development environment setup

set -e

echo "=========================================="
echo "Healthcare Platform - Local Development Setup"
echo "=========================================="

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Maven is required but not installed. Please install Maven first."
    exit 1
fi

# Check if Java 17+ is installed
JAVA_VERSION=$(java -version 2>&1 | grep -oP 'version "\K[^"]*')
echo "Java version: $JAVA_VERSION"

# Start PostgreSQL (using Docker)
echo "Starting PostgreSQL..."
docker run -d --name healthcare-postgres \
    -e POSTGRES_DB=healthcare_db \
    -e POSTGRES_USER=healthcare_user \
    -e POSTGRES_PASSWORD=healthcare_secure_password_123 \
    -p 5432:5432 \
    -v postgres_data:/var/lib/postgresql/data \
    postgres:15-alpine

echo "Waiting for PostgreSQL to be ready..."
sleep 5

# Build all services
echo "Building all services..."
mvn clean install -DskipTests

if [ $? -eq 0 ]; then
    echo "✓ Build successful!"
    echo ""
    echo "Services are ready. To start a service, run:"
    echo "  cd <service-name>"
    echo "  mvn spring-boot:run"
    echo ""
    echo "Or run all services using docker-compose:"
    echo "  docker-compose up"
else
    echo "✗ Build failed"
    exit 1
fi
