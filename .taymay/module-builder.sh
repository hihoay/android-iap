#!/bin/bash
echo '----.env  file'
start_time=$(date +%s) # Thời gian bắt đầu (giây từ Unix epoch)
apt update
apt install curl dos2unix tree jq wget vim  bc -y
dos2unix .env
cat .env
export $(cat .env | xargs)
echo "---------------env"
echo "DISCORD_WEBHOOK $DISCORD_WEBHOOK"
echo "DISCORD_NOTIFY_API $DISCORD_NOTIFY_API"
echo "FILE_SERVICE $FILE_SERVICE"
#!/bin/bash
android_increase_version_name() {
    branch_name=$1
    # nếu branch là release thì tăng lên 1
    build_file=$2
        unix2dos $build_file

        # xoa ky tu tab dau dong
        versionName=$(grep 'versionName "\(.*\)"' $build_file | sed 's/^[ \t]*//g')

        if [ -z "$versionName" ]; then
            versionName="0.0.0.0"
        fi
        # lấy tên versionName
        version=$(echo $versionName | sed 's/versionName "\(.*\)"/\1/')
        # echo $version
        # nếu branch là develop thì tăng lên 1 1.0.0.1
        if [ "$branch_name" == "develop" ]; then
            version=$(echo $version | awk -F. -v OFS=. '{$4++; print}')
        fi
        # nếu branch là testing thì tăng lên 1 1.0.1.0
        if [ "$branch_name" == "testing" ]; then
            version=$(echo $version | awk -F. -v OFS=. '{$3++; $4=0; print}')
        fi
        # nếu branch là release thì tăng lên 1 ở số thứ 2 1.1.0 còn số 3 là 0
        if [ "$branch_name" == "release" ]; then
            version=$(echo $version | awk -F. -v OFS=. '{$2++; $3=0; $4=0; print}')
        fi
        #nếu branch là main thì tăng lên 1 ở số thứ 1 2.0.0 còn số 2 và 3 là 0
        if [ "$branch_name" == "main" ]; then
            version=$(echo $version | awk -F. -v OFS=. '{$1++; $2=0; $3=0; $4=0; print}')
        fi
        version=$(echo $version | tr -d '\r')
        # echo $version
        new_versionName="versionName \"$version\""
        # echo $versionName '>' $new_versionName
        if [ $? -eq 0 ]; then
            # ghi version vào $build_file
            sed "s/$versionName/$new_versionName/g" $build_file > tmp.txt
            mv tmp.txt $build_file
            echo $version
        fi
}
android_increase_version() {
    new_tag=$1
    build_file=$2
    unix2dos $build_file
    versionName=$(grep 'version = "\(.*\)"' $build_file | sed 's/^[ \t]*//g')
    new_version="version = \"$new_tag\""
    new_version=$(echo $new_version | sed 's/\n//g')
    if [ $? -eq 0 ]; then
        sed "s/$versionName/$new_version/g" $build_file > tmp.txt
        mv tmp.txt $build_file
        echo $new_version
    fi
}
android_increase_version_code() {
    branch_name=$1
        build_file=$2
    if [ "$branch_name" == "main" ]; then
        # Lấy dòng chứa versionCode từ $build_file
        versionCode=$(grep -E 'versionCode [0-9]+' $build_file | sed 's/^[ \t]*//g')
        echo $versionCode
        # Nếu không tìm thấy versionCode, thoát với lỗi
        if [ -z "$versionCode" ]; then
            echo "Error: versionCode not found in $build_file" >&2
            exit 1
        fi
        echo "Current: $versionCode"
        # Lấy số từ versionCode
        code_number=$(echo "$versionCode" | awk '{print $2}' | tr -d '\n')
        code_number=$(echo $code_number | tr -d '\r' | tr -d '\n')
        # Đảm bảo code_number là số nguyên
        if [[ "$code_number" =~ ^[0-9]+$ ]]; then
            # Tăng giá trị lên 1
            code_number=$((code_number + 1))
            # Gán lại versionCode mới
            new_versionCode="versionCode $code_number"
            echo "New versionCode: $new_versionCode"
        else
            echo "Error: code_number is not a valid number" >&2
            exit 1
        fi
        # Kiểm tra kết quả
        if [ $? -eq 0 ]; then
            echo "versionCode updated successfully in $build_file"
            sed "s/$versionCode$/$new_versionCode/" $build_file > tmp.txt
            mv tmp.txt $build_file
            echo $new_versionCode
        else
            echo "Error: Failed to update versionCode in $build_file" >&2
            exit 1
        fi
    else
        echo "This operation is allowed only on the 'main' branch."
    fi
}
get_current_tag() {
    # current_tag=$(git describe --tags --abbrev=0 2>/dev/null)
    # if [ -z "$current_tag" ]; then
    #     current_tag="0.0.0.0"
    # fi
    # echo $current_tag
    # Lấy danh sách tags từ remote
    tags=$(git ls-remote --tags origin | awk -F'/' '{print $NF}' | sort -V)
    # Lấy tag mới nhất
    latest_tag=$(echo "$tags" | tail -n 1)
    if [ -z "$latest_tag" ]; then
        latest_tag="1.0.0.0"
    fi
    echo $latest_tag
}
increase_tag() {
    branch_name=$1
    current_tag=$(get_current_tag)
    # nếu branch là release thì tăng lên 1
        # xoa ky tu tab dau dong
        version=$(echo $current_tag | sed 's/v\(.*\)/\1/')
        # echo $version
             # nếu branch là develop thì tăng lên 1 1.0.0.1
        if [ "$branch_name" == "develop" ]; then
            version=$(echo $version | awk -F. -v OFS=. '{$4++; print}')
        fi
        # nếu branch là develop thì tăng lên 1 1.0.1.0
        if [ "$branch_name" == "testing" ]; then
            version=$(echo $version | awk -F. -v OFS=. '{$3++; $4=0; print}')
        fi
        # nếu branch là release thì tăng lên 1 ở số thứ 2 1.1.0 còn số 3,4 là 0
        if [ "$branch_name" == "release" ]; then
            version=$(echo $version | awk -F. -v OFS=. '{$2++; $3=0; $4=0; print}')
        fi
        #nếu branch là main thì tăng lên 1 ở số thứ 1 2.0.0 còn số 2,3,4 là 0
        if [ "$branch_name" == "main" ]; then
            version=$(echo $version | awk -F. -v OFS=. '{$1++; $2=0; $3=0; $4=0; print}')
        fi
        version=$(echo $version | tr -d '\r')
        # echo $version
        # echo $versionName
        version=$(echo $version | sed 's/\n//g')
        new_tag="$version"
        new_tag=$(echo $new_tag | sed 's/\n//g')
        # echo "$versionName to $new_tag"
        # ghi version vào $build_file
        if [ $? -eq 0 ]; then
            echo $new_tag
        fi

}
git fetch --tags
notify_discord() {
    local title="$1"
    local message="$2"
    local url="$3"
    local color="$4"
    local webhook="$DISCORD_WEBHOOK"
    curl -X POST "$DISCORD_NOTIFY_API" \
        -H "Content-Type: application/json" \
        -d "$(jq -n --arg title "$title" --arg message "$message" --arg url "$url" --arg color "$color" --arg webhook "$webhook" \
                '{title: $title, message: $message, url: $url, color: $color, webhook: $webhook}')"
}
upload_file() {
    local file_path="$1"
    local file_name="$2"
    echo $(curl -F "file=@\"$file_path\";filename=\"$file_name\"" "$FILE_SERVICE/upload")
}
current_branch=$(sed 's/origin\///g' <<< $1)
echo "current_branch $current_branch"
  if [ "$current_branch" != "develop" ] && [ "$current_branch" != "release" ] && [ "$current_branch" != "main" ] && [ "$current_branch" != "testing" ]; then
exit  0
fi

module_name=$2




newTag=$(increase_tag $current_branch)
echo "newTag $newTag"
IFS='.' read -r vmain vrelease vtesting vdevelop <<< "$newTag"
# In kết quả
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


build_app="app/build.gradle"
dos2unix $build_app
sed "s/$(grep 'versionName "\(.*\)"' $build_app)/versionName \"$newTag\"/g" $build_app > tmp.txt &&  mv tmp.txt $build_app
sed "s/$(grep -E 'versionCode [0-9]+' $build_app)/versionCode $vmain/g" $build_app > tmp.txt && mv tmp.txt $build_app

ANDROID_HOME=/root/android-sdk
source /etc/profile
echo "ANDROID_HOME: $ANDROID_HOME"

run_builder(){
  echo "sdk.dir=$ANDROID_HOME" > local.properties
  echo '...............................local.properties'
  cat local.properties
  ls -a
 rm -rf app/build/
dos2unix gradlew
chmod +x ./gradlew

./gradlew clean
./gradlew build

 if [ $? -ne 0 ]; then
     echo "Command failed!"
     exit 1
 else
     echo "Command succeeded!"
 fi


if [ "$current_branch" == "develop" ] ||  [ "$current_branch" == "testing" ]; then
     ./gradlew assembleDebug
     if [ $? -ne 0 ]; then
         echo "Command failed!"
         exit 1
     else
         echo "Command succeeded!"
     fi
  find app/build/outputs -type f -name "*-release*" -delete

fi
if [ "$current_branch" == "release" ] || [ "$current_branch" == "main" ]; then

     ./gradlew assembleRelease
     if [ $? -ne 0 ]; then
         echo "Command failed!"
         exit 1
     else
         echo "Command succeeded!"
     fi

     ./gradlew :app:bundleRelease
     if [ $? -ne 0 ]; then
         echo "Command failed!"
         exit 1
     else
         echo "Command succeeded!"
     fi
 find app/build/outputs -type f -name "*-debug*" -delete

fi
}
auto_create_tag(){
    git fetch origin --tags
  current_branch=$(sed 's/origin\///g' <<< $GIT_BRANCH)
  # current_branch=develop
  echo "current_branch: "$current_branch
  if [ "$current_branch" == "develop" ] || [ "$current_branch" == "release" ] || [ "$current_branch" == "main" ] || [ "$current_branch" == "testing" ]; then
      git branch --show-current
      git fetch origin --tags
      ./gradlew publish
      git tag -a  "$newTag" -m "[$current_branch] Auto create tag "$newTag
      git push origin --tags
      echo $newTag
  fi
}

run_builder

 tree app/build/outputs

 if [ $? -ne 0 ]; then
     echo "Command failed!"
     exit 1
 else
      auto_create_tag
     echo "Command succeeded!"
 fi





  if  [ "$current_branch" == "release" ] || [ "$current_branch" == "main" ] || [ "$current_branch" == "testing" ]; then
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
