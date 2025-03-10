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

echo "📜 Fetching logs from library_app..."
sleep 10
./testapi.sh