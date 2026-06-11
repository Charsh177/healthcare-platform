#!/bin/bash

# Stop all services and cleanup

echo "Stopping all services..."
docker-compose down -v

echo "✓ All services stopped and cleaned up"
