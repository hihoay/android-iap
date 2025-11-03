

## Giới thiệu

FCM có 2 luồng tương tác khi muốn gửi Message từ Cloud:

- `App đang mở`: App sẽ nhận được Message mà không có thông báo qua phương thức `onMessageReceived` trong đối tượng  `class FCMService : FirebaseMessagingService` lúc này thì App sẽ nhận thông tin `RemoteMessage` và xử lý tiếp theo.
-  `App đã đóng hoặc bị Kill khi User bật đa nhiệm và đóng App`: Lúc này thông qua `System Tray` thì Android sẽ bắn lên thông báo (Icon, Title, Body và chứa Data qua Bundle Intent)  lên để thông báo cho User.  Khi User bấm vào thông báo sẽ mở lên Activity Main và truyền dữ liệu qua Bundle Intent. Ứng dụng của luồng này là có thể thúc đẩy User mở App nếu họ Quên hoặc gọi lại User mở App. (Nếu User đã cấp quyền Notify cho App)
## Sử dụng FMC

1. Xin quyền và tích hợp trên Activity

```kotlin
    override fun onCreate(savedInstanceState: Bundle?) {  
        super.onCreate(savedInstanceState)  
        enableEdgeToEdge()  
        ....
        checkAndRequestNotificationPermission(  
            onPermissionGranted = {  
	            // Chạy vào đây nếu đã được User cấp quyền Notify
                Log.e("NotificationPermission", "onPermissionGranted")  
                // Gọi chạy setup FCM và khai báo phương thức nhận Token mới của User,
                // đăng ký thêm topic nếu có, mặc định sẽ đăng ký topic "all"
                // fun AppCompatActivity.taymayFCM(topics: List<String> = listOf("all"), newToken: (token: String) -> Unit  )
                taymayFCM{ newToken ->  
                    Log.e("newToken", newToken)  
                    eventTracking("new_fcm_token", mapOf("token" to newToken))
                }  
            }, onPermissionDenied = { isShouldShowRequestPermissionRationale ->  
            // Chạy vào đây nếu bị từ chối cấp quyền
            // isShouldShowRequestPermissionRationale thể hiện là phải làm giao diện xin quyền và vào Settings để cấp quyền
                Log.e(  
                    "NotificationPermission",  
                    "onPermissionDenied. $isShouldShowRequestPermissionRationale"  
                )  
                if (isShouldShowRequestPermissionRationale)  
                    showDialogAskGoToSettingsAllowNotifyPermission()  
            }  
        )  
        intent.extras?.let { extras ->   
        // dữ liệu Data được truyền qua Bundle Extras nếu User đóng app và mở app App thông qua việc bấm vào Notify. Lấy dữ liệu để xử lý thường được chạy ở màn Splash
            Log.e("Extra Receiver", "${extras.toJson().toString()}")  
			if (extras.keySet()  
			        .firstOrNull { it.equals("messageId") } != null  
			) eventTracking("open_splash", mapOf("from" to "message"))  
			else eventTracking("open_splash", mapOf("from" to "user"))
        }  
    }
```

2. AndroidManifest.xml không phải làm gì nếu không muốn ghi đè lại các giá trị mặc định, nếu muốn ghi đè lại thì có những giá trị dưới đây để ghi đè:

```xml
<application>  
    <meta-data  
        android:name="com.google.firebase.messaging.default_notification_channel_id"  
        android:value="@string/def_fcm_notify" />  
    <meta-data  
        android:name="com.google.firebase.messaging.default_notification_icon"  
        android:resource="@drawable/ic_fcm_notify" />  
    <meta-data  
        android:name="com.google.firebase.messaging.default_notification_color"  
        android:resource="@color/def_fcm_notify" />  
    <meta-data  
        android:name="firebase_messaging_auto_init_enabled"  
        android:value="false" />  
    <meta-data  
        android:name="firebase_analytics_collection_enabled"  
        android:value="false" />  
        ...
</application>
```

3. Cấu hình cho Application `class`

```kotlin
   class MyApplication : Application() {
      override fun onCreate() {  
        super.onCreate()  
        // Khai báo phương thức nhận Message khi User đang mở App
        FCMService.newMessageReceived = { jsonMessageReceived ->  
            Log.e("newMessageReceived", jsonMessageReceived.toString())  
        }  
        // Khai báo phương thức khi hệ thống thay đổi Token User sử dụng để định danh User và bắn notify từ Server xuống. Mặc định thì đã khai báo ở phương thức taymayFCM
//        FCMService.newToken = { token ->  
//            Log.e("newToken", token)  
//        }  
		....
    }
   }
```
4. Đăng ký topic
```kotlin
	// Đăng ký topic cho User để phân loại notify khi muốn bắn cho từng chủ đề cụ thể,
	// mặc định mọi user sẽ đăng ký topic "all"
	subscribeToTopic("topic_name")
```
