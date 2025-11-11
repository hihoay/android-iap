Tuyệt vời. Mình sẽ tổng hợp toàn diện về Firebase Cloud Messaging (FCM), bao gồm: tổng quan và kiến trúc hoạt động, các ứng dụng thực tế (use cases), cách tích hợp trên Android/iOS, cùng với các best practices để đảm bảo hiệu quả, bảo mật và tiết kiệm tài nguyên.

Mình sẽ quay lại ngay khi hoàn thành bản tổng hợp chi tiết cho bạn.

# Firebase Cloud Messaging (FCM) – Tổng quan, Ứng dụng và Hướng dẫn Tích hợp

## 1. Tổng quan và Kiến trúc hoạt động của FCM

_Sơ đồ kiến trúc FCM: (1) tạo yêu cầu gửi thông báo (qua Firebase console hoặc máy chủ ứng dụng), (2) máy chủ FCM backend xử lý phân phối, (3) tầng vận chuyển nền tảng (Android qua Google Play, iOS qua APNs, Web qua Web Push), (4) SDK FCM trên thiết bị nhận và hiển thị thông báo._

**Firebase Cloud Messaging (FCM)** là giải pháp nhắn tin đa nền tảng của Google, cho phép gửi thông báo tin cậy đến ứng dụng client trên Android, iOS và web. Kiến trúc FCM gồm bốn thành phần chính:

- **Thành phần tạo yêu cầu thông báo:** Có thể là giao diện **Notifications composer** trên Firebase console (GUI) hoặc hệ thống máy chủ đáng tin cậy của bạn (sử dụng Firebase Admin SDK hoặc FCM HTTP API) để xây dựng và gửi yêu cầu thông báo.
    
- **FCM Backend (máy chủ FCM):** Nhận các yêu cầu gửi tin từ thành phần trên, xử lý phân phối (ví dụ: _fan-out_ tin nhắn đến nhiều thiết bị theo chủ đề) và gắn thông tin metadata (như ID thông báo).
    
- **Tầng vận chuyển nền tảng:** FCM không gửi trực tiếp đến thiết bị, mà chuyển tin nhắn qua dịch vụ push của từng nền tảng. Cụ thể: Android thông qua **Android Transport Layer** (dịch vụ Google Play), iOS thông qua **Apple Push Notification service (APNs)**, và web qua **Web Push Protocol**. (Lưu ý: với iOS, cần cấu hình chứng chỉ hoặc khóa APNs trong Firebase để FCM có thể giao tiếp với APNs).
    
- **FCM SDK trên thiết bị:** Được tích hợp trong ứng dụng di động. SDK này quản lý việc đăng ký nhận thông báo (cấp **mã token đăng ký** cho app) và đảm bảo thông báo được hiển thị hoặc xử lý tùy theo trạng thái ứng dụng (foreground/background) và logic của app.
    

**Chu trình hoạt động:** Mỗi instance app khi khởi chạy lần đầu sẽ đăng ký với FCM và nhận **mã thông báo đăng ký (registration token)** duy nhất. Server ứng dụng cần lưu token này. Khi muốn gửi thông báo, server sẽ gửi yêu cầu kèm nội dung và token thiết bị lên FCM (được ủy quyền bằng khóa bảo mật của dự án). FCM backend xác định thiết bị dựa trên token, chuyển tiếp thông báo qua dịch vụ push phù hợp (Google hoặc APNs), cuối cùng đẩy tới thiết bị, nơi SDK FCM nhận và hiện thông báo.

**Server key và Device token:** Để gửi một thông báo xuống thiết bị qua FCM, server cần **hai thông tin quan trọng**: (a) **FCM device token** của thiết bị (địa chỉ định danh app trên thiết bị đó) và (b) **FCM server key** của dự án (khóa bí mật để xác thực quyền gọi API FCM). **Device token** do SDK sinh ra gần như duy nhất toàn cầu cho mỗi app cài trên một thiết bị, dùng làm địa chỉ nhận thông báo. **Server key** (hoặc credential dịch vụ) nằm trong Firebase project, được dùng bởi máy chủ để xác thực khi gửi yêu cầu đến FCM. Nói cách khác, server key giống như “mật khẩu” của server khi gọi FCM, còn device token giống “địa chỉ” của người nhận. Chỉ khi có đúng **cả hai** thì mới gửi tin nhắn thành công; do đó, cần giữ bí mật server key và không nhúng vào ứng dụng client (tránh lộ cho người dùng).

## 2. Ứng dụng thực tế của FCM

FCM được sử dụng rộng rãi trong các ứng dụng di động để **gửi thông báo đẩy (push notification)** nhằm nâng cao tương tác và trải nghiệm người dùng. Một số ứng dụng thực tế tiêu biểu của FCM gồm:

- **Thông báo marketing & tái tương tác người dùng:** Ứng dụng có thể gửi tin khuyến mãi, ưu đãi, hoặc nhắc nhở người dùng quay lại ứng dụng. Ví dụ: gửi thông báo về đợt giảm giá, mã coupon, hoặc nội dung mới để **thu hút người dùng quay lại** (tăng re-engagement). FCM hỗ trợ phân nhóm đối tượng hoặc sử dụng **topic** (chủ đề) để gửi thông báo marketing đến một phân khúc người dùng nhất định (ví dụ: topic "khuyenmai" cho những người quan tâm khuyến mãi).
    
- **Thông báo hệ thống, sự kiện trong ứng dụng:** FCM dùng để gửi các alert quan trọng cho người dùng về hoạt động liên quan đến họ. Ví dụ: thông báo có tin nhắn mới, có người thích bình luận của bạn, cập nhật đơn hàng/thanh toán, nhắc lịch sự kiện hoặc cảnh báo hệ thống. Đây là các **notification** mang tính cá nhân, thời gian thực cho từng người dùng, giúp họ không bỏ lỡ các hoạt động quan trọng trong app.
    
- **Tin nhắn thời gian thực (chat, cập nhật tức thì):** Đối với ứng dụng chat hoặc cộng tác thời gian thực, FCM đóng vai trò thông báo cho thiết bị rằng **có dữ liệu mới** (tin nhắn mới, nội dung mới) để đồng bộ. Ví dụ: một ứng dụng nhắn tin sử dụng FCM để báo cho người nhận rằng có tin nhắn đến, sau đó app sẽ tải nội dung đầy đủ. FCM cho phép đính kèm payload dữ liệu tối đa ~4KB, đủ để gửi nội dung ngắn hoặc thông tin cần thiết ngay lập tức.
    
- **Cập nhật dữ liệu nền (silent push):** FCM còn được dùng để gửi các _data message_ “âm thầm” (không hiển thị thông báo cho người dùng) để ứng dụng cập nhật dữ liệu nền. Ví dụ: ứng dụng tin tức có thể định kỳ gửi data message để thông báo app tải trước bài viết mới, hoặc ứng dụng đồng bộ lịch, email dùng để báo có dữ liệu mới và app sẽ tự đồng bộ ở nền. Những thông báo dạng này **không làm phiền người dùng** và giúp dữ liệu luôn mới nhất. (Trên iOS, để làm được điều này yêu cầu payload đặc biệt với `content-available` và app có quyền Background Fetch).
    
- **Các trường hợp khác:** FCM cũng được dùng cho các thông báo nhắc nhở (như nhắc uống thuốc, nhắc sự kiện lịch), thông báo tình trạng từ thiết bị IoT, hoặc bất cứ kịch bản nào cần gửi một tín hiệu từ server đến ứng dụng client tức thời.
    

Nhờ khả năng **nhắm mục tiêu linh hoạt**, FCM cho phép gửi tin nhắn đến: một thiết bị đơn lẻ (thông qua token), một nhóm thiết bị (device group) hoặc tất cả thiết bị theo **topic** mà chúng subscribe. Điều này rất hữu ích cho các tình huống broadcast như gửi thông báo cho toàn bộ người dùng về sự kiện quan trọng hoặc gửi theo chủ đề mà người dùng quan tâm. Ngoài ra, công cụ Firebase Notifications composer có sẵn giao diện cho phép gửi nhanh các thông báo marketing và theo dõi hiệu quả (analytics) mà không cần code server.

## 3. Cách tích hợp FCM vào Android và iOS

Việc tích hợp FCM gồm hai phần: cấu hình dự án Firebase và thêm SDK vào ứng dụng client. Trước tiên, cần tạo dự án trên Firebase Console, kích hoạt dịch vụ Cloud Messaging, và thêm ứng dụng (Android/iOS) vào dự án đó. Firebase sẽ cung cấp file cấu hình ứng dụng cho mỗi nền tảng (ví dụ: `google-services.json` cho Android, `GoogleService-Info.plist` cho iOS) – cần đưa các file này vào project ứng dụng tương ứng. Dưới đây là hướng dẫn tích hợp cụ thể cho Android và iOS.

### 3.1. Tích hợp FCM vào Android

**Bước 1: Thêm Firebase vào ứng dụng Android.** Thêm file `google-services.json` đã tải về vào thư mục `app/` của dự án Android. Sau đó, thêm plugin và dependency Firebase vào dự án (cụ thể, thêm dòng `classpath 'com.google.gms:google-services:...` vào Gradle buildscript, và apply plugin `com.google.gms.google-services` cùng dependency thư viện FCM như `com.google.firebase:firebase-messaging:<version>` trong module app). Thông thường, Android Studio cung cấp **Firebase Assistant** để hỗ trợ thêm dễ dàng.

**Bước 2: Cấp quyền hiển thị thông báo (Android 13+).** Từ Android 13 (SDK 33) trở lên, ứng dụng cần xin quyền runtime `POST_NOTIFICATIONS` để cho phép hiển thị thông báo. Firebase Messaging SDK (v23.0.6+) đã tự khai báo quyền này trong manifest, nhưng **bạn vẫn phải yêu cầu người dùng cấp quyền lúc runtime** trước khi gửi thông báo. Hãy kiểm tra và gọi API `ActivityCompat.requestPermissions` hoặc `registerForActivityResult` để xin quyền nếu chưa được cấp, nhằm đảm bảo ứng dụng có thể hiện thông báo cho người dùng.

**Bước 3: Lấy **FCM Registration Token** của thiết bị.** Khi app chạy lần đầu, SDK FCM sẽ tự động cấp một token đăng ký duy nhất cho instance ứng dụng đó. Token này có thể truy xuất bất kỳ lúc nào. Bạn có thể gọi:

```java
FirebaseMessaging.getInstance().getToken()
    .addOnCompleteListener(task -> {
        if (!task.isSuccessful()) {
            Log.w(TAG, "Lấy FCM token thất bại", task.getException());
            return;
        }
        String token = task.getResult();
        Log.d(TAG, "FCM Token: " + token);
        // TODO: Gửi token này lên server của bạn để lưu trữ
    });
```

Token này sẽ ít thay đổi, trừ các trường hợp: người dùng cài lại app, xóa dữ liệu app, hoặc khôi phục app trên thiết bị mới – khi đó FCM có thể cấp token mới. Do đó, **cần gửi token lên server và cập nhật nếu token thay đổi** (bằng cách lắng nghe callback `onNewToken` của `FirebaseMessagingService`) để server luôn nắm token hiện hành của thiết bị.

**Bước 4: Cập nhật AndroidManifest và xử lý nhận thông báo.** Để ứng dụng nhận được tin nhắn FCM ngay cả khi chạy nền, bạn cần khai báo một Service kế thừa `FirebaseMessagingService` trong tệp `AndroidManifest.xml`:

```xml
<service
    android:name=".MyFirebaseMessagingService"
    android:exported="false">
    <intent-filter>
        <action android:name="com.google.firebase.MESSAGING_EVENT" />
    </intent-filter>
</service>
```

Khai báo này giúp Android biết chuyển tiếp thông báo FCM đến service của bạn. Trong lớp `MyFirebaseMessagingService`, bạn nên override phương thức `onMessageReceived` để xử lý các tin nhắn đến (ví dụ hiện thông báo tùy chỉnh hoặc cập nhật dữ liệu), và override `onNewToken` để nhận sự kiện khi token FCM được cập nhật (gửi token mới về server). Ví dụ tối giản:

```java
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        // Xử lý khi nhận thông báo hoặc data message từ FCM
        if (remoteMessage.getNotification() != null) {
            // Tin nhắn dạng notification có tiêu đề/nội dung
            showNotification(remoteMessage.getNotification().getTitle(),
                             remoteMessage.getNotification().getBody());
        }
        if (!remoteMessage.getData().isEmpty()) {
            // Tin nhắn dạng data có kèm dữ liệu tùy chỉnh
            handleDataPayload(remoteMessage.getData());
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "FCM token mới: " + token);
        sendTokenToServer(token); // Cập nhật token trên server
    }
}
```

Khi app ở **foreground**, FCM sẽ gọi `onMessageReceived` ngay lập tức cho cả thông báo dạng Notification và Data. Còn khi app **đang background**, nếu FCM gửi **notification message** thuần túy, Firebase SDK sẽ tự hiện thông báo trên thanh trạng thái (service của bạn _không_ nhận được callback). Nếu đó là **data message**, `onMessageReceived` sẽ được gọi ngay cả khi app nền, cho phép bạn xử lý tùy ý (thường là tạo một thông báo). Với trường hợp **tin nhắn hỗn hợp** (có cả notification _và_ data payload), khi app nền, FCM sẽ hiển thị phần notification lên system tray, còn phần data đi kèm sẽ được truyền đến intent mở Activity (khi người dùng nhấn vào thông báo) thay vì gọi ngay `onMessageReceived`.

**Bước 5: Xây dựng thông báo (nếu cần).** Nếu bạn muốn tùy biến cách hiển thị hoặc nội dung thông báo, bạn có thể tự dựng một `Notification` (sử dụng `NotificationCompat.Builder`) trong `onMessageReceived`. Lưu ý tạo **Notification Channel** cho các thông báo (đối với Android 8.0+). Nếu không tự tạo, FCM sẽ dùng channel mặc định với thiết lập cơ bản; tốt nhất bạn nên tạo channel riêng và thiết lập `default_notification_channel_id` trong manifest để FCM sử dụng khi hiển thị thông báo mặc định. Channel cho phép bạn cấu hình mức độ ưu tiên, âm thanh, v.v., và người dùng có thể quản lý các kênh thông báo này linh hoạt.

### 3.2. Tích hợp FCM vào iOS

**Bước 1: Thêm Firebase vào ứng dụng iOS.** Tích hợp Firebase SDK bằng CocoaPods hoặc Swift Package Manager. Thêm pod `Firebase/Messaging` vào project, sau đó chạy `pod install`. Đặt file `GoogleService-Info.plist` vào project (thường kéo vào Xcode). Trong code, gọi `FirebaseApp.configure()` tại lúc khởi động (ví dụ trong `AppDelegate.application(_:didFinishLaunchingWithOptions:)`) để khởi tạo Firebase.

**Bước 2: Cấu hình APNs và quyền thông báo.** Trên Apple Developer, tạo khóa APNs (hoặc chứng chỉ) và **upload khóa APNs** đó lên Firebase Project Settings (tab Cloud Messaging). Việc này cho phép FCM sử dụng APNs để gửi thông báo đến thiết bị iOS. Tiếp theo, bật **Push Notifications** và **Remote Notifications** trong _Capabilities_ của target ứng dụng trên Xcode.

Trong `AppDelegate`, đăng ký ứng dụng với APNs để nhận thông báo đẩy:

```swift
import UserNotifications
import FirebaseMessaging

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate, UNUserNotificationCenterDelegate, MessagingDelegate {
  func application(_ application: UIApplication,
                   didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
    // Cấu hình Firebase
    FirebaseApp.configure()

    // Yêu cầu quyền gửi thông báo cho người dùng
    UNUserNotificationCenter.current().delegate = self
    UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound, .badge]) { granted, error in
        // Xử lý sau khi người dùng cho hoặc từ chối phép
    }
    application.registerForRemoteNotifications()  // đăng ký với APNs
    Messaging.messaging().delegate = self         // đặt delegate để nhận token
    return true
  }

  // Nhận FCM token (registration token) mới hoặc cập nhật
  func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
    print("FCM token cho thiết bị: \(String(describing: fcmToken))")
    // TODO: Gửi token lên server để đăng ký
  }

  // ... các hàm khác ...
}
```

Giải thích: Đoạn code trên yêu cầu quyền hiển thị thông báo từ người dùng và đăng ký thiết bị với APNs (`registerForRemoteNotifications()`). Khi đăng ký thành công, iOS sẽ cung cấp **device token APNs**, Firebase SDK sẽ tự động _mapping_ device token này với FCM và lấy về **FCM registration token** tương ứng (thông qua cơ chế swizzling hoặc qua delegate). Bạn cài đặt `Messaging.messaging().delegate` để nhận callback `didReceiveRegistrationToken` – tại đây bạn sẽ có được `fcmToken` (chuỗi này chính là device token FCM, tương tự token bên Android). Hãy gửi token này về server của bạn để lưu trữ giống như với Android.

**Bước 3: Xử lý nhận thông báo trên iOS.** Có hai loại payload FCM đến iOS: _notification_ (thông báo hiển thị) và _data_ (dữ liệu ẩn).

- Với **notification message** (payload APNs thông thường): Nếu app đang foreground, bạn cần hiện thông báo thủ công (ví dụ hiện `UNNotification` hoặc popup) vì mặc định iOS sẽ không tự hiển thị khi app mở. Bạn làm điều này trong phương thức `userNotificationCenter(_:didReceive:withCompletionHandler:)` của `UNUserNotificationCenterDelegate`. Nếu app background/đã tắt, iOS sẽ hiển thị thông báo trên màn hình khóa/trung tâm thông báo như bình thường. Khi người dùng nhấn vào, app sẽ mở và bạn có thể xử lý hành động trong `userNotificationCenter(_:didReceiveResponse:)`.
    
- Với **data message** (thông điệp ngầm chứa `content-available=1`): iOS sẽ cố gắng đánh thức app (chạy ở nền) và gọi delegate `application(_:didReceiveRemoteNotification:fetchCompletionHandler:)`. Tại đây, bạn nhận được dictionary dữ liệu tùy chỉnh và có khoảng thời gian ngắn để xử lý (tải dữ liệu mới, cập nhật UI nền,...). **Lưu ý:** Nếu người dùng _force quit_ (vuốt tắt hẳn) ứng dụng, iOS **sẽ không nhận data message** cho đến khi ứng dụng mở lại. Vì vậy, nếu cần đảm bảo người dùng luôn thấy thông báo, hãy gửi kèm dạng notification.
    
- Với **tin nhắn hỗn hợp (notification + data)**: iOS sẽ hiển thị phần notification cho người dùng (khi app nền/tắt), còn phần data kèm theo thì chỉ được chuyển đến app khi người dùng nhấn vào thông báo (thông qua `userInfo` trong phương thức mở app). Nếu app đang mở, bạn sẽ nhận cả notification (có thể qua delegate) và data (qua callback fetch ở trên) cùng lúc. Do đó, kiểu này được dùng khi bạn cần vừa hiện thông báo cho người dùng, vừa gửi kèm dữ liệu bổ sung để xử lý sau khi người dùng tương tác.
    

Tóm lại, việc tích hợp FCM trên iOS đòi hỏi cấu hình APNs đúng và triển khai các delegate của **UserNotifications** và **Firebase Messaging** phù hợp. Khi cấu hình xong, bạn có thể gửi thử thông báo từ Firebase console để kiểm tra. Nếu thiết bị iOS không nhận được thông báo, hãy kiểm tra lại khóa APNs đã upload và đảm bảo thiết bị có kết nối internet, không chặn cổng APNs (5223, 443).

## 4. Các _best practices_ khi sử dụng FCM

Để sử dụng FCM hiệu quả, an toàn và mang lại trải nghiệm tốt, nhà phát triển nên tuân theo một số **best practices** sau:

### Quản lý và lưu trữ token đăng ký

- **Lưu trữ token phía server và cập nhật kịp thời:** Mỗi app cài trên một thiết bị có một **FCM registration token** duy nhất. Token này có thể thay đổi trong một số trường hợp (cài đặt lại app, xóa data, thiết bị mới), vì vậy ứng dụng **phải cập nhật token định kỳ**. Khi app chạy lần đầu hoặc khi `onNewToken` được gọi (Android) / `didReceiveRegistrationToken` (iOS), hãy gửi token mới lên server và thay thế token cũ. Server của bạn nên lưu bản đồ giữa người dùng và token hiện hành của thiết bị họ dùng.
    
- **Xóa token không còn hợp lệ:** Theo thời gian có thể phát sinh token “mồ côi” (người dùng gỡ app nhưng server vẫn lưu token). FCM có cơ chế phản hồi lỗi khi bạn gửi đến token không hợp lệ hoặc không còn sử dụng – ví dụ trả về lỗi `"messaging/registration-token-not-registered"` nếu token đó không còn tồn tại. Bạn nên xử lý các phản hồi lỗi từ FCM và xóa những token lỗi thời khỏi cơ sở dữ liệu của mình. Việc này giúp giảm lãng phí tài nguyên và số lượng thông báo gửi vào ngõ cụt, đồng thời giữ cho thống kê tỷ lệ gửi chính xác.
    
- **Tránh trùng lặp và lạm dụng token:** Một người dùng có thể có nhiều thiết bị (mỗi thiết bị một token). Nếu ứng dụng của bạn cho phép đăng nhập nhiều thiết bị, nên lưu tất cả token của user đó và có chiến lược gửi phù hợp (gửi tới tất cả hoặc chọn lọc). Hạn chế việc một thiết bị nhận cùng một thông báo nhiều lần qua các token khác nhau (trong trường hợp token cũ chưa xóa). Ngoài ra, đảm bảo an ninh nơi lưu token: token FCM khá ngẫu nhiên và không dễ đoán, nhưng bạn vẫn cần bảo mật cơ sở dữ liệu chứa token (vd. nếu dùng Firebase Firestore, hãy đặt quy tắc bảo mật chặt chẽ để chỉ chủ nhân token mới ghi/đọc được).
    

### Xử lý thông báo khi ứng dụng bị kill (tắt)

- Ở trạng thái **ứng dụng không chạy** (bị kill hoàn toàn), hành vi nhận thông báo tùy thuộc vào loại thông báo và nền tảng: với Android, **notification message** vẫn sẽ được FCM hiển thị qua hệ thống (vì đã được chuyển cho hệ điều hành Android quản lý) – người dùng vẫn thấy thông báo ngay cả khi app bị tắt. Tuy nhiên, **data message** sẽ **không tự động khởi chạy ứng dụng nếu app đã bị force stop**. Thông điệp data có thể được đợi cho đến khi app mở lại mới xử lý. Vì vậy, **best practice** ở đây là: nếu thông báo quan trọng cần đảm bảo người dùng thấy, hãy gửi nó dưới dạng **notification hoặc mixed message** thay vì chỉ data thuần. Điều này đặc biệt quan trọng trên iOS – nếu người dùng vuốt tắt app, iOS sẽ chặn luôn cả thông báo nền (data) cho đến khi app mở lại. Do đó, hãy cân nhắc gửi kèm thông tin dưới dạng notification để ít nhất nội dung chính được hiển thị. Nếu cần truyền dữ liệu ngầm khi app tắt, có thể xem xét sử dụng **Push Notification Service Extension** (iOS) hoặc kế hoạch đồng bộ khi app khởi động.
    
- **Kiểm tra các trường hợp edge-case:** Hãy thử nghiệm việc gửi thông báo khi app ở các trạng thái: foreground, background, và đã bị kill, trên cả Android và iOS. Đảm bảo rằng các thông báo quan trọng vẫn đến được người dùng. Trên Android, tránh phụ thuộc vào việc tự khởi chạy service nền nếu app không hoạt động – từ Android 8 trở lên có nhiều giới hạn khiến app bị kill sẽ không nhận được background service (trừ khi là high-priority FCM trong một khoảng ngắn). Giải pháp là luôn hiển thị một thông báo (để OS xử lý) càng sớm càng tốt khi nhận tin FCM, thay vì làm nhiều việc nền dẫn tới trễ hoặc bị hệ thống chặn.
    

### Thiết kế **Notification Channel** hợp lý (Android)

- **Phân loại thông báo theo kênh:** Trên Android Oreo (8.0)+, nên tạo các **notification channel** cho từng nhóm loại thông báo trong ứng dụng (ví dụ: _Thông báo khuyến mãi_, _Tin nhắn cá nhân_, _Cập nhật hệ thống_). Việc này cho phép bạn đặt mức độ quan trọng, âm thanh, đèn LED… riêng cho từng loại. Người dùng cũng có thể vào cài đặt ứng dụng để tắt/mở hoặc tùy chỉnh từng kênh thông báo thay vì phải tắt tất cả.
    
- **Cài đặt kênh mặc định cho FCM:** FCM cung cấp sẵn một kênh mặc định nếu bạn không chỉ định, nhưng **best practice** là tự tạo kênh mặc định của riêng bạn. Tạo kênh qua `NotificationManager.createNotificationChannel()` khi ứng dụng khởi động (nếu chưa tồn tại), sau đó thêm `<meta-data android:name="com.google.firebase.messaging.default_notification_channel_id" android:value="YOUR_CHANNEL_ID"/>` vào AndroidManifest. Như vậy, mọi thông báo từ FCM không chỉ định kênh sẽ dùng kênh mặc định của bạn với cấu hình mong muốn (ví dụ âm thanh nhỏ, không rung cho thông báo marketing; rung và ưu tiên cao cho tin nhắn chat…).
    
- **Nội dung thông báo rõ ràng, cô đọng:** Dù kênh nào, hãy đảm bảo **tiêu đề và nội dung** thông báo ngắn gọn, đúng trọng tâm. Người dùng thường lướt qua nhiều thông báo, thông điệp của bạn cần dễ hiểu trong một hai dòng đầu. Tránh nhồi nhét quá nhiều văn bản hoặc thông tin không cần thiết.
    

### Tránh spam người dùng

- **Gửi thông báo có chọn lọc và ý nghĩa:** Chỉ gửi khi thực sự cần thiết và có giá trị với người dùng. Tránh tình trạng spam – gửi quá nhiều thông báo không liên quan sẽ gây phiền hà. Google khuyến cáo mọi thông báo nên “có cấu trúc tốt, có thể hành động được, kịp thời và phù hợp” với nhu cầu người dùng. Nếu lạm dụng, người dùng **có thể tắt quyền nhận thông báo hoặc gỡ cài đặt ứng dụng**.
    
- **Tôn trọng thời gian và tần suất:** Không gửi thông báo vào giờ nhạy cảm (đêm khuya) trừ khi rất quan trọng. Hạn chế số lượng thông báo gửi ra mỗi ngày cho mỗi người dùng – hãy cân nhắc gộp nội dung hoặc sử dụng kỹ thuật _collapsible messages_ của FCM (những thông điệp cùng loại chỉ giữ lại cái mới nhất khi người dùng chưa online) để tránh “bão thông báo”. FCM hỗ trợ `collapse_key` cho Android để hợp nhất các thông báo không quan trọng, ví dụ chỉ giữ thông báo tỉ số bóng đá mới nhất thay vì mọi cập nhật từng phút.
    
- **Phân nhóm đối tượng và cá nhân hóa:** Sử dụng tính năng **Firebase Analytics/Audiences** hoặc **topics** để chỉ gửi thông báo đến nhóm người dùng quan tâm. Thông báo được cá nhân hóa (ví dụ chào tên người dùng, gợi ý dựa trên hoạt động của họ) sẽ ít gây khó chịu hơn thông báo chung chung gửi đại trà. Firebase Cloud Messaging có thể kết hợp với **Firebase Predictions** hoặc **Remote Config** để chỉ gửi thông báo đến người dùng có khả năng tương tác cao, tránh làm phiền những người dùng không quan tâm.
    

### Tối ưu hiệu suất & pin khi dùng FCM

- **Sử dụng đúng mức ưu tiên cho thông báo:** FCM cho phép đặt **priority** cho thông báo (đặc biệt là với data message). **Nguyên tắc chung**: chỉ dùng **High priority** cho những thông báo thực sự **cấp bách, nhạy thời gian** (như tin nhắn chat đến, thông báo giao dịch tài chính, cảnh báo quan trọng) – những trường hợp cần đánh thức thiết bị ngay cả khi đang Doze để người dùng thấy ngay. Còn lại, hãy dùng **Normal priority** cho các thông báo không yêu cầu phản hồi tức thì (vd. cập nhật nội dung nền, thông báo xã hội không khẩn). Việc này giúp tiết kiệm pin vì ở chế độ Doze, thông báo thường sẽ đợi đến khoảng thời gian cho phép mới gửi, thay vì đánh thức thiết bị liên tục. Lưu ý rằng nếu bạn lạm dụng high priority cho những việc không quan trọng, Android có thể **hạn chế**: trên Android 9+, hệ thống áp dụng **App Standby Bucket** – nếu app ở nhóm ít được dùng, bạn chỉ được gửi một số lượng giới hạn tin nhắn ưu tiên cao mỗi ngày, quá mức đó FCM sẽ tự động hạ xuống normal. Do đó, **đừng “dùng quota” high priority một cách lãng phí** cho những việc không xứng đáng, nếu không khi cần gửi thông báo khẩn, tin của bạn có thể bị trì hoãn hoặc bị chặn.
    
- **Hiển thị thông báo ngay, tránh xử lý nền lâu:** Khi nhận được `onMessageReceived`, **best practice** là tạo và hiện thông báo cho người dùng **càng sớm càng tốt** trước khi làm việc khác. Không nên thực hiện các tác vụ mạng hoặc tính toán nặng trong lúc xử lý FCM vì có thể làm chậm hoặc ngăn việc hiển thị thông báo. Nếu cần tải thêm dữ liệu (ví dụ tải ảnh cho notification), hãy **hiển thị một thông báo cơ bản trước** rồi dùng WorkManager/JobScheduler để tải và cập nhật thông báo sau. Trên Android, nếu xử lý quá 10 giây hoặc khởi chạy background service không phù hợp, hệ thống có thể **giết tiến trình** do vi phạm giới hạn (nhất là với normal priority message). Sử dụng WorkManager cho tác vụ nền dài sẽ an toàn hơn so với khởi tạo `Thread` hoặc `Service` thủ công.
    
- **Tận dụng payload để đính kèm dữ liệu cần thiết:** Để giảm thiểu việc phải mở kết nối mạng khi nhận tin, hãy cố gắng đưa những dữ liệu cần cho việc hiển thị thông báo vào thẳng payload FCM. Ví dụ: thay vì gửi một data message chỉ chứa ID bài viết rồi app phải gọi API để lấy nội dung, hãy gửi sẵn tiêu đề hoặc một trích đoạn nội dung trong payload, nhờ vậy có thể hiện ngay thông báo mà không cần chờ tải thêm. Điều này vừa làm thông báo nhanh hơn, vừa tránh đánh thức radio mạng không cần thiết (tiết kiệm pin). Payload FCM giới hạn ~4KB, hãy sử dụng dung lượng này hợp lý. Nếu cần tải thêm nhiều dữ liệu (ví dụ hình ảnh lớn), cân nhắc thiết kế để tải sau khi người dùng mở app thay vì tải tất cả ở nền.
    

### Bảo mật khi sử dụng FCM

- **Bảo vệ khóa máy chủ (Server key):** Tuyệt đối **không đưa server key FCM vào ứng dụng client**. Server key phải được giữ bí mật trên server của bạn hoặc môi trường cloud function. Nếu để lộ khóa này, kẻ xấu có thể lợi dụng để gửi thông báo giả mạo đến người dùng của bạn. Trường hợp duy nhất server key nằm trong app là khi lập trình viên gửi FCM trực tiếp từ client (rất không nên) – như Reddit cảnh báo, điều này trao cả token thiết bị và server key cho client, tiềm ẩn nguy cơ nghiêm trọng. Hãy luôn thực hiện gửi thông báo từ một **trusted server environment** (server của bạn, hoặc Firebase Cloud Functions với quyền hạn thích hợp).
    
- **Xác thực người gửi và người nhận:** Nếu bạn cho phép gửi thông báo từ phía client (ví dụ user A gửi tin nhắn cho user B), hãy triển khai xác thực và kiểm tra trên server để đảm bảo người gửi có quyền và nội dung hợp lệ trước khi gọi FCM. Tránh tình trạng lạm dụng API gửi tin. Bạn có thể sử dụng **Firebase Authentication** để xác định danh tính người gửi trên server.
    
- **Mã hóa nội dung nhạy cảm:** Mặc dù FCM truyền dữ liệu qua kết nối bảo mật, nhưng để tăng cường bảo mật, bạn có thể mã hóa end-to-end phần tải trọng nhạy cảm. Chỉ giải mã trên ứng dụng client sau khi nhận. Điều này đề phòng trường hợp thông báo có thể bị lộ nếu ai có được quyền truy cập FCM token và server key (rất hiếm, nhưng không thừa). Google gợi ý có thể dùng các thư viện mã hóa cho FCM payload (như **Capillary** chẳng hạn).
    
- **Tuân thủ chính sách nền tảng:** Đảm bảo tuân thủ các quy định của Google và Apple về nội dung thông báo. Ví dụ: không gửi nội dung spam, gây hiểu lầm; trên iOS, không lạm dụng thông báo nền cho mục đích không phù hợp (Apple có thể từ chối ứng dụng nếu sử dụng push sai mục đích). Đồng thời, tôn trọng quyền riêng tư – không đính kèm dữ liệu cá nhân nhạy cảm trong thông báo hiển thị công khai, trừ khi cần thiết và có sự đồng ý của người dùng.
    

## 5. Các loại message trong FCM: Notification, Data và Mixed

FCM cung cấp hai loại tin nhắn cơ bản là **Notification message** và **Data message**, ngoài ra có thể kết hợp cả hai loại (gọi không chính thức là **Mixed message**). Mỗi loại có đặc điểm, ưu nhược điểm riêng và phù hợp với những trường hợp sử dụng khác nhau:

- **Notification Message:** Đây là tin nhắn mà payload chứa phần `notification` (ví dụ có `title` và `body`). Loại này do **hệ điều hành xử lý để hiển thị** thông báo tới người dùng. Trên Android và iOS, nếu app đang background hoặc bị tắt, hệ thống sẽ tự hiện thông báo ra thanh trạng thái/màn hình khóa. Notification message **đơn giản cho lập trình viên** vì không cần code xử lý nếu chỉ muốn hiển thị mặc định. Tuy nhiên, nhược điểm là **ít linh hoạt**: payload của nó chỉ bao gồm những trường cố định (tiêu đề, nội dung, icon...), không mang dữ liệu tùy biến nhiều, và khi app đang mở foreground thì thông báo sẽ **không tự hiện** – bạn cần xử lý nếu muốn hiển thị trong app. Notification message mặc định được FCM đặt ưu tiên **cao** trên Android (high priority), giúp đảm bảo hệ thống chuyển nó nhanh tới người dùng. Loại này thích hợp cho **các thông báo đơn thuần tới người dùng** (như nhắc lịch, thông báo cập nhật, tin marketing) khi bạn không cần app phải xử lý gì phức tạp ngoài việc hiển thị cho người dùng biết.
    
- **Data Message:** Đây là tin nhắn chỉ chứa payload trong trường `data` (các cặp key-value tùy ý do bạn định nghĩa). Data message **luôn được chuyển thẳng đến ứng dụng (qua callback)** để app tự xử lý, **không tự hiện thông báo UI**. Ưu điểm là tính linh hoạt rất cao: bạn có thể gửi dữ liệu tùy ý (JSON nhỏ gọn) và khi nhận được, app có thể làm bất cứ việc gì – lưu database, hiện thông báo tùy biến, hoặc bỏ qua… Nhờ đó data message phù hợp cho các tình huống cần **xử lý ngầm hoặc logic phức tạp** trên client (ví dụ đồng bộ dữ liệu, thực hiện tính toán, điều hướng người dùng...). Ngoài ra, data message có thể được dùng cho _silent push_ (như đã đề cập ở trên). Nhược điểm của nó là bạn **phải viết code xử lý** trên mọi nền tảng, và nếu code chưa tốt có thể bỏ lỡ thông báo. Đặc biệt, **trên iOS, data message yêu cầu app được phép chạy nền**; nếu app bị force quit, data message sẽ không được nhận cho đến khi app mở lại (Apple hạn chế điều này vì lý do pin/bảo mật). Trên Android, data message có thể bị trì hoãn nếu ở chế độ Doze trừ khi được đặt ưu tiên cao. Mặc định data message có priority "normal", bạn có thể tự đặt "high" nếu cần gấp. Nên dùng **Data message** khi bạn cần toàn quyền xử lý thông tin nhận được, ví dụ: thông báo tin chat mới (sau đó app sẽ dựng notification theo kiểu riêng, như hiển thị sender avatar), hoặc cập nhật đếm số badge, v.v., những thứ mà notification message khuôn mẫu không làm được.
    
- **Mixed (Notification + Data) Message:** Đây không hẳn là một loại tách biệt do FCM định nghĩa, mà là tin nhắn có chứa cả hai phần: `notification` _và_ `data` trong payload. Khi gửi dạng này, FCM sẽ tận dụng ưu điểm của cả hai: nếu app đang background/tắt, **hệ thống sẽ hiển thị phần notification** cho người dùng ngay (đảm bảo thông báo không bị bỏ lỡ), **đồng thời** chuyển phần `data` cho ứng dụng. Lưu ý trên Android, khi app background, FCM **sẽ không gọi** `onMessageReceived` ngay cho mixed message; thay vào đó, phần `data` được đưa vào `Intent` mở Activity khi người dùng nhấn vào thông báo. Còn trên iOS, phần `data` trong thông báo APNs + data chỉ đến với app nếu người dùng mở thông báo (iOS không có cơ chế đẩy data ngầm kèm notification trừ khi dùng **mutable content** trong Notification Service Extension để xử lý trước khi hiển thị). Vì thế, **ưu điểm** của mixed message là **đảm bảo thông báo luôn hiển thị** nhờ payload notification, đồng thời cung cấp **data tùy biến** để app sử dụng khi người dùng tương tác hoặc khi app đang mở. Nó thường được dùng trong trường hợp: bạn muốn hiển thị một thông báo tương đối đơn giản nhưng vẫn cần truyền kèm dữ liệu cho ứng dụng – ví dụ thông báo "Bạn có tin nhắn mới từ A", kèm data là `conversation_id` để khi người dùng nhấn thì app mở đúng cuộc trò chuyện. **Nhược điểm** của mixed message là logic xử lý phức tạp hơn chút: bạn phải lập trình để lấy data khi app mở notification, và phòng trường hợp người dùng bỏ qua không nhấn thì data đó có thể không bao giờ được xử lý. Ngoài ra payload sẽ lớn hơn chút so với chỉ notification hoặc data. Hãy dùng dạng **notification + data** khi **cả việc hiển thị lẫn dữ liệu ngầm đều quan trọng** – ví dụ thông báo giao dịch tài chính: hiện "Giao dịch X thành công" cho người dùng, đồng thời gửi kèm mã giao dịch, số liệu... để app ghi vào lịch sử giao dịch ngay.
    

Để tóm tắt sự khác biệt, bảng sau liệt kê cách xử lý của mỗi loại tin nhắn FCM trong các trạng thái ứng dụng:

|**Loại tin nhắn**|**Khi app foreground**|**Khi app background/tắt**|**Ưu / Nhược điểm chính**|**Trường hợp nên dùng**|
|---|---|---|---|---|
|**Notification message**|Gọi callback cho app (Android nhận trong `onMessageReceived` như data, iOS qua delegate nếu set, mặc định không tự hiện). App có thể tùy quyết định hiển thị.|Hệ thống **tự hiển thị thông báo** trên tray/màn hình khóa. App nhận biết khi người dùng nhấn (mở app).|+ Đơn giản, không cần code nhiều để hiện UI; OS đảm nhiệm hiển thị.+ Luôn hiện được cho user khi app không chạy.- Không kèm dữ liệu tùy ý (chỉ tiêu đề, text cố định).- App foreground phải tự xử lý nếu muốn hiển thị.|Thông báo chung cho người dùng, nội dung tĩnh (marketing, nhắc nhở sự kiện). Khi muốn đảm bảo user thấy mà không cần tương tác app phức tạp.|
|**Data message**|Gọi ngay `onMessageReceived` (Android) hoặc hàm delegate fetch (iOS) – **app tự xử lý toàn bộ**. Không UI trừ khi app tạo.|Android: FCM vẫn chuyển tới app qua service (nếu chưa bị kill). iOS: chỉ nhận nếu app chạy nền (nếu app bị kill sẽ không nhận). Không có UI nếu app không tạo.|+ Linh hoạt: chứa key-value tùy ý, app định nghĩa hành vi.+ Cho phép cập nhật ngầm (silent) không làm phiền user.- Phải viết code xử lý mọi thứ.- Nếu app không chạy (đặc biệt iOS bị kill), message có thể bị trễ hoặc mất cho đến khi mở lại.|Các tình huống cần logic tùy chỉnh: đồng bộ dữ liệu nền, cập nhật UI thầm lặng, hoặc thông báo đòi hỏi app xử lý (chat, thông tin nhạy cảm) trước khi hiển thị.|
|**Mixed message** _(notification + data)_|App nhận callback giống data message (có đủ cả `notification` và `data` khi foreground). Có thể hiện UI ngay hoặc tùy chỉnh.|Hệ thống **hiển thị notification** cho user (như notification msg). **Data kèm theo được chuyển đến app khi** user chạm vào thông báo (Android đưa vào Intent extras, iOS qua userInfo khi app mở).|+ Kết hợp ưu điểm: user luôn thấy thông báo, đồng thời có data để app dùng.+ Phù hợp để dẫn dắt người dùng: nhấn thông báo app biết phải làm gì (dựa trên data).- App background không nhận được data ngay cho đến khi user tương tác, phải thiết kế xử lý phù hợp.- Payload lớn hơn chút, cấu trúc phức tạp hơn.|Khi cần hiển thị thông tin ngay cho user **và** gửi kèm dữ liệu để app xử lý tiếp. Ví dụ: thông báo có nội dung + dữ liệu điều hướng (ID nội dung, link…) phục vụ khi user mở app từ thông báo.|

_Nguồn tham khảo: Firebase Docs về [Notification vs Data messages] và [Android background delivery]._

**Khi nào chọn loại nào?** Tóm lại, hãy dùng **notification message** nếu bạn chỉ cần thông báo đơn giản đến người dùng và không cần app làm gì đặc biệt (OS sẽ lo hiển thị, như gửi tin khuyến mãi hoặc thông báo tin tức mới). Dùng **data message** nếu bạn cần kiểm soát hoàn toàn và xử lý logic trong app (ví dụ cập nhật cơ sở dữ liệu, tạo thông báo tùy chỉnh có hình ảnh, hoặc các tác vụ nền khác). Còn **kết hợp cả hai** khi bạn muốn vừa có thông báo nổi cho người dùng vừa có dữ liệu cho ứng dụng (thường dùng trong các trường hợp phức tạp như thông báo dẫn đến hành động trong app). Việc hiểu rõ ưu nhược điểm từng loại giúp bạn thiết kế hệ thống push notification tối ưu về trải nghiệm và hiệu năng.

**Tài liệu tham khảo:** Kiến trúc và hướng dẫn FCM từ Firebase Docs, bài viết chia sẻ kinh nghiệm FCM, cùng các hướng dẫn best practices từ Google. Các khía cạnh chi tiết hơn (ví dụ: giới hạn tải trọng, chính sách nền tảng) có thể được tìm thấy trong tài liệu chính thức của Firebase và hướng dẫn của Android/iOS. Chúc bạn triển khai FCM thành công và hiệu quả!

**Nguồn trích dẫn:**

1. Firebase Docs – _FCM Architectural Overview_
    
2. Firebase Docs – _FCM Architectural Overview_ (tiếp)
    
3. Viblo Blog – _Giải đáp FAQ về Firebase Cloud Messaging_
    
4. Reddit – _Firebase messaging device token security_ (giải thích về server key & device token)
    
5. Firebase Docs – _Firebase Cloud Messaging (Tổng quan)_
    
6. Firebase Docs – _Receive messages in Android_ (behavior foreground/background)
    
7. Android Dev Blog – _Notifying your users with FCM_ (best practices tránh spam, ưu tiên)
    
8. StackOverflow – _FCM data message not coming when app is closed in iOS_
    
9. Firebase Docs – _Manage FCM registration tokens_ (quản lý token thiết bị)
    
10. Firebase Docs – _Set up FCM on Android_ (config manifest, notification channel)
    
11. Firebase Docs – _Set up FCM on iOS_ (upload APNs key)
    
12. Android Dev Blog – _Notifying users with FCM_ (FCM message types và battery)