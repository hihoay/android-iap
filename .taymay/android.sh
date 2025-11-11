

install_android_sdk(){

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

}

run_builder(){
  echo "sdk.dir=$ANDROID_HOME"
  echo "sdk.dir=$ANDROID_HOME" > local.properties
  echo '...............................local.properties'
  cat local.properties
  ls -a
 rm -rf app/build/
dos2unix gradlew
chmod +x ./gradlew


 ./gradlew build
 if [ $? -ne 0 ]; then
     echo "Command failed!"
     exit 1
 else
     echo "Command succeeded!"
 fi

if [ "$current_branch" == "develop" ]; then
     ./gradlew assembleDebug
     if [ $? -ne 0 ]; then
         echo "Command failed!"
         exit 1
     else
         echo "Command succeeded!"
     fi
  find app/build/outputs -type f -name "*-release*" -delete
fi

if  [ "$current_branch" == "testing" ]; then
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
#  find app/build/outputs -type f -name "*-release*" -delete
fi
if [ "$current_branch" == "release" ]; then
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
# find app/build/outputs -type f -name "*-debug*" -delete
fi
if [ "$current_branch" == "main" ]; then
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
