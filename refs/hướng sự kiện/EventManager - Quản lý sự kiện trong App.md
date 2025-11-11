Tất nhiên rồi. Dưới đây là mã nguồn hoàn chỉnh của `EventManager` đã được cập nhật và tài liệu hướng dẫn sử dụng chi tiết.

-----

### \#\# Mã nguồn `EventManager` (Phiên bản tự động hủy)

```kotlin
// EventManager.kt
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import org.json.JSONObject

/**
 * Lớp Singleton quản lý việc gửi và nhận sự kiện trong toàn ứng dụng.
 * Nó có khả năng tự động hủy đăng ký listener khi Activity/Fragment bị hủy
 * để tránh rò rỉ bộ nhớ (memory leak).
 */
class EventManager {

    // Map lưu trữ các listener: Map<Tên sự kiện, Danh sách các listener>
    private val listeners = mutableMapOf<String, MutableList<EventCallback>>()

    /**
     * Hủy đăng ký một callback khỏi một sự kiện cụ thể.
     * Phương thức này được để private vì việc hủy đăng ký giờ đã được tự động hóa.
     */
    private fun unregister(eventName: String, callback: EventCallback) {
        // Sử dụng synchronized để đảm bảo an toàn khi truy cập từ nhiều luồng
        synchronized(listeners) {
            listeners[eventName]?.remove(callback)
        }
    }

    /**
     * Đăng ký một callback để lắng nghe sự kiện.
     * Callback sẽ tự động bị hủy khi LifecycleOwner (Activity/Fragment) bị hủy.
     *
     * @param owner The LifecycleOwner (thường là `this` trong Activity hoặc `viewLifecycleOwner` trong Fragment).
     * @param eventName Tên của sự kiện cần lắng nghe.
     * @param callback Hàm sẽ được gọi khi có sự kiện.
     */
    fun register(owner: LifecycleOwner, eventName: String, callback: EventCallback) {
        // Thêm callback vào danh sách listener
        synchronized(listeners) {
            val eventListeners = listeners.getOrPut(eventName) { mutableListOf() }
            eventListeners.add(callback)
        }

        // Tạo một Observer để lắng nghe vòng đời của `owner`
        val observer = object : DefaultLifecycleObserver {
            // Hàm này sẽ được tự động gọi khi `owner` (Activity/Fragment) bị hủy
            override fun onDestroy(owner: LifecycleOwner) {
                // Tự động gọi hàm hủy đăng ký
                unregister(eventName, callback)
                // Gỡ bỏ observer để dọn dẹp chính nó
                owner.lifecycle.removeObserver(this)
            }
        }

        // Gắn observer vào vòng đời của owner
        owner.lifecycle.addObserver(observer)
    }

    /**
     * Gửi một sự kiện đến tất cả các listener đã đăng ký.
     *
     * @param eventName Tên của sự kiện.
     * @param data Dữ liệu dưới dạng Map, sẽ được chuyển thành chuỗi JSON. Có thể là null.
     */
    fun post(eventName: String, data: Map<String, Any>?) {
        val jsonString = data?.let { JSONObject(it).toString() }
        val callbacksToNotify: List<EventCallback>

        synchronized(listeners) {
            // Tạo một bản sao của danh sách để tránh lỗi ConcurrentModificationException
            callbacksToNotify = listeners[eventName]?.toList() ?: emptyList()
        }

        // Thông báo cho tất cả các callback đã đăng ký
        callbacksToNotify.forEach { callback ->
            callback.onEvent(eventName, jsonString)
        }
    }
}

/**
 * Một functional interface định nghĩa cấu trúc của một callback sự kiện.
 */
fun interface EventCallback {
    /**
     * Được gọi khi một sự kiện được gửi đi.
     * @param eventName Tên của sự kiện.
     * @param data Dữ liệu dạng chuỗi JSON, có thể là null.
     */
    fun onEvent(eventName: String, data: String?)
}
```

-----

### \#\# Tài liệu hướng dẫn sử dụng `EventManager`

#### \#\#\# 1. Mục đích 🎯

**EventManager** là một công cụ trung gian giúp các thành phần khác nhau của ứng dụng (như `Activity`, `Fragment`, `Service`) có thể giao tiếp với nhau một cách linh hoạt mà không cần phải có tham chiếu trực tiếp. Nó đặc biệt hữu ích khi bạn muốn:

* Gửi dữ liệu từ một `Fragment` này sang một `Fragment` khác.
* Thông báo cho `Activity` đang hoạt động khi một tác vụ nền đã hoàn thành.
* Cập nhật giao diện ở nhiều nơi cùng lúc khi có một sự kiện chung xảy ra.

Tính năng nổi bật nhất là khả năng **tự động hủy đăng ký listener**, giúp loại bỏ hoàn toàn nguy cơ rò rỉ bộ nhớ (memory leak) do quên dọn dẹp.

-----

#### \#\#\# 2. Cài đặt ban đầu

Để `EventManager` có thể được truy cập từ mọi nơi, bạn cần khởi tạo nó như một đối tượng duy nhất trong lớp `Application` tùy chỉnh của mình.

**a. Tạo lớp `MyApplication`:**

```kotlin
// MyApplication.kt
import android.app.Application

class MyApplication : Application() {
    // Khởi tạo một instance duy nhất của EventManager
    val eventManager = EventManager()
}
```

**b. Khai báo trong `AndroidManifest.xml`:**

Đây là bước **bắt buộc** để Android sử dụng lớp `Application` của bạn.

```xml
<application
    android:name=".MyApplication"
    ...>
    </application>
```

-----

#### \#\#\# 3. Cách sử dụng

##### **a. Gửi một sự kiện**

Bạn có thể gửi sự kiện từ bất kỳ đâu có quyền truy cập vào `Context`.

```kotlin
// Ví dụ: Trong một Fragment hoặc Activity
fun onUpdateButtonClick() {
    // 1. Lấy instance của EventManager từ Application
    val eventManager = (requireActivity().application as MyApplication).eventManager

    // 2. Chuẩn bị dữ liệu (nếu có) dưới dạng Map
    val eventData = mapOf(
        "itemId" to 42,
        "status" to "Completed",
        "isUrgent" to false
    )

    // 3. Gửi sự kiện với một tên định danh và dữ liệu
    eventManager.post("ITEM_STATUS_UPDATED", eventData)
}
```

##### **b. Lắng nghe một sự kiện**

Để nhận sự kiện, bạn cần đăng ký một `EventCallback`.

```kotlin
// Ví dụ: Trong một Activity hoặc Fragment khác
import org.json.JSONObject

class MyFragment : Fragment() {

    private lateinit var eventManager: EventManager

    // 1. Định nghĩa một callback để xử lý sự kiện
    private val itemUpdateCallback = EventCallback { eventName, data ->
        // Luôn đảm bảo cập nhật UI trên luồng chính
        activity?.runOnUiThread {
            if (eventName == "ITEM_STATUS_UPDATED" && data != null) {
                // 2. Phân tích dữ liệu JSON nhận được
                val jsonData = JSONObject(data)
                val itemId = jsonData.getInt("itemId")
                val status = jsonData.getString("status")

                // 3. Cập nhật giao diện hoặc thực hiện logic
                Log.d("EventManager", "Item $itemId has new status: $status")
                updateUI(itemId, status)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eventManager = (requireActivity().application as MyApplication).eventManager

        // 4. Đăng ký listener.
        // - Đối với Fragment, dùng `viewLifecycleOwner`.
        // - Đối với Activity, dùng `this`.
        // Việc hủy đăng ký sẽ được thực hiện tự động!
        eventManager.register(viewLifecycleOwner, "ITEM_STATUS_UPDATED", itemUpdateCallback)
    }

    // Bạn không cần phải override onDestroy() để hủy đăng ký nữa.
}
```

-----

#### \#\#\# 4. Các lưu ý quan trọng ⚠️

* **Tên sự kiện:** Hãy sử dụng các hằng số (ví dụ: `const val EVENT_NAME = "..."`) để đặt tên cho sự kiện nhằm tránh lỗi chính tả.
* **Luồng (Threading):** Callback sẽ được thực thi trên cùng một luồng mà hàm `post` được gọi. Nếu bạn gửi sự kiện từ luồng nền và muốn cập nhật UI, hãy dùng `runOnUiThread` hoặc `view.post` như trong ví dụ.
* **Dữ liệu:** Dữ liệu được truyền dưới dạng `Map<String, Any>` và được chuyển đổi thành JSON. Các kiểu dữ liệu cơ bản (String, Int, Boolean, Double) được hỗ trợ. Đối với các đối tượng phức tạp, bạn có thể chuyển chúng thành JSON trước khi đặt vào Map.