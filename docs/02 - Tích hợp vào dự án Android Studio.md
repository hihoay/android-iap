
## Tích hợp và cài đặt cơ bản vào dự án Android thông qua Android Studio


1. Thêm vô `build.gradle`:
- Kotlin DSL:
```kotlin
dependencies {
	implementation("com.hihoay:adx.service.android:x.0.0.0")
	implementation("androidx.multidex:multidex:2.0.1")
}
```

- Groovy DSL:
```groovy
defaultConfig {
	...
        multiDexEnabled true
        resConfigs("af", "sq", "am", "ar", "hy", "az", "eu", "be", "bn", "bs", "bg", "ca", "ceb", "ny", "co", "hr", "cs", "da", "nl", "en", "eo", "et", "tl", "fr", "fy", "gl", "ka", "de", "el", "gu", "ht", "ha", "haw", "iw", "hi", "hmn", "hu", "is", "ig", "id", "ga", "it", "ja", "jw", "kn", "kk", "km", "rw", "ko", "ku", "ky", "lo", "la", "lv", "lt", "lb", "mk", "mg", "ms", "ml", "mt", "mi", "mr", "mn", "my", "ne", "no", "or", "ps", "fa", "pl", "pt", "pa", "ro", "ru", "sm", "gd", "sr", "st", "sn", "sd", "si", "sk", "sl", "so", "es", "su", "sw", "sv", "tg", "ta", "tt", "te", "th", "tr", "tk", "uk", "ur", "ug", "uz", "vi", "cy", "xh", "yi", "yo", "zu", "zh")
        setProperty("archivesBaseName", applicationId + "-v" + versionCode + "(" + versionName + ")")
	...
}
dependencies {
	...
	implementation "com.hihoay:adx.service.android:x.0.0.0"
	implementation "androidx.multidex:multidex:2.0.1"
}
```
- Lấy phiển bản mới nhất ở đây [Latest Tag](https://github.com/hihoay/adx-service-android/tags)
2. Thêm vô `settings.gradle`:

- Kotlin DSL:
```Kotlin
repositories {
	google()
	mavenCentral()
	gradlePluginPortal()
	maven (url = "https://jitpack.io" )
	maven {
		url = uri("https://maven.pkg.github.com/hihoay/repository")
		credentials {
			username = "develop"
			password = "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"
		}
	}
}
```

- Groovy DSL:

```groovy
repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
	maven { url = "https://jitpack.io" }
    maven {
        url = uri("https://maven.pkg.github.com/hihoay/repository")
        credentials {
            username = "develop"
            password = "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"
        }
    }
}

```


3. Thêm các tệp cấu hình mặc định vào thư mục `assets`:

> `ads.json` là tệp cấu hình mặc định các vị trí quảng cáo.

- Lấy nội dung tệp `ads.json` bằng cách kiểm tra `Logcat` trên `Android Studio` sau khi chạy ứng
  dụng sẽ in ra đường dẫn chứa nội dung của tệp `copy` nội dung từ đường dẫn và `dán` vào
  tệp `ads.json`, đường dẫn định dạng sau:

```json
ad_version   https://bot.taymay.io/ad_version/<app_package_name>.json
```

> `data.json` là tập cấu hình mặc định các dữ liệu sử dụng.

- Lấy nội dung tệp `data.json` bằng cách kiểm tra `Logcat` trên `Android Studio` sau khi chạy ứng
  dụng sẽ in ra đường dẫn chứa nội dung của tệp `copy` nội dung từ đường dẫn và `dán` vào
  tệp `data.json`, đường dẫn có định dạng sau:

```json
data_version   https://bot.taymay.io/data_version/<app_package_name>.json
```

- Thêm vào `defaultConfig` để tự động tên của phiên bản khi build ra tệp .apk hoặc .aad trong tệp `build.gradle (:app)`:

```groovy
defaultConfig {
    ...
    setProperty("archivesBaseName", applicationId + "-v" + versionCode + "(" + versionName + ")")
}
```

- Rebuild lại Project


---

## Cấu hình với Firebase và Admob

**(Phần này thì App đã được cài rồi nên mọi người có thể bỏ qua)**

- Thêm vào Manifest thông tin App ID của Admob nếu chưa có (không sẽ bị crash, dưới đây là ID test khi nào có ID thật thì thay thế) (meta-data được thêm ở vị trí trong thẻ `application`)

```xml

    <!-- Sử dụng ID này để test nếu chưa có: ca-app-pub-3940256099942544~3347511713 -->

<meta-data android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="<id của app trên Admob>" />
```

- Thêm vào trong thẻ `application` trong `manifest`

```xml
  <application
    ...
      tools:replace="android:theme">
  </application>
```

5. Gắn Firebase vào App nếu chưa gắn:

- Thêm filre `google-services.json` vào thư mục `app` của project.

- Trên Android Studio vào
  menu `Tools` > `Firebase` > `Analytics` > `Get started with Google Analytics [Kotlin]` > chọn
  module `app`

- Trên Android Studio vào
  menu `Tools` > `Firebase` > `Crashlytics` > `Get started with Firebase Crashlytics [Kotlin]` >
  chọn module `app`

6. Cấu hình `Remote Configs`:

- Key là `ad_version` giá trị string là `default`
- Key là `data_version` giá trị string là `default`

7. Rebuild lại Project
