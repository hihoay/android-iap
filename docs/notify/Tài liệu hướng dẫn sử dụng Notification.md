# 📦 NotificationHelper

`NotificationHelper` là một tiện ích dùng để:

- Tạo và enqueue thông báo một lần.
- Thiết lập công việc lặp lại định kỳ (Periodic Work) sử dụng `WorkManager`.

---

## 🔹 enqueueNotification

```kotlin
    fun enqueueNotification(applicationContext: Context, model: NotificationModel)
```

✅ Mô tả :
Gửi một thông báo một lần thông qua WorkManager bằng cách truyền vào một NotificationModel.

🧾 Tham số :

Tên Kiểu dữ liệu Mô tả
applicationContext Context Context.model NotificationModel Dữ liệu chứa thông tin thông báo.

📌 Ví dụ :
    # Chú ý: nếu app của bạn dùng những thư viện này thì hãy configurations

```kotlin
    // WorkManager - quản lý công việc chạy ngầm
    implementation(libs.androidx.work.runtime.ktx)
    
    // Gson - dùng để convert object <-> JSON
    implementation(libs.gson)
    
    // Ktor Client - để gọi API network
    implementation(libs.ktor.client.core) // core chính của ktor
    implementation(libs.ktor.client.cio)  // sử dụng engine CIO để call HTTP
    implementation(libs.ktor.client.content.negotiation)
    
    // Kotlinx Serialization - cho JSON
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.kotlinx.serialization.json)
```

# Input

```kotlin
    val dataModel = DataModel(
        title = "Tên của nút action",  // Tiêu đề hiển thị cho nút hành động trong notification
        icon = R.drawable.ic,         // ID resource của icon hiển thị kèm nút
        data = "json"                 // Dữ liệu JSON hoặc string bất kỳ được gửi kèm khi nhấn nút
    )



    data class NotificationModel(
        val id: Int = NotificationIdGenerator.nextId(),
        // ID duy nhất cho mỗi notification. Được tạo tự động nếu không truyền vào.
    
        val title: String,
        // Tiêu đề chính hiển thị của thông báo.
    
        val message: String,
        // Nội dung thông báo.
    
        val iconResBitmap: Int = android.R.drawable.ic_dialog_info,
        // ID của icon nhỏ hiển thị trong notification (ở thanh trạng thái). Dùng icon từ resource.
    
        val isOngoing: Boolean = false,
        // Nếu `true`, notification sẽ ở trạng thái đang hoạt động (không thể swipe để xóa, ví dụ: nhạc, tải xuống).
    
        val targetActivity: String? = null,
        // Tên class của Activity sẽ được mở khi người dùng tương tác với notification.
    
        val listAction: MutableList<DataModel>? = null,
        // Danh sách các hành động (nút bấm) đi kèm notification, mỗi action là một `DataModel`.
    
        val style: String = NotifyConstants.STYLE_BASIC,
        // Kiểu hiển thị của thông báo (ví dụ: cơ bản, có ảnh, inbox, v.v.).
    
        val imageResId: Int? = null,
        // ID của hình ảnh (resource) hiển thị lớn trong notification, dùng cho kiểu BigPicture.
    
        val imageString: String? = null
        // Đường dẫn đến hình ảnh (URL hoặc path trong máy) nếu không dùng imageResId.
    )

```

✅ Cách dùng :
```kotlin
    val dataModel = DataModel(
        title = "", //tên của action
        icon = R.drawable.ic, // icon action
        data = "json"  //dữ liệu đầu vào 
    )

    val modelNotification = NotificationModel(
        title = "", //tiêu đề notifi
        message = "", // nội dung
        iconResId = R.drawable.ic_launcher_foreground, // icon nhỏ
        isOngoing = false,
        targetActivity = SplashActivity::class.java.name, // activity muốn mở
        listAction = mutableListOf(dataModel),
        style = NotifyConstants.STYLE_BASIC, //STYLE_BASIC hoặc STYLE_ACTION 
        imageResId = R.drawable.meo, // ảnh to với resID
        imageString = "https://cdn.discordapp.com/attachments/1264497417877852223/1364945775402156032/image.png?ex=68102190&is=680ed010&hm=14015bc16e09ee860e5e92053ac996a0cf5b18887d4ef2031bfd33ca751ca42e&"
        // ảnh to với đường dẫn
    )

    // thông báo một lần
    NotificationHelper.enqueueNotification(applicationContext, modelNotification)



    // thông báo lặp lịch, cứ mỗi một thời gian sẽ gọi lại hàm, thường có thể call API
    // thêm internet vào manifest khi call api
    // "AndroidManifest": <uses-permission android:name="android.permission.INTERNET"/>
    
    NotificationHelper.periodicWorkRequest(
        applicationContext,
        modelNotification,
        repeatInterval = 15, // Khoảng thời gian lặp lại sau mỗi lần thực hiện xong. Ở đây là 15 phút.
        flexTimeInterval = 5, // Thời gian "linh động" để hệ thống tối ưu pin, cho phép WorkManager thực thi sớm hoặc trễ trong khoảng này (ở đây là ±5 phút).
        timeUnitRepeatInterval = TimeUnit.MINUTES, // Đơn vị thời gian đang dùng cho RepeatInterval. Ở đây là phút.
        timeUnitFlexTimeInterval = TimeUnit.MINUTES // Đơn vị thời gian đang dùng cho FlexTimeInterval. Ở đây là phút.
    )
```

    

# Output
```kotlin
    //nhận dữ liệu đầu ra 
    val jsonData = intent?.getStringExtra(NotifyConstants.JSON_DATA_NOTIFICATION)
    
    //nhận title của action để điều hướng màn hình
    val action =  intent?.getStringExtra(NotifyConstants.ACTION_APP)
    
    // nhận targetAcitvity
    val targetActivity = intent?.getStringExtra(NotifyConstants.TARGET_ACTIVITY)
    
```

# 🔐 Permission & Settings Utils

        Tập hợp các tiện ích dùng để kiểm tra và yêu cầu quyền thông báo, mở cài đặt tự khởi động (Auto Start) cho Xiaomi, và cài đặt tối ưu pin .

---

## 📌 `PermissionConstants`

```kotlin
    object PermissionConstants {
        const val NOTIFICATION_REQUEST_CODE = 96
    }
```

> Mã request dùng cho việc xin quyền thông báo (`POST_NOTIFICATIONS`).

---

## 🔔 Hàm: `requestAppPermissionNotification`

```kotlin
    fun Activity.requestAppPermissionNotification(requestCode: Int)
```

### Mô tả:

Yêu cầu quyền gửi thông báo (`POST_NOTIFICATIONS`) nếu thiết bị đang chạy Android 13 trở lên (
`TIRAMISU`).

### Tham số:

- `requestCode`: `Int` – mã định danh khi xử lý kết quả xin quyền.

### Ví dụ sử dụng:

```kotlin
    requestAppPermissionNotification(PermissionConstants.NOTIFICATION_REQUEST_CODE)
```

---

## ✅ Hàm: `checkPermissionNotification`

```kotlin
    fun Activity.checkPermissionNotification(): Boolean
```

### Mô tả:

Kiểm tra xem quyền gửi thông báo đã được cấp chưa.

### Giá trị trả về:

- `true`: nếu đã được cấp hoặc hệ điều hành thấp hơn Android 13.
- `false`: nếu chưa được cấp trên Android 13+.

### Ví dụ sử dụng:

```kotlin
    if (!checkPermissionNotification()) {
        requestAppPermissionNotification(PermissionConstants.NOTIFICATION_REQUEST_CODE)
    }
```

---

## ⚙️ Hàm: `openXiaomiAutoStartSettings`

```kotlin
    fun Activity.openXiaomiAutoStartSettings()
```

### Mô tả:

Mở màn hình quản lý khởi động ứng dụng tự động (Auto Start) trên thiết bị Xiaomi.

> 💡 Hữu ích với những app cần chạy service ngầm hoặc gửi thông báo khi thiết bị khởi động lại.

### Ví dụ sử dụng:

```kotlin
openXiaomiAutoStartSettings()
```

---

## 🔋 Hàm: `openBatteryOptimizationSettings`

```kotlin
    fun Activity.openBatteryOptimizationSettings()
```

### Mô tả:

Mở phần cài đặt bỏ qua tối ưu hóa pin để đảm bảo ứng dụng không bị giới hạn hoạt động ở chế độ nền.

### Ví dụ sử dụng:

```kotlin
    openBatteryOptimizationSettings()
```

---

## 📎 Gợi ý sử dụng tổng hợp:

```kotlin
    if (!checkPermissionNotification()) {
        requestAppPermissionNotification(PermissionConstants.NOTIFICATION_REQUEST_CODE)
    }
    
    if (Build.MANUFACTURER.equals("xiaomi", ignoreCase = true)) {
        openXiaomiAutoStartSettings()
    }
    
    openBatteryOptimizationSettings()
```

# Dùng các hằng số ở đây
```kotlin
    object NotifyConstants {
        const val STYLE_BASIC = "basic"
        const val STYLE_ACTION = "action"
    
        const val TARGET_ACTIVITY = "targetActivity"
        const val JSON_DATA_NOTIFICATION = "json_data_notification"
        const val CANCEL_NOTIFICATION = "cancel"
    
        const val ACTION_OPEN = "open"
        const val ACTION_FEATURE = "action_feature_"
    
    }
```

---




