#!/bin/bash
set -e


moduleName=$1

APP_ID=$(grep package_name $moduleName/google-services.json | sed -n 's/.*"package_name": *"\([^"]*\)".*/\1/p')
echo
echo "App ID = $APP_ID"
echo

API="https://production.hihoay.com/main/session/service/api/v1/remotes/view/"
dir="$moduleName/src/main/assets"
mkdir -p "$dir"

#----------- DATA -----------
JSON_DATA=$(jq -n \
  --arg app_id "$APP_ID" \
  '{app_id: $app_id, country_code: "global", type: "data", version: "module"}'
)

echo "[$APP_ID] for data ---------"


echo "$JSON_DATA"

curl -sS -X POST "$API" -H 'Content-Type: application/json' -d "$JSON_DATA" > "$dir/remote.json"

#----------- ADX -----------
JSON_DATA=$(jq -n \
  --arg app_id "$APP_ID" \
  '{app_id: $app_id, country_code: "global", type: "adx", version: "module"}'
)

echo "[$APP_ID] for adx ---------"
echo "$JSON_DATA"

curl -sS -X POST "$API" -H 'Content-Type: application/json' -d "$JSON_DATA" > "$dir/adx.json"
