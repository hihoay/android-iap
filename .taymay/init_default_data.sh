
APP_ID=$( cat app/google-services.json | grep package_name | sed -n 's/.*"package_name": *"\([^"]*\)".*/\1/p')

API=https://production.hihoay.com/main/session/service/api/v1/remotes/view/

dir=app/src/main/assets/
mkdir -p $dir
#-----------

JSON_DATA=$(jq -n \
  --arg app_id "$APP_ID" \
  '{app_id: $app_id, country_code: "global", type: "data", version: "module"}'
)

echo "$APP_ID for data ---------"
echo " $JSON_DATA"

curl -X POST "$API" -H 'Content-Type: application/json' -d "$JSON_DATA" > $dir/remote.json

#-----------

JSON_DATA=$(jq -n \
  --arg app_id "$APP_ID" \
  '{app_id: $app_id, country_code: "global", type: "adx", version: "module"}'
)
echo "$APP_ID for adx ---------"
echo " $JSON_DATA"

curl -X POST "$API" -H 'Content-Type: application/json' -d "$JSON_DATA" > $dir/adx.json

