#!/bin/bash

echo "[+] Updating system packages..."
sudo apt update -y

echo "[+] Installing Java..."
sudo apt install -y openjdk-11-jdk
java -version

echo "[+] Installing Docker..."
sudo apt install -y docker.io
sudo usermod -aG docker $USER

echo "[+] Installing Docker Compose..."
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" \
-o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
docker-compose --version

echo "[+] Running docker-compose..."
cd "$(dirname "$0")"  # go to infra folder
docker-compose up -d