#!/bin/bash

# Script to build and run the Docker containers

# echo "Entering the directory with app"
# cd app

# echo "Compiling java app"
# sudo mvn clean package

# echo "Going back"
# cd .. 

echo "🚀 Stopping any running containers..."
docker-compose down

echo "🔄 Removing old Docker images..."
docker system prune -af

echo "📦 Building and starting the containers..."
docker-compose up --build -d

echo "✅ Containers are now running!"
echo "📜 To check logs, use: docker logs -f <container_name>"
echo "🐳 To check running containers, use: docker ps"

