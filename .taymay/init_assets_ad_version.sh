app_id=$( cat app/google-services.json | grep package_name | sed -n 's/.*"package_name": *"\([^"]*\)".*/\1/p')
url=https://hihoay.com/ads/service/ad_version/$app_id.json
ad_remotes=$(curl $url)
# Lấy mã trạng thái HTTP của URL
status_code=$(curl -s -o /dev/null -w "%{http_code}" "$url")
# Kiểm tra và đưa ra thông báo dựa trên mã trạng thái
if [ "$status_code" -eq 200 ]; then
    echo "URL $url hoạt động (Status Code: $status_code)"
    mkdir -p app/src/main/assets/
    echo $ad_remotes > app/src/main/assets/ads.json
else
    echo "URL $url không hoạt động (Status Code: $status_code)"
    exit 1
fi
url=https://hihoay.com/ads/service/data_version/$app_id.json
ad_remotes=$(curl $url)
# Lấy mã trạng thái HTTP của URL
status_code=$(curl -s -o /dev/null -w "%{http_code}" "$url")
# Kiểm tra và đưa ra thông báo dựa trên mã trạng thái
if [ "$status_code" -eq 200 ]; then
    echo "URL $url hoạt động (Status Code: $status_code)"
    mkdir -p app/src/main/assets/
    echo $ad_remotes > app/src/main/assets/data.json
else

    echo "URL $url không hoạt động (Status Code: $status_code)"
    echo '[]' > app/src/main/assets/data.json
fi

