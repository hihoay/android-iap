# Android Firebase Service

- [Version Tag lấy ở đây](https://github.com/hihoay/android-iap/tags)

- Dịch vụ tích hợp tính năng thanh toán trong App qua Google Billing Android

## Phát triển dự án

Khi push lên các nhánh `develop`, `testing`, `release`, `main` **Jenkins** sẽ tự động chạy và đóng gói thành **Dependences** với tag tương ứng.

## Tích hợp vào dự án:

- Đối với Groovy DSL:

```groovy
// Thêm vào tệp build.gradle

dependencies {
//    ...
    implementation 'com.taymay:android-iap:<version_tag>'
}


// Thêm vào tệp settings.gradle

repositories {
//    ...
    maven {
        url = "https://maven.pkg.github.com/hihoay/repository"
        credentials {
            username = "develop"
            password = ""
        }
    }
}

```

- Đối với Kotlin DSL:

Dưới đây là cách **chuyển đoạn cấu hình Groovy sang Kotlin DSL** (`build.gradle.kts` và `settings.gradle.kts`):

### 🧩 `build.gradle.kts`

```kotlin
dependencies {
    // ...
    implementation("com.taymay:android-iap:<version_tag>")
}
```

### ⚙️ `settings.gradle.kts`

```kotlin
pluginManagement {
    repositories {
        // ...
        maven {
            url = uri("https://maven.pkg.github.com/hihoay/repository")
            credentials {
                username = "develop"
                password = ""
            }
        }
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://maven.pkg.github.com/hihoay/repository")
            credentials {
                username = "develop"
                password = ""
            }
        }
    }
}
```

---

### 💡 Ghi chú:

- Kotlin DSL yêu cầu dùng **`uri()`** thay cho chuỗi trực tiếp `"..."` trong `url`.

- Các repository thường nên được thêm trong `dependencyResolutionManagement` (Gradle 7+).

- Nếu bạn chỉ cần đơn giản (không dùng pluginManagement), bạn có thể rút gọn:

```kotlin
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/hihoay/repository")
        credentials {
            username = "develop"
            password = ""
        }
    }
}
```


## Triển khai sử dụng trong dự án:


- Khởi tạo đầu tiên để lấy dữ liệu, nên triển khai trong phương thức `onCreate` của `Application`

```kotlin
val product = "remove_ad"
setupIAP(  
    context = this,  
    isTesting = true,  
    products = product,  
    onPricesUpdated = {  
    
    },  
    onProductPurchased = {
    
    },  
    onProductRestored = {
    
    },  
    onPurchaseFailed = {
    
    })
```

- Hiển thị Dialog hỏi Thanh toán xóa quảng cáo trong App:

```kotlin
val product = "remove_ad"
DialogRemoveAd(this).showDialogRemoveAd(product, MainActivity::class.java)
```

- Kiểm tra xem User đã thanh toán xóa Quảng cáo trong App hay chưa:

```kotlin
isPayRemoveAd()
```

