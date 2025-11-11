
# Config Cloud Messaging

## Bước 1: Truy cập Firebase Console

1.  Mở trình duyệt web và truy cập trang web Firebase Console: [https://console.firebase.google.com/](https://console.firebase.google.com/)
2. Chọn project Firebase muốn gửi Cloud Message đến

## Bước 2: Điều hướng đến Cloud Messaging

1.  Trong trang tổng quan của project, tìm và nhấp vào mục **Run**
2.  Trong menu thả xuống, chọn **Messaging**.

## Bước 3: Tạo chiến dịch thông báo mới

1.  Trên trang Cloud Messaging, bạn sẽ thấy danh sách các chiến dịch đã gửi (nếu có).
2.  Nhấp vào nút **Create your first campaign** (nếu bạn chưa gửi chiến dịch nào) hoặc nút **New campaign**.
3.  Chọn loại chiến dịch là **Notification**.

## Bước 4: Nhập nội dung thông báo

Trong bước này, bạn sẽ nhập thông tin cơ bản cho thông báo của mình:

1.  **Tiêu đề thông báo:** Nhập tiêu đề mà người dùng sẽ thấy trong thông báo đẩy.
2.  **Văn bản thông báo:** Nhập nội dung chính của thông báo.
3.  **(Tùy chọn) Hình ảnh thông báo:** Bạn có thể thêm một hình ảnh để hiển thị cùng với thông báo bằng cách nhập URL hình ảnh.
4.  **(Tùy chọn) Tùy chọn nâng cao:** Mở rộng phần này để tùy chỉnh thêm các thuộc tính như:
    * **Dữ liệu tùy chỉnh:** Thêm các cặp key-value để truyền dữ liệu tùy chỉnh đến ứng dụng của bạn. Ứng dụng có thể xử lý dữ liệu này khi nhận được thông báo.
    * **Âm thanh:** Chọn âm thanh thông báo tùy chỉnh (nếu đã tải lên).
    * **Màu sắc:** Đặt màu sắc cho biểu tượng và văn bản thông báo (tùy thuộc vào thiết bị và hệ điều hành).
    * **Hành động nút:** Thêm các nút hành động mà người dùng có thể nhấp vào trong thông báo (tùy thuộc vào nền tảng).

## Bước 5: Nhắm mục tiêu thông báo

Trong bước này, bạn sẽ xác định đối tượng người dùng mà bạn muốn gửi thông báo đến:

1.  **Ứng dụng người dùng:** Gửi thông báo đến tất cả các phiên bản ứng dụng đã đăng ký với project Firebase của bạn.
2.  **Phân khúc người dùng:** Nhắm mục tiêu dựa trên các phân khúc người dùng đã được định nghĩa trong Firebase Analytics (yêu cầu tích hợp Analytics).
3.  **Chủ đề:** Gửi thông báo đến các thiết bị đã đăng ký một chủ đề cụ thể.
4.  **Mã thông báo FCM:** Gửi thông báo đến một thiết bị cụ thể bằng FCM registration token của nó. Cần có token này từ ứng dụng của người dùng.

Chọn phương pháp nhắm mục tiêu phù hợp với nhu cầu .

## Bước 6: Lên lịch thông báo (tùy chọn)

1.  Có thể chọn gửi thông báo **ngay bây giờ** hoặc **lên lịch** để gửi vào một thời điểm cụ thể trong tương lai.
2.  Nếu chọn lên lịch, hãy chọn ngày và giờ mong muốn.

## Bước 7: Tùy chọn nâng cao (tùy chọn)

Trong bước này, Có thể cấu hình thêm các tùy chọn nâng cao cho thông báo của mình:

1.  **Thời gian tồn tại (TTL):** Chỉ định khoảng thời gian tối đa mà FCM sẽ cố gắng gửi thông báo nếu thiết bị ngoại tuyến.
2.  **Độ ưu tiên:** Đặt độ ưu tiên của thông báo (Cao hoặc Thường). Thông báo có độ ưu tiên cao có thể được gửi ngay cả khi thiết bị đang ở chế độ Doze (Android).
3.  **Tùy chỉnh nền tảng:** Bạn có thể tùy chỉnh các tùy chọn riêng cho Android và iOS nếu cần.

## Bước 8: Xem lại và gửi

1.  Xem lại tất cả các cấu hình của thông báo đã thiết lập.
2.  Đảm bảo rằng tiêu đề, nội dung và đối tượng nhắm mục tiêu là chính xác.
3.  Nhấp vào nút **Xem lại** và sau đó nhấp vào nút **Gửi** để gửi thông báo (hoặc lên lịch nếu bạn đã chọn tùy chọn đó).

## Theo dõi chiến dịch

Sau khi gửi chiến dịch, Có thể theo dõi hiệu suất của nó trong trang Cloud Messaging, bao gồm số lượng tin nhắn đã gửi, đã mở và các tương tác khác (nếu có).


# Hướng dẫn tạo chiến dịch In-App Messaging trên Firebase

Firebase In-App Messaging cho phép bạn gửi các thông báo hiển thị trực tiếp bên trong ứng dụng của bạn khi người dùng đang tích cực sử dụng nó. Hướng dẫn này sẽ giúp bạn tạo chiến dịch In-App Messaging trên Firebase Console.

## Bước 1: Truy cập Firebase Console

1.  Mở trình duyệt web và truy cập trang web Firebase Console: [https://console.firebase.google.com/](https://console.firebase.google.com/)
2. Chọn project Firebase muốn tạo chiến dịch In-App Messaging.

## Bước 2: Điều hướng đến In-App Messaging

1.  Trong trang tổng quan của project, tìm và nhấp vào mục **Run**
2.  Trong menu thả xuống, chọn **Messaging**.

## Bước 3: Tạo chiến dịch mới

1.  Trên trang In-App Messaging, bạn sẽ thấy danh sách các chiến dịch đã tạo (nếu có).
2.  Nhấp vào nút **Tạo chiến dịch**.

## Bước 4: Chọn đối tượng mục tiêu

Trong bước này, bạn sẽ xác định những người dùng nào sẽ nhìn thấy thông báo in-app này:

1.  **Ứng dụng người dùng:** Gửi đến tất cả người dùng ứng dụng của bạn.
2.  **Phân khúc người dùng:** Nhắm mục tiêu dựa trên các phân khúc người dùng đã được định nghĩa trong Firebase Analytics (yêu cầu tích hợp Analytics). Bạn có thể tạo các phân khúc dựa trên hành vi, thuộc tính người dùng, v.v.
3.  **Test trên thiết bị:** Cho phép bạn nhắm mục tiêu một thiết bị cụ thể bằng Firebase Installation ID (FID) để kiểm tra thông báo trước khi triển khai rộng rãi.

Chọn đối tượng mục tiêu phù hợp với mục đích của chiến dịch. Nhấp vào **Tiếp tục**.

## Bước 5: Thiết kế thông báo

Trong bước này, bạn sẽ tạo giao diện cho thông báo in-app của mình:

1.  **Chọn kiểu hiển thị:**
    * **Modal:** Một hộp thoại hiển thị ở giữa màn hình, làm mờ nội dung bên dưới.
    * **Banner trên cùng/dưới cùng:** Một banner nhỏ hiển thị ở đầu hoặc cuối màn hình.
    * **Toàn màn hình:** Một thông báo chiếm toàn bộ màn hình.
    * **Chỉ hình ảnh:** Chỉ hiển thị một hình ảnh.
2.  **Tùy chỉnh giao diện:** Tùy thuộc vào kiểu hiển thị bạn chọn, bạn sẽ có các tùy chọn tùy chỉnh khác nhau:
    * **Tiêu đề:** Nhập tiêu đề cho thông báo.
    * **Nội dung:** Nhập nội dung chính của thông báo.
    * **Hình ảnh:** Tải lên hoặc nhập URL hình ảnh để hiển thị.
    * **Màu sắc:** Chọn màu nền, màu chữ, màu nút.
    * **Nút:** Thêm một hoặc hai nút hành động với văn bản và hành động (ví dụ: mở một liên kết, điều hướng đến một màn hình).
    * **Hình dạng nút:** Tùy chỉnh bo tròn góc của nút.
3.  **Xem trước:** Firebase Console sẽ hiển thị bản xem trước của thông báo trên các kích thước màn hình khác nhau.

Sau khi thiết kế xong, nhấp vào **Tiếp tục**.

## Bước 6: Đặt Trigger (Điều kiện kích hoạt)

Đây là bước quan trọng để xác định khi nào thông báo sẽ hiển thị cho người dùng:

1.  Nhấp vào **Chọn sự kiện**.
2.  Chọn một trong các trigger sau:
    * **App launch:** Thông báo hiển thị khi ứng dụng được mở. Bạn có thể tùy chỉnh số lần khởi chạy trước khi hiển thị.
    * **On foreground:** Thông báo hiển thị khi ứng dụng chuyển lên foreground.
    * **Sự kiện tùy chỉnh:** Chọn một sự kiện Firebase Analytics mà bạn đã log trong ứng dụng của mình. Thông báo sẽ hiển thị khi sự kiện này xảy ra. Đây là cách mạnh mẽ nhất để nhắm mục tiêu dựa trên hành vi người dùng.
    * **Thời gian trong ứng dụng:** Thông báo hiển thị sau một khoảng thời gian nhất định người dùng ở trong ứng dụng.
    * **Cập nhật ứng dụng:** Thông báo hiển thị sau khi người dùng cập nhật ứng dụng lên phiên bản mới.
3.  **(Tùy chọn) Thêm điều kiện:** Có thể thêm các điều kiện bổ sung dựa trên thuộc tính người dùng hoặc các sự kiện Analytics khác để nhắm mục tiêu chính xác hơn.

Chọn trigger phù hợp với mục đích của chiến dịch. Nhấp vào **Tiếp tục**.

## Bước 7: Lên lịch chiến dịch (tùy chọn)

1.  Có thể chọn **Gửi ngay bây giờ** hoặc **Lên lịch** để chiến dịch bắt đầu vào một thời điểm cụ thể.
2.  Nếu bạn chọn lên lịch, hãy chọn thời gian bắt đầu và thời gian kết thúc (tùy chọn) cho chiến dịch.

Nhấp vào **Tiếp tục**.

## Bước 8: Đặt sự kiện chuyển đổi (tùy chọn)

1. Có thể chọn một sự kiện Firebase Analytics làm sự kiện chuyển đổi. Điều này cho phép bạn theo dõi xem người dùng có thực hiện hành động mong muốn sau khi xem thông báo in-app hay không.

Nhấp vào **Tiếp tục**.

## Bước 9: Xem lại và kích hoạt

1.  Xem lại tất cả các cấu hình của chiến dịch.
2.  Đảm bảo rằng đối tượng, thiết kế, trigger và lịch trình là chính xác.
3.  Nhấp vào nút **Xem lại** và sau đó nhấp vào nút **Kích hoạt chiến dịch** để bắt đầu hiển thị thông báo cho người dùng.

## Theo dõi chiến dịch

Sau khi chiến dịch được kích hoạt, có thể theo dõi hiệu suất của nó trong trang In-App Messaging, bao gồm số lần hiển thị, số lần nhấp và tỷ lệ chuyển đổi (nếu bạn đã thiết lập sự kiện chuyển đổi).
