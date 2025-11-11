#!/bin/bash
. .taymay/discord.sh
. .taymay/file.sh
. .taymay/git_tag.sh
. .taymay/android.sh

app_name=$1
module_name=$2
start_time=$(date +%s)
apt update
apt install curl dos2unix tree jq wget vim  bc -y
dos2unix .env
cat .env
export $(cat .env | xargs)
git fetch --tags
if [ "$BRANCH_NAME" != "develop" ] && [ "$BRANCH_NAME" != "release" ] && [ "$BRANCH_NAME" != "main" ] && [ "$BRANCH_NAME" != "testing" ]; then
  exit  0
fi
newTag=$(increase_tag $BRANCH_NAME)
echo "newTag $newTag"
IFS='.' read -r vmain vrelease vtesting vdevelop <<< "$newTag"
echo "Main: $vmain"
echo "Release: $vrelease"
echo "Testing: $vtesting"
echo "Develop: $vdevelop"


build_file="$module_name/build.gradle"

dos2unix $build_file
versionName=$(grep 'version = "\(.*\)"' $build_file | sed 's/^[ \t]*//g')
new_versionName="version = \"$newTag\""
sed "s/$versionName/$new_versionName/g" $build_file > tmp.txt

mv tmp.txt $build_file
build_app="$app_name/build.gradle"
dos2unix $build_app
sed "s/$(grep 'versionName "\(.*\)"' $build_app)/versionName \"$newTag\"/g" $build_app > tmp.txt &&  mv tmp.txt $build_app
sed "s/$(grep -E 'versionCode [0-9]+' $build_app)/versionCode $vmain/g" $build_app > tmp.txt && mv tmp.txt $build_app




ANDROID_HOME=/root/android-sdk

source /etc/profile
echo "ANDROID_HOME: $ANDROID_HOME"
run_builder
tree app/build/outputs

if [ $? -ne 0 ]; then
    echo "Command failed!"
    exit 1
else
    auto_create_tag
    echo "Command succeeded!"
fi

if  [ "$BRANCH_NAME" == "release" ] || [ "$BRANCH_NAME" == "main" ] || [ "$BRANCH_NAME" == "testing" ]; then
end_time=$(date +%s) # Thời gian kết thúc (giây từ Unix epoch)
elapsed_seconds=$((end_time - start_time))
elapsed_minutes=$(echo "scale=2; $elapsed_seconds / 60" | bc)

for file in $(find app/build/outputs -type f \( -name "*.apk" -o -name "*.aab" \)); do
   echo "Processing file: $file"
   file_name=$(basename $file)
   out_file=$SITE$(upload_file $file $file_name)
   echo "url: $out_file"
   qr=$SITE$(curl $FILE_SERVICE"/qr?text=$out_file")
  echo "qr: $qr"
   msg="
   File: [$file_name]($out_file)
   Tag: *$newTag*
   Path: *$file*
   Size: *$(ls -lh "$file" | awk '{print $5}')*
   QR: [Open]($qr)
   Build long: *$elapsed_seconds*s
   Package: \`implementation(\"com.taymay:android-$module_name:$newTag\")\`
   "
    # Tạo JSON payload hợp lệ
    JSON_PAYLOAD=$(
        jq -n \
            --arg username "$GIT_AUTHOR_NAME" \
            --arg avatar_url "https://mirrors.tuna.tsinghua.edu.cn/jenkins/art/jenkins-logo/256x256/headshot.png" \
            --arg title "Build Success" \
            --arg url "$out_file" \
            --arg description "$msg" \
            --arg image_url "$qr" \
            '{username: $username, avatar_url: $avatar_url, embeds: [{title: $title, url: $url, description: $description, color: 3066993, image: {url: $image_url}}]}'
    )
    if [ "$BRANCH_NAME" == "main" ]; then
        curl -H 'Content-Type: application/json' -X POST -d "$JSON_PAYLOAD" $DISCORD_WEBHOOK_PRODUCTION
    fi

    if [ "$BRANCH_NAME" == "release" ]; then
        curl -H 'Content-Type: application/json' -X POST -d "$JSON_PAYLOAD" $DISCORD_WEBHOOK_PRODUCTION
    fi

    if [ "$BRANCH_NAME" == "testing" ]; then
        curl -H 'Content-Type: application/json' -X POST -d "$JSON_PAYLOAD" $DISCORD_WEBHOOK_TESTING
    fi
    if [ "$BRANCH_NAME" == "develop" ]; then
          curl -H 'Content-Type: application/json' -X POST -d "$JSON_PAYLOAD" $DISCORD_WEBHOOK_GITHUB
    fi
done
fi
