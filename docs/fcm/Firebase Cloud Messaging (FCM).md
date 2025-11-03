## Firebase Cloud Messaging (FCM): Cầu Nối Giao Tiếp Hiệu Quả Với Người Dùng Android

**Firebase Cloud Messaging (FCM)**, trước đây được biết đến với tên gọi Google Cloud Messaging (GCM), là một dịch vụ nhắn tin đa nền tảng miễn phí và mạnh mẽ từ Google. Nó cho phép các nhà phát triển gửi thông báo đẩy (push notification) và các gói dữ liệu nhỏ từ máy chủ của họ đến các ứng dụng của người dùng một cách nhanh chóng, đáng tin cậy và hiệu quả về mặt năng lượng.

Đối với ứng dụng Android, FCM là một công cụ không thể thiếu để duy trì sự kết nối và tương tác với người dùng, ngay cả khi họ không mở ứng dụng.

-----

### Giới thiệu về Firebase Cloud Messaging

Về cơ bản, FCM hoạt động như một cầu nối trung gian. Máy chủ ứng dụng của bạn (app server) sẽ gửi một yêu cầu chứa nội dung tin nhắn đến máy chủ FCM của Google. Sau đó, FCM chịu trách nhiệm chuyển tiếp tin nhắn này đến các thiết bị cụ thể đã cài đặt ứng dụng của bạn. Mỗi ứng dụng trên mỗi thiết bị sẽ có một mã định danh duy nhất (FCM registration token) để máy chủ có thể nhắm mục tiêu chính xác.

FCM hỗ trợ hai loại tin nhắn chính:

1.  **Tin nhắn thông báo (Notification Messages):** Đây là loại tin nhắn phổ biến nhất, thường được gọi là "thông báo đẩy". FCM SDK trên thiết bị sẽ tự động xử lý và hiển thị những tin nhắn này trên thanh thông báo của hệ thống khi ứng dụng đang ở chế độ nền. Nhà phát triển có thể dễ dàng tạo và gửi chúng thông qua Firebase Console mà không cần viết mã phức tạp.
2.  **Tin nhắn dữ liệu (Data Messages):** Loại tin nhắn này chứa các cặp key-value tùy chỉnh do bạn định nghĩa. Ứng dụng client sẽ chịu trách nhiệm hoàn toàn về việc xử lý các tin nhắn này. Chúng được sử dụng khi bạn muốn gửi dữ liệu thầm lặng để ứng dụng tự xử lý logic, chẳng hạn như đồng bộ hóa dữ liệu, cập nhật giao diện người dùng, hoặc kích hoạt một tác vụ nền.

-----

### Ứng dụng của Firebase Cloud Messaging trên Android App

Tích hợp FCM vào ứng dụng Android mở ra vô số khả năng để cải thiện trải nghiệm người dùng và đạt được các mục tiêu kinh doanh. 📱

#### 1\. Tăng Tương Tác và Giữ Chân Người Dùng

Đây là ứng dụng phổ biến và mạnh mẽ nhất của FCM.

* **Thông báo tin tức, bài viết mới:** Các ứng dụng báo chí, blog có thể gửi thông báo ngay khi có nội dung mới được xuất bản để thu hút người dùng quay lại.
* **Ưu đãi, khuyến mãi đặc biệt:** Các ứng dụng thương mại điện tử có thể gửi thông báo về các đợt giảm giá, flash sale hoặc các mã coupon độc quyền để thúc đẩy doanh số.
* **Nhắc nhở giỏ hàng:** Gửi thông báo nhắc nhở người dùng về các sản phẩm họ đã bỏ quên trong giỏ hàng.

#### 2\. Đồng Bộ Hóa Dữ Liệu Nền

Sử dụng **tin nhắn dữ liệu**, FCM có thể "đánh thức" ứng dụng của bạn một cách thầm lặng để thực hiện các tác vụ quan trọng mà không làm phiền người dùng.

* **Cập nhật dữ liệu ứng dụng:** Tự động đồng bộ hóa email mới, tin nhắn chat, hoặc nội dung mới trong feed mà không cần người dùng phải mở ứng dụng và làm mới thủ công.
* **Làm mới bộ đệm (cache):** Kích hoạt ứng dụng để tải trước dữ liệu mới, giúp lần mở ứng dụng tiếp theo của người dùng trở nên nhanh chóng và mượt mà hơn.

#### 3\. Gửi Thông Báo Theo Ngữ Cảnh và Cá Nhân Hóa

Bằng cách kết hợp FCM với các dịch vụ Firebase khác như Analytics và Remote Config, bạn có thể gửi các thông báo được cá nhân hóa cao.

* **Thông báo giao dịch:** Gửi xác nhận đơn hàng, thông tin vận chuyển, hoặc hóa đơn ngay lập tức.
* **Nhắc nhở sự kiện:** Nhắc nhở người dùng về một sự kiện họ đã đăng ký hoặc một cuộc hẹn sắp tới.
* **Thông báo dựa trên vị trí:** Gửi ưu đãi từ một cửa hàng khi người dùng đang ở gần đó.

#### 4\. Kích Hoạt Các Hành Động Xã Hội

* **Tin nhắn mới, lượt thích, bình luận:** Trong các ứng dụng mạng xã hội, FCM thông báo cho người dùng khi có người tương tác với bài viết của họ hoặc gửi tin nhắn trực tiếp.
* **Lời mời kết bạn hoặc tham gia nhóm:** Thông báo khi có lời mời mới, khuyến khích người dùng mở rộng mạng lưới kết nối.

### Tích Hợp FCM vào Ứng Dụng Android

Quy trình tích hợp FCM vào dự án Android bao gồm các bước chính sau:

1.  **Thêm Firebase vào dự án Android** của bạn thông qua Firebase Console.
2.  **Thêm thư viện FCM** vào tệp `build.gradle` của ứng dụng:
    ```groovy
    // Thêm BoM (Bill of Materials) để quản lý phiên bản
    implementation(platform("com.google.firebase:firebase-bom:33.1.1"))

    // Thêm dependency cho Cloud Messaging
    implementation("com.google.firebase:firebase-messaging")
    ```
3.  **Yêu cầu quyền gửi thông báo** (Đối với Android 13 trở lên): Bạn cần khai báo quyền `POST_NOTIFICATIONS` trong `AndroidManifest.xml` và yêu cầu người dùng cấp quyền này khi chạy ứng dụng.
4.  **Tạo một dịch vụ (Service) kế thừa từ `FirebaseMessagingService`:** Đây là nơi bạn sẽ xử lý việc nhận tin nhắn (cả tin nhắn dữ liệu và tin nhắn thông báo khi ứng dụng ở chế độ foreground) và xử lý việc làm mới token.
5.  **Khai báo Service trong tệp `AndroidManifest.xml`:**
    ```xml
    <service
        android:name=".MyFirebaseMessagingService"
        android:exported="false">
        <intent-filter>
            <action android:name="com.google.firebase.MESSAGING_EVENT" />
        </intent-filter>
    </service>
    ```

Sau khi hoàn tất, bạn có thể bắt đầu gửi thông báo thử nghiệm từ **Firebase Console** hoặc xây dựng logic phía máy chủ để gửi thông báo thông qua API của FCM.




Khi bạn gửi **mixed message** (có cả `notification` và `data` payload) thì hành vi của FCM như sau:

1. **App ở foreground**
    
    - Hệ thống **không tự hiện** notification tray.
        
    - SDK gọi luôn `onMessageReceived(RemoteMessage)` (Android) hoặc callback tương tự (iOS), bạn sẽ nhận được cả phần `notification` và `data` để xử lý ngay lập tức trong code. ([Firebase](https://firebase.google.com/docs/cloud-messaging/concept-options?utm_source=chatgpt.com "About FCM messages | Firebase Cloud Messaging - Google"))
        
2. **App ở background hoặc đã kill**
    
    - **Notification** (phần UI) vẫn được **hệ điều hành tự động hiển thị** trên thanh thông báo (Android) hoặc màn hình khóa (iOS).
        
    - **Data payload** **không** được phát ngay tới `onMessageReceived` (Android) hay delegate silent push (iOS) và **không** chạy ngầm. Thay vào đó, data sẽ được **gắn vào Intent extras** (Android) hoặc `userInfo` (iOS) và chỉ có thể được truy xuất **khi người dùng nhấn** vào notification để mở app. ([Firebase](https://firebase.google.com/docs/cloud-messaging/concept-options?utm_source=chatgpt.com "About FCM messages | Firebase Cloud Messaging - Google"))
        

---

### Tóm lại

- **Mixed message** bảo đảm rằng user luôn **thấy** notification (bởi hệ thống lo phần UI), nhưng **data** kèm theo chỉ được **truyền tới app** khi user **tương tác** với notification chứ **không** được xử lý ngầm khi app đã kill.
    
- Nếu bạn cần **xử lý ngầm ngay cả khi app tắt**, phải gửi **pure data message** (với priority cao và chỉ payload `data`), nhưng lưu ý rằng trên iOS khi app bị force‑quit hoặc trên một số Android ROM tuỳ biến thì data-only message đôi khi cũng không được kích hoạt.
    

Hy vọng giải đáp giúp bạn hiểu rõ hơn về cách FCM xử lý combined payload!



Đúng vậy, nếu bạn chọn **gửi qua Topic Messaging** (tức là trong payload bạn dùng `"to":"/topics/your_topic"` hoặc gọi `admin.messaging().sendToTopic("your_topic", …)`), thì **chỉ những thiết bị đã gọi**

```java
FirebaseMessaging.getInstance().subscribeToTopic("your_topic");
```

trên client trước đó mới nhận được notification.

---

### Các trường hợp không cần subscribe

1. **Gửi đến “All app users”** trên Firebase Console
    
    - Khi bạn vào **Cloud Messaging → New campaign → Target: All users**, Firebase Console sẽ gửi tới **tất cả các token** mà FCM đã lưu cho dự án, không cần topic hay subscribe.
        
2. **Gửi trực tiếp bằng Registration Tokens**
    
    - Bạn giữ danh sách token trên server và gọi API
        
        ```js
        admin.messaging().sendToDevice([token1, token2, …], payload)
        ```
        
        → ngay cả khi client không subscribe topic nào, vẫn nhận thông báo.
        
3. **Gửi bằng Condition**
    
    - Dùng
        
        ```json
        {
          "condition": "'news' in topics || 'promo' in topics",
          …
        }
        ```
        
        → tương tự topic, nhưng bạn có thể kết hợp nhiều topic trong biểu thức. Các client vẫn phải subscribe ít nhất một trong các topic xuất hiện trong condition.
        

---

### Kết luận

- **Topic Messaging** → **bắt buộc subscribe** trước.
    
- **All users / Device tokens / Conditions** → không cần subscribe (miễn là token của thiết bị có trong phân khúc hoặc bạn gửi trực tiếp).
    

Nếu bạn muốn mọi user tự động nhận notification mà không cần phải subscribe thủ công, hãy gửi qua **All app users** trên Console hoặc qua **registration tokens**/**device group** chứ không dùng topic.


Đúng vậy. Khi bạn thêm và khởi tạo Firebase Messaging SDK trong app Android, SDK sẽ tự động:

1. **Đăng ký với FCM backend**  
    – Ngay lần đầu app khởi chạy (hoặc khi `FirebaseApp.configure()` gọi xong), SDK sẽ liên hệ với FCM để tạo **registration token** duy nhất cho instance app đó.  
    – Token này là chuỗi dài ngẫu nhiên, dùng để định danh app/device khi gửi push.
    
2. **Cung cấp token cho bạn**  
    – Bạn có thể lấy token bất kỳ lúc nào bằng:
    
    ```java
    FirebaseMessaging.getInstance().getToken()
      .addOnCompleteListener(task -> {
        if (task.isSuccessful()) {
          String token = task.getResult();
          // TODO: gửi token về server để lưu
        }
      });
    ```
    
    – Đồng thời, mỗi khi token được cấp mới hoặc refresh (ví dụ user cài lại app, xóa data, hoặc FCM rotate token), SDK sẽ gọi callback trong `FirebaseMessagingService.onNewToken()` để bạn kịp thời cập nhật token trên server.
    
3. **Bạn là người “đăng ký” token**  
    – Mặc dù SDK tự fetch token, nhưng bạn phải **gửi token đó lên server của bạn** (qua API riêng), để server có danh sách token dùng khi gọi FCM gửi message.  
    – Nếu không lưu token, bạn sẽ không biết gửi đến đâu.
    
4. **Tóm lại**
    
    - **SDK tự khởi tạo token** ngay khi khởi app.
        
    - **Dev cần gọi `getToken()` và/or xử lý `onNewToken()`** để lấy và lưu token.
        
    - **FCM backend sẽ nhận diện thiết bị** dựa trên token này mỗi lần bạn gửi push.
        

Vậy bạn không phải subscribe thủ công để lấy token—SDK lo chuyện đó—nhưng bạn vẫn phải thu thập token và quản lý trên server để sử dụng cho việc gửi thông báo.

---

Khi bạn **thoát hẳn app** (force‑swipe kill hoặc app chưa chạy nền), thì:

1. **`onMessageReceived()` không được gọi** cho các tin nhắn chỉ có `notification` payload hoặc mixed payload (khi app bị kill, hệ thống sẽ tự hiển thị notification, chứ không khởi service để gọi `onMessageReceived`)
    
2. Nếu bạn gửi **data‑only** message thì cũng **chỉ** `onMessageReceived()` được gọi khi app vẫn còn chạy nền (background) — nếu app kill hoàn toàn, FCM **không** khởi service để deliver, message sẽ chờ tới khi app chạy lại.
    

Vậy trong tình huống của bạn — bạn override `onMessageReceived()` rồi **comment** phần `sendNotification(…)` đi — khi app kill:

- FCM nhận ra bạn gửi mixed (notification+data), nên **hệ thống tự bắn** notification tray dựa theo phần `notification` payload.
    
- Nhưng nếu bạn gửi **data‑only**, hệ thống sẽ không bắn gì, và bạn cũng không gọi `sendNotification()`, nên chẳng có gì hiện ra.
    

---

## Cách khắc phục

1. **Nếu bạn muốn hệ thống tự hiển thị** (kể cả khi app kill), hãy gửi **notification message** (hoặc mixed) và **đảm bảo đã khai báo** `default_notification_channel_id` trong `AndroidManifest.xml`:
    
    ```xml
    <application …>
      <!-- ID channel bạn tạo ở khởi động app -->
      <meta-data
        android:name="com.google.firebase.messaging.default_notification_channel_id"
        android:value="your_channel_id"/>
    </application>
    ```
    
    Đồng thời, **tạo channel** trong code khởi động:
    
    ```kotlin
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val chan = NotificationChannel("your_channel_id",
          "My Channel", NotificationManager.IMPORTANCE_HIGH)
      NotificationManagerCompat.from(this)
          .createNotificationChannel(chan)
    }
    ```
    
    Khi đó, nếu payload JSON là:
    
    ```json
    {
      "to":"…",
      "notification":{
        "title":"Tiêu đề",
        "body":"Nội dung"
      }
    }
    ```
    
    → hệ thống sẽ tự động show notification, không cần `onMessageReceived`.
    
2. **Nếu bạn muốn tùy biến hoàn toàn** (ví dụ dùng `data` để build UI khác, add action buttons…), hãy gửi **data‑only** hoặc **mixed** nhưng trong `onMessageReceived` **phải**:
    
    - Bỏ comment phần gọi `sendNotification(...)` hoặc `NotificationHelper.enqueueNotification(...)`
        
    - Thực sự gọi `show()` lên `NotificationManager` với một `NotificationCompat.Builder`.
        
    
    Ví dụ tối giản trong `onMessageReceived`:
    
    ```kotlin
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
      remoteMessage.data.let { data ->
        val title = data["title"] ?: "FCM Message"
        val body  = data["body"]  ?: ""
        sendNotification(title, body)
      }
    }
    
    private fun sendNotification(title: String, body: String) {
      val intent = Intent(this, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
      }
      val pi = PendingIntent.getActivity(this,0,intent,
          PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
    
      val notif = NotificationCompat.Builder(this, "your_channel_id")
          .setContentTitle(title)
          .setContentText(body)
          .setSmallIcon(R.drawable.ic_notification)
          .setContentIntent(pi)
          .setAutoCancel(true)
          .build()
    
      NotificationManagerCompat.from(this)
          .notify( Random().nextInt(), notif)
    }
    ```
    
3. **Chú ý priority** khi gửi data‑only:
    
    ```json
    {
      "to":"…",
      "data":{ "key":"value" },
      "priority":"high"
    }
    ```
    
    → hệ thống ưu tiên deliver ngay, nhưng vẫn chỉ khi app chưa kill.
    

---

### Kết luận

- **App kill hoàn toàn** → chỉ có **notification payload** (notification‑only hoặc mixed) mới đảm bảo **hệ thống** bắn notification tray.
    
- Nếu bạn rely vào `onMessageReceived` để tự show, thì **phải** có data‑only hoặc mixed **và** app còn chạy nền; thêm vào đó, **không** comment mất phần `sendNotification()`.
    
- Đừng quên tạo **Notification Channel** và khai báo `default_notification_channel_id` để FCM tự show cho bạn khi app không chạy.
    

Kiểm tra lại payload bạn gửi, bỏ comment logic hiển thị notification trong `onMessageReceived`, và cấu hình channel phù hợp — đảm bảo bạn sẽ thấy notification ngay cả khi app đã tắt.


---

Khi user bấm vào notification (mixed message có cả phần `notification` và `data`), Android sẽ khởi lại (hoặc resume) `Activity` bạn chỉ định trong `PendingIntent`—và **gán toàn bộ data payload vào Intent extras**. Để lấy những giá trị này khi app được mở, bạn làm như sau:

1. **Khai báo PendingIntent chứa data**  
    Khi xây dựng notification trong `FirebaseMessagingService`, bạn thường tạo `Intent` trỏ vào `MainActivity` (hoặc Activity bất kỳ), rồi thêm luôn `remoteMessage.data` vào Intent đó:
    
    ```kotlin
    val intent = Intent(this, MainActivity::class.java).apply {
      flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
      // copy data payload thành extras
      for ((key, value) in remoteMessage.data) {
        putExtra(key, value)
      }
    }
    val pendingIntent = PendingIntent.getActivity(
      this, 0, intent,
      PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )
    ```
    
2. **Trong `onCreate()` của Activity**, kiểm tra `intent.extras` để lấy data:
    
    ```kotlin
    class MainActivity : AppCompatActivity() {
      override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Lấy bundle extras
        intent.extras?.let { bundle ->
          // Ví dụ bạn gửi key "orderId" và "status"
          val orderId = bundle.getString("orderId")
          val status  = bundle.getString("status")
          if (orderId != null && status != null) {
            // Xử lý tiếp: ví dụ mở màn OrderDetail với orderId...
            showOrderDetail(orderId, status)
          }
        }
      }
    
      // Nếu Activity đã chạy sẵn và bạn bật lại notification
      override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)  // cập nhật intent mới
        intent.extras?.let { bundle ->
          val orderId = bundle.getString("orderId")
          val status  = bundle.getString("status")
          if (orderId != null && status != null) {
            showOrderDetail(orderId, status)
          }
        }
      }
    
      private fun showOrderDetail(id: String, status: String) {
        // Logic chuyển tới fragment hoặc hiển thị dialog, v.v.
      }
    }
    ```
    
3. **Chú ý**
    
    - Dùng `PendingIntent.FLAG_UPDATE_CURRENT` để hệ thống **cập nhật** extras mỗi lần có notification mới, tránh dùng Intent “cũ”.
        
    - Đảm bảo mỗi key bạn gửi trong `data` payload không trùng với key hệ thống (như `"google.sent_time"`, `"google.message_id"`), và dùng tên rõ ràng, ví dụ `"orderId"`, `"promoCode"`, v.v.
        
    - Nếu bạn gửi notification qua Firebase console (Notifications composer), console sẽ **tự động** gắn data payload vào extras khi you chọn phần “Advanced options → Additional data”.
        
4. **Tóm lại**
    
    - Mixed message → hệ thống show notification, đồng thời gán tất cả data payload vào **Intent extras** của PendingIntent.
        
    - Khi user mở ứng dụng qua notification, **hãy đọc `intent.extras`** trong `onCreate` hoặc `onNewIntent` để xử lý data ấy.
        

Vậy là bạn đã có cách lấy và xử lý data payload ngay khi app được mở từ notification!



---

Đúng rồi, trong trường hợp bạn **không dùng** `onMessageReceived()` để tạo notification (ví dụ bạn gửi **notification-only** hoặc **mixed** và để hệ thống tự bắn) thì bạn vẫn hoàn toàn có thể **gán data vào PendingIntent** để khi user chạm vào notification thì app mở lên kèm dữ liệu. Cách làm như sau:

---

## 1. Tạo PendingIntent trong Manifest hoặc Service

Bạn tạo một **BroadcastReceiver** hoặc **Activity** làm entry point khi user mở notification. Ví dụ dùng Activity `DeepLinkActivity` chỉ để nhận data:

```kotlin
// Tạo Intent hướng về DeepLinkActivity
val intent = Intent(this, DeepLinkActivity::class.java).apply {
    flags = Intent.FLAG_ACTIVITY_NEW_TASK or
            Intent.FLAG_ACTIVITY_CLEAR_TOP
    // Gán toàn bộ data payload làm extras
    remoteMessage.data.forEach { (key, value) ->
        putExtra(key, value)
    }
}
val pendingIntent = PendingIntent.getActivity(
    this,
    0,
    intent,
    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
)

// Dùng PendingIntent này khi config notification builder:
val notif = NotificationCompat.Builder(this, CHANNEL_ID)
    .setContentTitle("Bạn có thông báo mới")
    .setContentText("Chạm để xem chi tiết")
    .setSmallIcon(R.drawable.ic_notify)
    .setContentIntent(pendingIntent)
    .setAutoCancel(true)
    .build()

NotificationManagerCompat.from(this).notify(NOTIF_ID, notif)
```

> **Lưu ý**:
> 
> - Dùng `FLAG_UPDATE_CURRENT` để mỗi lần notification mới, extras được cập nhật theo payload mới.
>     
> - Nếu bạn gửi notification-only qua console, bạn phải dùng **Cloud Functions** hoặc server để đính data vào phần `data` payload.
>     

---

## 2. Bắt Intent trong Activity khi app launch

Trong `DeepLinkActivity` (hoặc `MainActivity` nếu bạn dùng luôn):

```kotlin
class DeepLinkActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Không cần layout, chỉ xử lý data rồi chuyển tiếp
        handleDeepLink(intent)
        finish() // đóng ngay sau khi xử lý hoặc chuyển hướng
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleDeepLink(intent)
        finish()
    }

    private fun handleDeepLink(intent: Intent) {
        intent.extras?.let { extras ->
            // Ví dụ bạn gửi "orderId" trong data payload
            val orderId = extras.getString("orderId")
            val promo   = extras.getString("promoCode")
            if (orderId != null) {
                // Chuyển đến màn OrderDetail với orderId
                val detailIntent = Intent(this, OrderDetailActivity::class.java).apply {
                    putExtra("orderId", orderId)
                    putExtra("promoCode", promo)
                }
                startActivity(detailIntent)
            } else {
                // Nếu không có data, mở màn chính
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }
}
```

- Khi notification được chạm, Android sẽ khởi `DeepLinkActivity` với **Intent** chứa extras bạn đã gán.
    
- `onCreate` (và `onNewIntent` nếu Activity đã tồn tại) đều đọc được các giá trị này.
    

---

## 3. Đảm bảo cấu hình manifest

Trong `AndroidManifest.xml`, khai báo `DeepLinkActivity`:

```xml
<activity
    android:name=".DeepLinkActivity"
    android:exported="true">
    <!-- Nếu muốn bắt luôn cả launch từ launcher -->
    <intent-filter>
        <action android:name="android.intent.action.MAIN"/>
        <category android:name="android.intent.category.DEFAULT"/>
    </intent-filter>
</activity>
```

Hoặc chỉ cần **không cần** intent‑filter, vì bạn khởi trực tiếp bằng PendingIntent.

---

### Kết luận

1. **Dù app bị kill**, hệ thống vẫn sử dụng `PendingIntent` bạn đã xây dựng để **mở Activity** với Intent extras.
    
2. **Activity** đó sẽ nhận đầy đủ `data payload` từ notification và cho bạn xử lý ngay khi app khởi.
    
3. Bạn không cần dùng `onMessageReceived()` nếu đã để hệ thống tự bắn notification — chỉ cần chắc chắn đã gán data vào PendingIntent đúng cách.
    

Với cách này, mọi dữ liệu bạn đính kèm trong `remoteMessage.data` đều có thể đọc lại khi user mở notification, kể cả app vừa bị kill.


---
Chính xác, bạn đã chỉ ra một điểm rất quan trọng và phổ biến của FCM trên Android.

Khi ứng dụng của bạn bị người dùng "kill" (vuốt khỏi trình đa nhiệm) hoặc bị hệ điều hành dừng hoàn toàn (force-stopped), dịch vụ `FirebaseMessagingService` và phương thức `onMessageReceived` sẽ **không được gọi** đối với các thông báo chỉ chứa `data` payload. Đây là một cơ chế tiết kiệm pin của Android.

---

### ✨ Giải pháp: Kết hợp cả `notification` và `data` payload

Để đảm bảo thông báo **luôn luôn hiển thị** ngay cả khi app bị kill, bạn cần gửi một payload "lai" chứa **cả hai** khối `notification` và `data` từ server.

**Cách hoạt động của payload "lai":**

1. **Khi App bị Kill / Chạy nền:** Hệ điều hành Android sẽ tự động xử lý khối `notification` và hiển thị một thông báo **đơn giản** (chỉ có tiêu đề và nội dung) lên khay hệ thống. `onMessageReceived` sẽ **KHÔNG** được gọi. Khi người dùng nhấn vào thông báo này, app sẽ được mở và bạn có thể lấy dữ liệu từ khối `data` trong Intent của Activity khởi chạy.
    
2. **Khi App đang mở (Foreground):** `onMessageReceived` **SẼ** được gọi. Bạn sẽ nhận được cả hai khối `notification` và `data`. Lúc này, bạn có toàn quyền sử dụng `data` để tự xây dựng và hiển thị thông báo tùy chỉnh với các nút action như đã làm trước đây.
    

---

### ## 1. Phía Server Python (Cập nhật)

Bạn chỉ cần thêm khối `notification` vào code gửi tin nhắn trước đó.

Python

```
import firebase_admin
from firebase_admin import credentials, messaging
import json

# ... khởi tạo firebase_admin ...

def send_hybrid_fcm(token):
    actions_payload = json.dumps([
        {"action": "APPROVE_ACTION", "title": "Đồng ý 👍"},
        {"action": "REJECT_ACTION", "title": "Từ chối 👎"}
    ])

    message = messaging.Message(
        # ---- THÊM KHỐI NÀY VÀO ----
        # Khối này đảm bảo thông báo sẽ hiện khi app bị kill
        notification=messaging.Notification(
            title='Yêu cầu phê duyệt',
            body='Bạn có một yêu cầu mới cần xử lý.'
        ),
        # ---------------------------

        # Giữ nguyên khối data để xử lý khi app đang chạy hoặc khi người dùng mở app
        data={
            'title': 'Yêu cầu phê duyệt', # Vẫn nên có để tùy chỉnh trong onMessageReceived
            'body': 'Bạn có một yêu cầu mới cần xử lý.',
            'actions': actions_payload,
            'notification_id': '12345'
        },
        token=token,
        # Đặt độ ưu tiên cao để tăng khả năng nhận được khi chạy nền
        android=messaging.AndroidConfig(
            priority='high'
        )
    )

    try:
        response = Messaging(message)
        print('Gửi thông báo lai thành công:', response)
    except Exception as e:
        print('Lỗi khi gửi thông báo:', e)

# Gọi hàm
device_token = "YOUR_DEVICE_REGISTRATION_TOKEN"
send_hybrid_fcm(device_token)
```

### ## 2. Phía Client Android (Không cần thay đổi nhiều)

Code trong `MyFirebaseMessagingService` và `NotificationActionReceiver` của bạn về cơ bản không cần thay đổi. Logic vẫn như cũ:

- **Nếu `onMessageReceived` được gọi (app đang mở):** Nó sẽ đọc `data` payload và hiển thị thông báo với các nút action. Hoàn hảo!
    
- **Nếu `onMessageReceived` không được gọi (app bị kill):**
    
    - Hệ thống sẽ tự hiển thị thông báo đơn giản từ khối `notification`.
        
    - Khi người dùng nhấn vào thông báo đó, `Launcher Activity` của bạn sẽ khởi động.
        
    - Bạn cần thêm code vào `onCreate` của `Launcher Activity` để kiểm tra `intent.getExtras()` và xem có dữ liệu từ FCM không. Dựa vào đó, bạn có thể điều hướng người dùng đến màn hình chi tiết.
        

Kotlin

```
// Trong MainActivity.kt hoặc Launcher Activity của bạn
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    // Xử lý khi app được mở từ một thông báo bị kill
    handleIntent(intent)
}

// Xử lý khi app đã mở và nhận được intent mới
override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    intent?.let { handleIntent(it) }
}

private fun handleIntent(intent: Intent) {
    val extras = intent.extras
    if (extras != null) {
        // Kiểm tra xem có phải mở từ FCM không
        val notificationId = extras.getString("notification_id")
        if (notificationId != null) {
            Log.d("FCM_LAUNCH", "App được mở từ thông báo có ID: $notificationId")
            // TODO: Điều hướng đến màn hình phù hợp
            // val deepLinkIntent = Intent(this, DetailActivity::class.java)
            // deepLinkIntent.putExtras(extras)
            // startActivity(deepLinkIntent)
        }
    }
}
```

### Tổng kết

|Phương pháp|Ưu điểm|Nhược điểm|
|---|---|---|
|**Chỉ `data`**|Toàn quyền tùy chỉnh thông báo với các nút action.|**Không hiển thị thông báo khi app bị kill.**|
|**`notification` + `data`**|**Luôn đảm bảo thông báo được hiển thị.**|Khi app bị kill, thông báo hiển thị là dạng **đơn giản, không có nút action**.|

Đối với hầu hết các ứng dụng cần độ tin cậy cao, phương pháp **kết hợp `notification` và `data` là lựa chọn tốt nhất.**


---

Chắc chắn rồi. Việc xử lý 2 trường hợp này ở phía client rất rõ ràng. Bạn sẽ viết code ở hai nơi khác nhau:

1. **`FirebaseMessagingService`:** Để xử lý khi app đang chạy và nhận được thông báo tùy chỉnh với các nút bấm.
    
2. **`MainActivity` (hoặc Activity khởi chạy):** Để xử lý khi app được mở lên từ một thông báo đơn giản (khi app bị kill).
    

Dưới đây là cách triển khai chi tiết.

---

### ## Trường hợp 1: App đang mở (Foreground) -> Xử lý trong `FirebaseMessagingService`

Phần này bạn đã làm ở các bước trước. Khi app của bạn đang chạy ở foreground, `onMessageReceived` sẽ được gọi. Code trong đây sẽ đọc `data` payload và dùng `NotificationCompat.Builder` để **tự tạo ra một thông báo đầy đủ với các nút action**.

Kotlin

```
// Trong file MyFirebaseMessagingService.kt
class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Lấy Application context để đảm bảo an toàn
        val appContext = applicationContext ?: return

        // Khi app đang chạy, ta ưu tiên dữ liệu trong `data` để tạo thông báo tùy chỉnh.
        if (remoteMessage.data.isNotEmpty()) {
            val data = remoteMessage.data
            val title = data["title"] ?: "Thông báo"
            val body = data["body"] ?: "Bạn có tin nhắn mới."
            val notificationId = data["notification_id"]?.toIntOrNull() ?: System.currentTimeMillis().toInt()
            val actionsJson = data["actions"]

            // Gọi hàm để xây dựng và hiển thị thông báo với các nút bấm
            showNotificationWithActions(appContext, title, body, actionsJson, notificationId)
        }
        // Có thể thêm else ở đây để xử lý nếu chỉ có `notification` payload
    }

    private fun showNotificationWithActions(context: Context, title: String, body: String, actionsJson: String?, notificationId: Int) {
        // ... (Code xây dựng thông báo với NotificationCompat.Builder và các action)
        // ... (Phần này giống hệt như câu trả lời trước)
        // Đoạn code này sẽ tạo ra thông báo với nút "Đồng ý" và "Từ chối"
    }
}
```

**Kết quả của trường hợp này:** Người dùng sẽ thấy một thông báo đầy đủ chức năng với các nút bấm, vì app của bạn đang hoạt động và có thể xử lý nó.

---

### ## Trường hợp 2: App bị Kill / Chạy nền -> Xử lý trong `Activity`

Khi app bị kill, hệ thống Android sẽ tự hiển thị thông báo **đơn giản** từ khối `notification`. Khi người dùng nhấn vào thông báo này, Android sẽ mở `Launcher Activity` của bạn (thường là `MainActivity`) và đính kèm `data` payload vào trong `Intent`.

Bạn cần xử lý `Intent` này để đọc dữ liệu.

Kotlin

```
// Trong file MainActivity.kt (hoặc Activity được chỉ định làm launcher)
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // **Bước 1: Xử lý Intent ngay khi Activity được tạo**
        // Đây là nơi xử lý khi app được khởi chạy từ trạng thái kill
        handleNotificationIntent(intent)
    }

    // **Bước 2: Xử lý Intent khi Activity đã chạy sẵn**
    // QUAN TRỌNG: Cần phải có để xử lý khi app đang ở background (chưa bị kill)
    // và người dùng nhấn vào thông báo.
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            handleNotificationIntent(it)
        }
    }

    /**
     * Hàm chung để xử lý logic từ dữ liệu của thông báo
     */
    private fun handleNotificationIntent(intent: Intent) {
        val extras = intent.extras
        if (extras != null) {
            // Dùng một key đặc trưng từ `data` payload để kiểm tra
            // Ví dụ, kiểm tra xem có `notification_id` không
            if (extras.containsKey("notification_id")) {
                val notificationId = extras.getString("notification_id")
                val title = extras.getString("title")
                Log.d("FCM_LAUNCH", "App được mở từ thông báo. ID: $notificationId, Title: $title")

                // TODO: VIẾT LOGIC CỦA BẠN TẠI ĐÂY
                // Ví dụ: Dựa vào dữ liệu để điều hướng tới màn hình cụ thể
                // val orderId = extras.getString("order_id")
                // val detailIntent = Intent(this, OrderDetailActivity::class.java)
                // detailIntent.putExtra("EXTRA_ORDER_ID", orderId)
                // startActivity(detailIntent)

                // Sau khi xử lý, bạn có thể xóa cờ của intent đi để tránh xử lý lại
                setIntent(Intent())
            }
        }
    }
}
```

**Kết quả của trường hợp này:** App của bạn sẽ mở ra. Dù không có các nút action trên thông báo, bạn vẫn nhận được đầy đủ dữ liệu và có thể tự động điều hướng người dùng đến đúng nội dung họ cần xem.

---

### ## Sơ đồ luồng xử lý

Code snippet

```
graph TD
    A[FCM gửi payload 'notification' + 'data'] --> B{App đang mở?};
    B -- Có --> C[onMessageReceived được gọi];
    C --> D[Tự tạo thông báo TÙY CHỈNH với các nút action];

    B -- Không --> E[Hệ thống tự hiển thị thông báo ĐƠN GIẢN];
    E --> F{Người dùng nhấn vào thông báo};
    F --> G[Mở MainActivity];
    G --> H[Xử lý dữ liệu trong onCreate / onNewIntent];
    H --> I[Điều hướng đến màn hình phù hợp];
```

Bằng cách triển khai ở cả hai nơi như trên, bạn đã bao phủ được tất cả các kịch bản và đảm bảo ứng dụng của mình luôn xử lý thông báo một cách chính xác, bất kể trạng thái của nó là gì.