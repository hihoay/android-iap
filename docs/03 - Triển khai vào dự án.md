## Cấu hình cho Application `class` và Activity `class`
**Lưu ý các `Activity` sử dụng trong App phải kế thừa lại lớp `HihoayActivity`** (Không bắt buộc)

**Lưu ý các `Application` sử dụng trong App phải kế thừa lại lớp `HihoayApplication`** (Bắt buộc)
### 1,  Cài đặt vào `onCreate()` trong `Application` class

```kotlin
class MyApplication : HihoayApplication() {
    override fun onCreate() {
        super.onCreate()
        IS_TESTING = BuildConfig.DEBUG
    }
}
```

- Thêm các Key nếu có:

```kotlin
// Nếu không cần thì không cần khai báo
AD_CONFIG_VERSION_DEFAULT = "default"  //Cấu hình phiên bản quảng cáo trong App, Mặc định là `default` thay đổi nếu muốn test phiên bản khác. 
// Nếu không cần thì không cần khai báo
DATA_CONFIG_VERSION_DEFAULT = "default"  //Cấu hình dữ liệu điều khiển từ xa trong App, Mặc định là `default` thay đổi nếu muốn test phiên bản khác.
```

> `BuildConfig` phải nằm trong `package` của App

> Nếu không `import` được `BuildConfig.DEBUG`, hay không tìm thấy trong `package` của app thì mình cần thêm vào `buildFeatures` trong `build.gradle` của app:

```kotlin
buildFeatures {
    buildConfig true
}
```

> Sử dụng `elog()` để có thể log trong app thay vì Log.e hay Log.d ... khi `BuildConfig.DEBUG = true`  
