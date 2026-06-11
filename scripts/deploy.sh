#!/bin/bash

# Build and deploy script for Healthcare Platform

set -e

echo "=========================================="
echo "Healthcare Platform - Build & Deploy"
echo "=========================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check prerequisites
echo -e "${YELLOW}Checking prerequisites...${NC}"

if ! command -v docker &> /dev/null; then
    echo -e "${RED}Docker is not installed!${NC}"
    exit 1
fi

if ! command -v docker-compose &> /dev/null; then
    echo -e "${RED}Docker Compose is not installed!${NC}"
    exit 1
fi

echo -e "${GREEN}✓ Prerequisites checked${NC}\n"

# Build Maven project
echo -e "${YELLOW}Building Maven project...${NC}"
mvn clean install -DskipTests

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Maven build successful${NC}\n"
else
    echo -e "${RED}✗ Maven build failed${NC}"
    exit 1
fi

# Build Docker images
echo -e "${YELLOW}Building Docker images...${NC}"
docker-compose build

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Docker build successful${NC}\n"
else
    echo -e "${RED}✗ Docker build failed${NC}"
    exit 1
fi

# Start services
echo -e "${YELLOW}Starting services...${NC}"
docker-compose up -d

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Services started${NC}\n"
else
    echo -e "${RED}✗ Failed to start services${NC}"
    exit 1
fi

# Wait for services to be ready
echo -e "${YELLOW}Waiting for services to be ready...${NC}"
sleep 10

# Check service health
echo -e "${YELLOW}Checking service health...${NC}"
curl -s http://localhost:8080/actuator/health | jq . || echo "Gateway not yet available"

echo -e "${GREEN}=========================================="
echo "✓ Deployment complete!"
echo "=========================================${NC}"
echo ""
echo "Services are running at:"
echo "  - API Gateway: http://localhost:8080"
echo "  - Auth Service: http://localhost:8081"
echo "  - Patient Service: http://localhost:8082"
echo "  - Claims Service: http://localhost:8083"
echo "  - Billing Service: http://localhost:8084"
echo "  - File Service: http://localhost:8085"
echo "  - PostgreSQL: localhost:5432"
echo ""
echo "Swagger API Documentation:"
echo "  - http://localhost:8080/swagger-ui.html"
echo ""
echo "To view logs:"
echo "  - docker-compose logs -f <service-name>"
echo ""
echo "To stop services:"
echo "  - docker-compose down"
