#!/bin/bash

echo "🚀 Stopping any running containers..."
docker-compose down

echo "🔄 Removing old Docker images..."
docker system prune -af

echo "📦 Building and starting the containers..."
docker-compose up --build -d

echo "✅ Containers are now running!"
echo "📜 To check logs, use: docker logs -f <container_name>"
echo "🐳 To check running containers, use: docker ps"

echo "⏳ Waiting for containers to initialize..."
sleep 11

echo "📜 Fetching logs from library_app..."
docker logs -f library_app