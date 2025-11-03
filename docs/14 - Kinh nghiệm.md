## Giải pháp sửa luồng Exit rồi vào lại app mà không hiện Splash
### Discussed in https://github.com/hihoay/com.hihoay.app.service/discussions/7
<div type='discussions-op-text'>
<sup>Originally posted by **hihoay** August 17, 2024</sup>

  
1, Màn splash mọi người kế thừa `HihoaySplashActivity`
2, Sau khi hiển thị quản cáo Splash xong , trong phương thức chuyển từ màn Splash sang màn Home thì mọi người thêm câu lệnh ` hihoaySetIsSplash(false)` để quảng có ReturnApp có thể hiển thị.
> Vậy là ok rồi.</div>


---

## Thêm vào build.gradle thì build .aab mới có tệp String

```groovy
defaultConfig {
    ....
    resConfigs("af", "sq", "am", "ar", "hy", "az", "eu", "be", "bn", "bs", "bg", "ca", "ceb", "ny", "co", "hr", "cs", "da", "nl", "en", "eo", "et", "tl", "fr", "fy", "gl", "ka", "de", "el", "gu", "ht", "ha", "haw", "iw", "hi", "hmn", "hu", "is", "ig", "id", "ga", "it", "ja", "jw", "kn", "kk", "km", "rw", "ko", "ku", "ky", "lo", "la", "lv", "lt", "lb", "mk", "mg", "ms", "ml", "mt", "mi", "mr", "mn", "my", "ne", "no", "or", "ps", "fa", "pl", "pt", "pa", "ro", "ru", "sm", "gd", "sr", "st", "sn", "sd", "si", "sk", "sl", "so", "es", "su", "sw", "sv", "tg", "ta", "tt", "te", "th", "tr", "tk", "uk", "ur", "ug", "uz", "vi", "cy", "xh", "yi", "yo", "zu", "zh")

}
```

---
## Các kiểm tra về dữ liệu của Ad Config và Data Config.

### Discussed in https://github.com/hihoay/com.hihoay.app.service/discussions/2

<div type='discussions-op-text'>

<sup>Originally posted by **hihoay** August  1, 2024</sup>
- Thường sau khi cập nhật thì 2p sau thì Server mới thay đổi, để kiếm tra thay vì chạy app thì mọi người có vào link theo mẫu dưới đây để check:


- Ad Config: https://bot.taymay.io/ad_version/app_package.json
- Data Config: https://bot.taymay.io/data_version/app_package.json

Thay thế `app_package` cần check.

Ví dụ:

- Ad Config: https://bot.taymay.io/ad_version/com.taymay.fingerprint.animation.json
- Data Config: https://bot.taymay.io/data_version/com.taymay.fingerprint.animation.json</div>
---
# Tự động dịch string.xml

- Ở nhánh `main`
- Truy cập https://github.dev/hihoay/com.hihoay.string
- Sửa nội dung tệp `strings.xml`
- Commit & push là sẽ tự động dịch ra 20 ngôn ngữ mặc định, và tệp đầu ra sẽ gửi đến Discord chanel `#chim-lợn` có tên là `values.zip` và nội dung Commit sẽ là tiêu đề.
- Tệp `values.zip` chưa strings.xml là tệp đã dịch từ Tiếng Anh của App, tải về và giải nén vào thư mục `/app/res/` của dự án là oke!

---

## Multidex Application

- Tham khảo: https://developer.android.com/build/multidex#groovy

```grovy
  implementation 'androidx.multidex:multidex:2.0.1'
```

Modify the module-level `build.gradle` file to enable multidex and add the multidex library as a dependency, as shown here:

```groovy
android {
    defaultConfig {
        ...
        minSdkVersion 15 
        targetSdkVersion 33
        multiDexEnabled true
    }
    ...
}

dependencies {
    implementation "androidx.multidex:multidex:2.0.1"
}
```


### Xóa AppCompatDelegate fix lỗi này
```java
2024-11-04 14:52:22.774  8796-8796  ActivityImpl            com.taymay.note                      E  get life cycle exception
                                                                                                    java.lang.ClassCastException: android.os.BinderProxy cannot be cast to android.app.servertransaction.ClientTransaction
                                                                                                    	at android.app.ActivityImpl.checkAccessControl(ActivityImpl.java:68)
                                                                                                    	at android.app.ActivityImpl.onResume(ActivityImpl.java:163)
                                                                                                    	at android.app.Activity.onResume(Activity.java:2203)
                                                                                                    	at androidx.fragment.app.FragmentActivity.onResume(FragmentActivity.java:310)
                                                                                                    	at android.app.Instrumentation.callActivityOnResume(Instrumentation.java:1615)
                                                                                                    	at android.app.Activity.performResume(Activity.java:9057)
                                                                                                    	at android.app.ActivityThread.performResumeActivity(ActivityThread.java:5194)
                                                                                                    	at android.app.ActivityThread.handleResumeActivity(ActivityThread.java:5237)
                                                                                                    	at android.app.servertransaction.ResumeActivityItem.execute(ResumeActivityItem.java:57)
                                                                                                    	at android.app.servertransaction.ActivityTransactionItem.execute(ActivityTransactionItem.java:45)
                                                                                                    	at android.app.servertransaction.TransactionExecutor.executeLifecycleState(TransactionExecutor.java:190)
                                                                                                    	at android.app.servertransaction.TransactionExecutor.execute(TransactionExecutor.java:101)
                                                                                                    	at android.app.ClientTransactionHandler.executeTransaction(ClientTransactionHandler.java:68)
                                                                                                    	at android.app.ActivityThread.handleRelaunchActivityLocally(ActivityThread.java:6170)
                                                                                                    	at android.app.ActivityThread$H.handleMessage(ActivityThread.java:2591)
                                                                                                    	at android.os.Handler.dispatchMessage(Handler.java:106)
                                                                                                    	at android.os.Looper.loopOnce(Looper.java:224)
                                                                                                    	at android.os.Looper.loop(Looper.java:318)
                                                                                                    	at android.app.ActivityThread.main(ActivityThread.java:8727)
                                                                                                    	at java.lang.reflect.Method.invoke(Native Method)
                                                                                                    	at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:561)
                                                                                                    	at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:1013)
```


---

## Tự động build ra APK AAB debug, release cho Android Studio
### Discussed in https://github.com/hihoay/com.hihoay.app.service/discussions/5

<div type='discussions-op-text'>

<sup>Originally posted by **hihoay** August  7, 2024</sup>
Tự động hóa và tiết kiệm thời gian mọi người build ra file apk test hay file update rồi đẩy lên cloud rồi gửi link cho nhau thì mình làm cái Action khi mọi người push code lên các nhánh khác nhau sẽ có hình thức Build khác nhau:


> Quá trình build có thể mất 10- 15p nên mọi người tự động đẩy code lên rồi chuyển qua task khác,  kệ nó chạy, xong thì nó sẽ báo kết quả trên Discord nhé.

## Mô tả:

-  `push`  code lên nhánh `develop` thì nó sẽ tự động build ra `...-debug.apk` và tự động tạo ra link tải và mã QR để tải và bắn thông tin lên discord.

![image](https://github.com/user-attachments/assets/121145f7-db9e-47e5-8af6-573ffef49807)

-  `push`  code lên nhánh `release` thì nó sẽ tự động build ra `...-release.apk` `...-release.aab`  và tự động tạo ra link tải và mã QR để tải và bắn thông tin lên discord.

![image](https://github.com/user-attachments/assets/8ed37766-9108-40dd-b1bd-f1e82bc2e74e)


## Cài đặt

### 1, Thực hiện lệnh ở thư mục gốc của dự án.

- Linux, MacOS:

```shell
    curl -sL http://file-service.hihoay.io/static/android-action/init.sh | fish
    # hoặc
    curl -sL http://file-service.hihoay.io/static/android-action/init.sh | sh
    # hoặc
    curl -sL http://file-service.hihoay.io/static/android-action/init.sh | bash

```

- Window:

Tải tệp này về http://file-service.hihoay.io/static/android-action/.github.zip
rồi giải nén vào thư mục `.github` của project


- Cấu trúc thư mục sẽ như sau là đúng:
```css
.
├── .github
│   └── workflows
│       └── build-debug.yml
│       └── build-release.yml
│       └── build-product.yml
│   └── requirements.txt
├── app
│   ├── src
│   │   ├── main
│   │   ├── test
│   │   └── androidTest
│   └── build.gradle
├── gradle
├── build.gradle
├── settings.gradle
└── ...

```

> Lệnh sẽ tự động tạo các tệp tin cần thiết ở thư mục .github nằm ở thư mục gốc của dự án.

### 2, Chạy test Build:

- Chạy build test:

Sau khi cấu hình cơ bản thì mình cần chạy build test ở local trước rồi mới đẩy code lên các nhánh kia, để test ở local thì ở thư mục gốc của dự án chạy lện sau ở Terminal:

```shell

# Window
.\gradlew build


# Linux
chmod +x ./gradlew
./gradlew build

```

> Kết quả `BUILD FAILED in ...` mọi người xem lỗi ở đâu và sửa lại. (Nó có hiển thị lỗi ở tệp nào dòng nào).

> Kết quả `BUILD SUCCESSFUL in ...` là ok có thể build `Debug` tự động được.

### 3, Cài cấu hình app `build.gradle` để có thể build Release tự động được:

Thông thường `storeFile` của app sẽ ở thư mục gốc của dự án với tên file là **app package** hoặc **app package.jks**

Ví dụ: _com.taymay.bomb.prank.jks_, hoặc có dự án tên không có .jks sẽ là _com.taymay.bomb.prank_

- Cấu hình trong app:`build.gradle`

```groovy
android {
    signingConfigs {
        release {
            storeFile file('../com.taymay.bomb.prank.jks') // thay thế tên storeFile vào đây
            storePassword 'com.taymay.bomb.prank' // mặc định sẽ là package
            keyAlias 'com.taymay.bomb.prank' // mặc định sẽ là package
            keyPassword 'com.taymay.bomb.prank' // mặc định sẽ là package
        }
    }
    ...

    buildTypes {
        release {
            signingConfig signingConfigs.release // thêm dòng này
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    ...
}


```

> Ok vậy là xong

</div>

---

## Quy trình làm việc với Git
### Discussed in https://github.com/hihoay/com.hihoay.app.service/discussions/6

<div type='discussions-op-text'>

<sup>Originally posted by **hihoay** August  7, 2024</sup>
Mọi người lưu ý làm việc với git thì ae thỏa mái tạo các nhánh feature/,,, nhưng mình sẽ không nên push nhiều nhánh feature lên git nhé.

Thường sau khi phát triển xong 1 tính năng nào đó mà ok thì mình sẽ tạo pull request để merge nó vào nhánh develop rồi xóa nó đi gọi là xong tính năng này.

Sau này có nhiều người hay phát triển tính năng mới thì mình sẽ tạo nhánh feature mới từ nhánh develop ra và cứ lặp lại như thế.

![image](https://github.com/user-attachments/assets/9e201a31-e17d-4a98-b91f-c30f6bea4b46)


## Đối với các vấn đề (Issues):

- Có các vấn đề hay cần cập nhật gì đó vè dự án mọi người sẽ tạo `Issues` và mình sẽ bàn luận, trao đổi trên `Issues` đó.
- Từ Issuse mọi người tạo nhánh riêng để giải quyết vấn đề như là bug hoặc phát triển thêm,
- Hoàn thành `Issues` thì mọi người tạo `Pull Request` vào nhánh `develop`, sau khi merge thành công thì có thể xóa nhánh đó và đóng `Issues`.
- Tiếp tục lặp lại như vậy.

## Với việc tài liệu cho dự án, nghiên cứu, công nghệ,,,, mọi người có thể tạo trên Discussions.


## Wiki mọi người sẽ làm tài liệu triển khai và sử dụng cho dự án.


</div>
