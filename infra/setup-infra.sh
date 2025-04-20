#!/bin/bash

set -e  # exit on error

echo "[+] Updating system packages..."
sudo apt update -y

echo "[+] Installing dependencies: Git, Java, Docker, Docker Compose..."
sudo apt install -y git openjdk-11-jdk docker.io curl unzip

echo "[+] Verifying Java..."
java -version

echo "[+] Adding current user to Docker group..."
sudo usermod -aG docker $USER

echo "[+] Installing Docker Compose..."
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" \
-o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
docker-compose --version

echo "[+] Cloning automation framework repo..."
git clone https://github.com/madhusudhan241/HybridFramework.git

echo "[+] Navigating to infra and running docker-compose..."
cd HybridFramework/infra
docker-compose up -d