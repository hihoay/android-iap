
## Tích hợp Shell để build Apk, Abb tự động.md

Tự động build apk và abb thành công thì sẽ đẩy lên channel discord để test hoặc release hoặc hỗ trợ quá trình CI/CD.

---
### Tạo tự động
- Script tạo tự động:
```bash
mkdir -p .taymay
curl "https://raw.githubusercontent.com/hihoay/android-service/refs/heads/feature/duonglh/jitpack.yml?token=GHSAT0AAAAAACV7VMU6IQA7ID5ICUI5E4AOZ4CGAHA" > .taymay/builder.sh
cat << EOF >> .env
DISCORD_WEBHOOK="https://discord.com/api/webhooks/1326763626060583015/WOhNQ03XQUnS-Bd8G-UmrHuv4EbR0NNhZZWISIgfCDqwRpQVAG7lE1SOqMC97QxKuJps" # để notify đến discord, thay đổi nếu muốn link vào channel khác. Mặc định là #chim-lợn
FILE_SERVICE="https://hihoay.com/file/service" # File Service để upfile đã build lên.
DISCORD_NOTIFY_API="https://testing.hihoay.com/worker/service/discord-notify" # đẩy nofity qua Worker Serivce
EOF
```
---
### Tạo thủ công
- Tại thư mục gốc của dự án tạo tệp `.env`:
```bash
DISCORD_WEBHOOK="https://discord.com/api/webhooks/1326763626060583015/WOhNQ03XQUnS-Bd8G-UmrHuv4EbR0NNhZZWISIgfCDqwRpQVAG7lE1SOqMC97QxKuJps" # để notify đến discord, thay đổi nếu muốn link vào channel khác. Mặc định là #chim-lợn
FILE_SERVICE="https://hihoay.com/file/service" # File Service để upfile đã build lên.
DISCORD_NOTIFY_API="https://testing.hihoay.com/worker/service/discord-notify" # đẩy nofity qua Worker Serivce
```

- Tại thư mục dự án tạo tệp: `.taymay/builder.sh`:
```bash
#!/bin/bash
echo '----.env  file'
apt update
apt install curl dos2unix tree jq wget vim  -y
dos2unix .env
cat .env
set -x
export $(cat .env | xargs)
echo "---------------env"
echo "DISCORD_WEBHOOK $DISCORD_WEBHOOK"
echo "DISCORD_NOTIFY_API $DISCORD_NOTIFY_API"
echo "FILE_SERVICE $FILE_SERVICE"
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
#-----------------------
echo "--------------------------------"
source /root/.bashrc
echo "--------------------------------"
source /etc/profile
echo "--------------------------------"
echo "ANDROID_HOME: $ANDROID_HOME"
echo "--------------------------------"
if [ -z "${ANDROID_HOME+x}" ]; then
    echo "ANDROID_HOME does not exist or is empty"
    apt update
    apt install curl dos2unix tree jq wget vim  -y
    apt install android-sdk  -y
    apt install openjdk-17-jdk -y
    wget https://dl.google.com/android/repository/commandlinetools-linux-10406996_latest.zip -O /root/commandlinetools.zip
    mkdir -p /root/android-sdk/cmdline-tools
    unzip /root/commandlinetools.zip -d ~/android-sdk/cmdline-tools
    mv ~/android-sdk/cmdline-tools/cmdline-tools ~/android-sdk/cmdline-tools/latest
    echo "export ANDROID_HOME=~/android-sdk" >> ~/.bashrc
    echo "export PATH=\$ANDROID_HOME/cmdline-tools/latest/bin:\$ANDROID_HOME/platform-tools:\$PATH" >> ~/.bashrc
    source ~/.bashrc
    yes | sdkmanager --licenses
    echo "Android SDK setup complete"
    cat ~/.bashrc >> /etc/profile
    source ~/.bashrc
    source /etc/profile
    export PATH="$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools:$PATH"
    export ANDROID_HOME="~/android-sdk"
    ANDROID_HOME=~/android-sdk
    #-----------------------
    yes | sdkmanager --licenses
    echo "sdk.dir=$ANDROID_HOME" > local.properties
fi



run_builder(){
  echo "sdk.dir=$ANDROID_HOME"
  echo "sdk.dir=$ANDROID_HOME" > local.properties
  echo '...............................local.properties'
  cat local.properties
  ls -a
 rm -rf app/build/,,,,,,,,,,,
dos2unix gradlew
chmod +x ./gradlew


 ./gradlew build
 if [ $? -ne 0 ]; then
     echo "Command failed!"
     exit 1
 else
     echo "Command succeeded!"
 fi
 ./gradlew assembleDebug
 if [ $? -ne 0 ]; then
     echo "Command failed!"
     exit 1
 else
     echo "Command succeeded!"
 fi
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

}

#run_builder
 tree app/build/outputs
 if [ $? -ne 0 ]; then
     echo "Command failed!"
     exit 1
 else
     echo "Command succeeded!"
 fi
for file in $(find app/build/outputs -type f -name "*.apk"); do
    echo "Processing file: $file"
    file_name=$(basename $file)
    out_file=$(upload_file $file $file_name)
    echo "url: $out_file"
    qr=$(curl "https://hihoay.com/file/service/qr?text=$out_file")
    msg="
    File: [$file_name]($out_file)
    QR: [Open]($qr)
    "
    notify_discord "Android build apk complete!" "$msg" $out_file "green"
done

for file in $(find app/build/outputs -type f -name "*.aab"); do
    echo "Processing file: $file"
    file_name=$(basename $file)
    out_file=$(upload_file $file $file_name)
    echo "url: $out_file"
    qr=$(curl "https://hihoay.com/file/service/qr?text=$out_file")
    msg="
    File: [$file_name]($out_file)
    QR: [Open]($qr)
    "
    notify_discord "Android build aab complete!" "$msg" $out_file "green"
done
set +x

```
