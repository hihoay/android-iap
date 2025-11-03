Được, bạn hoàn toàn có thể xây dựng một hệ thống như vậy bằng cách sử dụng các thành phần cơ bản của Kotlin và Android mà không cần thư viện bên ngoài. Hệ thống này sẽ sử dụng một lớp `Application` tùy chỉnh để có thể truy cập ở mọi nơi, và dùng `Map` kết hợp `JSON` để truyền dữ liệu.

Tuy nhiên, tôi cần đưa ra một **lời cảnh báo quan trọng** trước khi chúng ta bắt đầu:

> 🚨 **Cảnh báo:** Phương pháp này **không được khuyến khích** cho các dự án thực tế vì những lý do sau:
>
>   * **Không nhận biết vòng đời (Not Lifecycle-Aware):** Bạn phải tự quản lý việc đăng ký và hủy đăng ký (`register`/`unregister`) một cách thủ công. Nếu quên hủy đăng ký trong `onDestroy` hoặc `onStop`, bạn sẽ gây ra **rò rỉ bộ nhớ (memory leak)** nghiêm trọng.
>   * **Không an toàn về luồng (Not Thread-Safe):** Bạn phải tự xử lý việc đồng bộ hóa nếu các sự kiện được gửi từ nhiều luồng khác nhau.
>   * **Phức tạp và dễ lỗi:** So với việc sử dụng `SharedFlow` hay `LiveData`, cách này phức tạp hơn và dễ phát sinh lỗi hơn rất nhiều.

Dù vậy, để phục vụ mục đích học hỏi và hiểu rõ cơ chế hoạt động bên trong, dưới đây là cách xây dựng nó.

-----

### \#\# Cách hoạt động

1.  **EventCallback:** Một interface đơn giản định nghĩa một hàm callback sẽ được gọi khi có sự kiện.
2.  **EventManager:** Một lớp Singleton sẽ quản lý tất cả các "listeners". Nó sử dụng một `Map` để lưu trữ, với `key` là tên sự kiện (String) và `value` là một danh sách các `EventCallback`.
3.  **MyApplication:** Lớp `Application` tùy chỉnh của bạn, dùng để khởi tạo và giữ một thực thể duy nhất của `EventManager`.

-----

### \#\# Xây dựng từng bước

#### Bước 1: Tạo Callback Interface

Tạo một interface đơn giản. Sử dụng `fun interface` của Kotlin cho phép bạn triển khai nó bằng một lambda gọn gàng.

```kotlin
// EventCallback.kt
fun interface EventCallback {
    /**
     * Được gọi khi một sự kiện được gửi đi.
     * @param eventName Tên của sự kiện.
     * @param data Dữ liệu dạng chuỗi JSON, có thể là null.
     */
    fun onEvent(eventName: String, data: String?)
}
```

#### Bước 2: Tạo Lớp Quản lý Sự kiện (EventManager)

Đây là trái tim của hệ thống.

```kotlin
// EventManager.kt
import org.json.JSONObject

class EventManager {
    // Map<Tên sự kiện, Danh sách các listener>
    private val listeners = mutableMapOf<String, MutableList<EventCallback>>()

    /**
     * Đăng ký một callback để lắng nghe một sự kiện.
     */
    fun register(eventName: String, callback: EventCallback) {
        synchronized(listeners) {
            val eventListeners = listeners.getOrPut(eventName) { mutableListOf() }
            eventListeners.add(callback)
        }
    }

    /**
     * Hủy đăng ký một callback. Rất quan trọng để tránh memory leak.
     */
    fun unregister(eventName: String, callback: EventCallback) {
        synchronized(listeners) {
            listeners[eventName]?.remove(callback)
        }
    }

    /**
     * Gửi một sự kiện đến tất cả các listener đã đăng ký.
     * @param data Dữ liệu dưới dạng Map, sẽ được chuyển thành JSON.
     */
    fun post(eventName: String, data: Map<String, Any>?) {
        val jsonString = data?.let { JSONObject(it).toString() }
        val callbacksToNotify: List<EventCallback>

        synchronized(listeners) {
            // Tạo một bản sao của danh sách để tránh ConcurrentModificationException
            // nếu một listener tự hủy đăng ký trong lúc đang duyệt
            callbacksToNotify = listeners[eventName]?.toList() ?: emptyList()
        }

        callbacksToNotify.forEach { callback ->
            // Có thể chạy trên luồng khác nếu cần
            callback.onEvent(eventName, jsonString)
        }
    }
}
```

#### Bước 3: Tích hợp vào Lớp Application

1.  Tạo lớp `MyApplication` và khởi tạo `EventManager` trong đó.

<!-- end list -->

```kotlin
// MyApplication.kt
import android.app.Application

class MyApplication : Application() {
    val eventManager = EventManager()

    override fun onCreate() {
        super.onCreate()
        // Có thể thực hiện các khởi tạo khác ở đây
    }
}
```

2.  Khai báo lớp `Application` này trong tệp `AndroidManifest.xml`. **Đây là bước bắt buộc.**

<!-- end list -->

```xml
<application
    android:name=".MyApplication"  android:allowBackup="true"
    android:icon="@mipmap/ic_launcher"
    android:label="@string/app_name"
    android:roundIcon="@mipmap/ic_launcher_round"
    android:supportsRtl="true"
    android:theme="@style/Theme.MyApp">
    ...
</application>
```

-----

### \#\# Cách sử dụng

Bây giờ bạn có thể gửi và nhận sự kiện từ bất kỳ đâu có `Context` (Activity, Fragment, Service...).

#### Gửi sự kiện (Ví dụ từ Activity A)

```kotlin
// Trong ActivityA.kt
class ActivityA : AppCompatActivity() {
    
    fun sendUpdateEvent() {
        // Lấy instance của EventManager từ Application context
        val eventManager = (application as MyApplication).eventManager

        // Tạo dữ liệu dưới dạng Map
        val eventData = mapOf(
            "userId" to 123,
            "message" to "Profile has been updated!"
        )

        // Gửi sự kiện với tên "USER_PROFILE_UPDATE"
        eventManager.post("USER_PROFILE_UPDATE", eventData)
    }
}
```

#### Lắng nghe sự kiện (Ví dụ trong Activity B)

```kotlin
// Trong ActivityB.kt
import org.json.JSONObject

class ActivityB : AppCompatActivity() {

    private lateinit var eventManager: EventManager
    
    // Tạo một callback duy nhất cho Activity này
    private val profileUpdateCallback = EventCallback { eventName, data ->
        // Đảm bảo chạy trên luồng UI nếu cần cập nhật giao diện
        runOnUiThread {
            if (eventName == "USER_PROFILE_UPDATE" && data != null) {
                val jsonData = JSONObject(data)
                val userId = jsonData.getInt("userId")
                val message = jsonData.getString("message")
                
                Log.d("ActivityB", "Received event: $message for user $userId")
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eventManager = (application as MyApplication).eventManager
        
        // Đăng ký lắng nghe sự kiện
        eventManager.register("USER_PROFILE_UPDATE", profileUpdateCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        // 🚨 BẮT BUỘC PHẢI HỦY ĐĂNG KÝ ĐỂ TRÁNH MEMORY LEAK
        eventManager.unregister("USER_PROFILE_UPDATE", profileUpdateCallback)
    }
}
```