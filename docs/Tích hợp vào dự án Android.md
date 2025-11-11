## Tích hợp và cài đặt cơ bản vào dự án Android thông qua Android Studio

1. Kết nối với Firebase 
    - thêm file `google-services.json`
    - vào **Tool** -> **Firebase** add `Analytics`, `Cloud Messaging`, `In-App Messaging`

## Tích hợp và cài đặt cơ bản vào dự án Android thông qua Android Studio


1. Thêm vô `build.gradle`:

- Project sẽ build ra 3 Module riêng để sử dụng
- Kotlin DSL:
```kotlin
dependencies {
   implementation("com.taymay:notify-android:x.0.0.0") // dành cho notify local
   implementation("com.taymay:fcm-android:x.0.0.0") // dành cho Firebase  Cloud Message
   implementation("com.taymay:iap-android:x.0.0.0") // Dành cho thanh toán trong ứng dụng
}
```

- Groovy DSL:
```groovy
    implementation "com.taymay:notify-android:x.0.0.0"
    implementation "com.taymay:fcm-android:x.0.0.0"
    implementation "com.taymay:iap-android:x.0.0.0"

```
- Lấy phiển bản mới nhất ở đây [Latest Tag](https://github.com/hihoay/notify-android/tags)
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

- Rebuild lại Project

