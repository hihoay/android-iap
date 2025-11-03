"**System Tray**" (khay hệ thống) là thuật ngữ mô tả **khu vực hiển thị thông báo hệ thống** của Android — nơi người dùng nhìn thấy các thông báo (notifications) đến từ ứng dụng như tin nhắn, cảnh báo, email, v.v.

---

## 📱 **System Tray trên Android là gì?**

Trên Android, **System Tray chính là khu vực Notification Bar** (Thanh thông báo) — phần kéo từ trên xuống trên màn hình điện thoại.

### 👇 Ví dụ minh họa:

Khi app nhận FCM và bạn chưa mở app:

- Nếu FCM sử dụng `notification` payload,
    
- → Android sẽ **hiển thị thông báo ngay trong System Tray**,
    
- → Người dùng có thể **vuốt xuống để xem, nhấn để mở app, hoặc vuốt để xóa**.
    

---

## ✅ Các đặc điểm chính:

|Đặc điểm|Mô tả|
|---|---|
|🎯 Vị trí|Nằm ở phía trên màn hình khi người dùng vuốt xuống|
|🧠 Do hệ điều hành xử lý|Nếu FCM sử dụng `notification` payload, Android hiển thị luôn mà không cần app xử lý|
|🔔 Tự động hiển thị|Dù app đang tắt hoặc bị kill, nếu có `notification`, hệ thống vẫn hiển thị|
|⚙️ Tùy chỉnh được|App có thể tùy chỉnh icon, tiêu đề, nội dung, hành động, v.v.|
|🔕 Có thể bị chặn|Người dùng có thể tắt thông báo của app trong Settings → Notifications|

---

## 🧪 Ví dụ gửi FCM với `notification`:

```json
{
  "to": "device_token",
  "notification": {
    "title": "New Message",
    "body": "You have a new message",
    "click_action": "OPEN_CHAT"
  },
  "data": {
    "chatId": "abc123"
  }
}
```

- 🔔 Android sẽ tự động hiển thị trong **System Tray**
    
- 📦 App có thể nhận được `data` trong `onMessageReceived`
    

---

## ✅ Khi nào cần hiểu rõ điều này?

|Tình huống|Điều cần biết|
|---|---|
|Muốn đảm bảo hiển thị notify dù app bị kill|Sử dụng `notification` payload|
|Muốn xử lý tùy biến hoàn toàn|Chỉ gửi `data` payload và tự hiện Notification trong `onMessageReceived()`|
|Gặp lỗi "Không thấy thông báo hiện"|Kiểm tra có đang dùng `data` mà chưa tự tạo `NotificationCompat.Builder`|

---

## 📌 Tóm lại:

> Trên Android, **System Tray** chính là khu vực **hiển thị thông báo mặc định của hệ điều hành**, thường thấy khi **vuốt từ trên xuống**. Khi bạn gửi FCM với `notification`, **Firebase sẽ tự hiển thị** nội dung đó trong **System Tray** — ngay cả khi app đang tắt hoặc bị kill.

---

Nếu bạn cần so sánh `notification` payload vs `data` payload hoặc muốn kiểm soát chi tiết cách hiển thị, mình có thể hướng dẫn chi tiết thêm.


---

Câu trả lời ngắn:

> ❌ **Không**, bạn **không thể gửi thông báo hiện qua System Tray** cho app nếu:

- Người dùng **chưa từng mở app lần nào sau khi cài đặt**, và
    
- **Không dùng FCM (Firebase Cloud Messaging)** hoặc dịch vụ push tương tự.
    

---

## 🔍 Giải thích chi tiết:

### ✅ 1. **System Tray** chỉ hiện Notification khi:

- App **đã được mở ít nhất một lần** ➜ để **khởi tạo các thành phần như Firebase SDK, đăng ký token, hoặc bật listener nền**.
    
- Hoặc hệ thống nhận được thông báo **qua FCM/APNs** vì:
    
    - **Firebase sẽ lấy FCM token cho thiết bị** khi app được mở và gửi lên server.
        
    - Server mới có token để gửi notification push.
        

---

### ⚠️ 2. Nếu bạn KHÔNG dùng FCM thì có thể bạn định làm một trong hai cách:

|Ý tưởng thay thế FCM|Có hoạt động khi app chưa mở?|Ghi chú|
|---|---|---|
|🔄 App tự kiểm tra server (polling)|❌ Không được nếu app chưa chạy|App phải chạy foreground hoặc background|
|🧪 AlarmManager / WorkManager|❌ Không kích hoạt khi app chưa khởi động|Android giới hạn các tác vụ nền nếu app chưa mở|
|📡 Sử dụng push của hãng (Huawei, Xiaomi Push...)|⚠️ Có thể được nếu đã tích hợp SDK và app được whitelist|Nhưng vẫn cần khởi tạo SDK và cấp quyền|
|📲 SMS hoặc Email|✅ Ngoài hệ thống Android, không hiện ở System Tray như notification|Không thay thế Notification được|

---

## ✅ Kết luận:

> 🔒 **Từ Android 10+**, vì lý do **bảo mật & quyền riêng tư**, app sẽ **không thể nhận bất kỳ push nào hoặc hiện Notification trong System Tray nếu chưa từng được khởi động**.

Muốn đảm bảo app có thể nhận được notification (và hiển thị trong System Tray), bạn cần:

### ✅ Cần làm:

1. Dùng **FCM** hoặc hệ thống push đáng tin cậy khác (Huawei Push Kit, Xiaomi Push, v.v.)
    
2. Khi app **mở lần đầu**, đăng ký và lấy FCM token ➜ gửi lên server.
    
3. Sau đó, server có thể gửi notification qua FCM, sẽ hiện trong **System Tray**, kể cả khi app bị kill.
    

---

## 📌 Gợi ý:

Nếu bạn muốn **kích thích người dùng mở app lần đầu**, có thể dùng:

- **Email/SMS** mời mở app
    
- **App store description mạnh hơn** để tăng khả năng mở sau khi cài
    

---

Nếu bạn cần ví dụ cấu hình tối giản FCM để hiện System Tray notification từ lần đầu, mình có thể giúp bạn viết chi tiết.

---

## ✅ Firebase Cloud Messaging (FCM) **có thể hiển thị notification trong System Tray dù app chưa mở lần nào**, **nhờ vào việc nó sử dụng hệ thống dịch vụ push đặc biệt của Android OS và Google Play Services**.

---

## 🧠 Vậy FCM **dựa trên cái gì?**

### 🔗 FCM sử dụng:

|Thành phần|Vai trò|
|---|---|
|**Google Play Services**|Là nền tảng hệ thống được cài sẵn trên hầu hết thiết bị Android có Google (trừ HUAWEI, Trung Quốc...)|
|**Google’s push service layer (GCM/FCM)**|Là dịch vụ luôn **chạy nền ở mức hệ thống** — ngay cả khi chưa mở app|
|**Cloud to Device Messaging (C2DM)** ➜ FCM|Kênh socket bí mật được Google duy trì giữa thiết bị và máy chủ Firebase|

---

## 📲 Cách FCM hoạt động:

1. Khi bạn **cài app**, dù **chưa mở app lần nào**, **Google Play Services vẫn có thể tự động đăng ký token FCM** (gọi là **lazy registration**).
    
2. Token này được liên kết với package name + device + Google account.
    
3. Khi server gửi notification payload tới token đó → Google Play Services nhận được → **hiển thị trực tiếp qua System Tray**.
    

---

## 📌 Vì sao FCM làm được điều này?

|Lý do|Mô tả|
|---|---|
|✅ **Google Play Services luôn chạy ngầm**|Nó có quyền hệ thống, không bị Android giới hạn như app thường|
|✅ **Socket kết nối với server Google luôn mở**|Cập nhật notification nhanh, không cần app khởi chạy|
|✅ **Notification được hiển thị bởi hệ thống, không cần app xử lý**|Với `notification` payload, hệ thống sẽ tự hiện mà không cần `onMessageReceived()`|

---

## ❌ Nếu bạn KHÔNG dùng FCM thì sao?

- Không có **push service mặc định**
    
- App sẽ không có kênh giao tiếp với server
    
- Bạn **không thể gửi push notification hiện lên System Tray** nếu app chưa từng chạy
    

---

## 🛡️ Còn Huawei/Xiaomi/OPPO thì sao?

Các hãng này có **push service riêng**:

|Hãng|Dịch vụ push|
|---|---|
|Huawei|Huawei Push Kit|
|Xiaomi|Mi Push|
|OPPO|HeyTap Push|
|Vivo|Vivo Push|

> Nhưng muốn dùng, bạn **phải tích hợp SDK riêng của từng hãng** và thường cần **whitelist** để tránh bị hệ điều hành chặn.

---

## ✅ Kết luận:

> **FCM có thể hiện thông báo trong System Tray dù app chưa mở lần nào** là vì:

- Nó dựa trên **Google Play Services**
    
- Hệ thống này có **quyền hệ thống, luôn chạy nền**
    
- **FCM giữ một kênh socket luôn kết nối với Google servers**
    
- **Notification được hiện bởi Android System**, không cần app chạy
    

---

Nếu bạn muốn thấy dòng logic chính xác khi debug hoặc kiểm tra logcat để xác nhận notification được system xử lý, mình có thể hướng dẫn bạn thêm.