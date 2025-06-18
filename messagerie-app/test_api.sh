#!/bin/bash

BASE_URL="http://localhost:8080"

echo "Création des utilisateurs..."

curl -s -X POST "$BASE_URL/users" -H "Content-Type: application/json" -d '{"username":"alice","email":"alice@example.com","password":"secret"}' | jq .

curl -s -X POST "$BASE_URL/users" -H "Content-Type: application/json" -d '{"username":"bob","email":"bob@example.com","password":"secret"}' | jq .

echo -e "\nCréation du channel général..."

curl -s -X POST "$BASE_URL/channels" -H "Content-Type: application/json" -d '{
  "name": "general",
  "isPrivate": false,
  "memberIds": [1,2]
}' | jq .

echo -e "\nEnvoi d'un message..."

curl -s -X POST "$BASE_URL/messages" -H "Content-Type: application/json" -d '{
  "content": "Salut tout le monde!",
  "senderId": 1,
  "channelId": 1
}' | jq .

echo -e "\nRécupération des messages du channel 1..."

curl -s "$BASE_URL/messages/channel/1" | jq .
