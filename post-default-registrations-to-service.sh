#!/usr/bin/env bash

set -e # Fail on first error

HOST=${1:-localhost}
PORT=${2:-8081}
URL="http://$HOST:$PORT/api/v1/registrations"

REGISTRATIONS=(
  "{\"eventId\": \"72ab7c8b-c0d5-4ab2-8c63-5cf1ad0b439b\",\"participant\":{\"email\":\"ulf.lundell@knowit.se\"},\"registrationId\":\"9e7f48fa-c811-4acc-a8a6-8ad44037c575\"}"
  "{\"eventId\": \"82ab7c8b-c0d5-4ab2-8c63-5cf1ad0b439b\",\"participant\":{\"email\":\"lars.bandage@knowit.se\"},\"registrationId\":\"b4de9a22-5bf5-4010-92b7-385f3341743e\"}"
)

echo "Sending registrations to path $URL.."

for registration in "${REGISTRATIONS[@]}"; do
  curl -XPOST -H 'Content-Type: application/json' -d "$registration" "$URL"
done

echo "Successfully sent ${#REGISTRATIONS[@]} registrations"
