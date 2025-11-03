- Lưu ý mặc định các App đều phải có giá trị `iap` đã cấu hình trong `Data Configs` để sử dụng được
  tính năng `IAP`.

- Sử dụng `Data Configs` để cấu hình iap với `key` là `iap` và cú pháp của giá trị
  là `"id_product_a,id_product_b,id_product_c,...."`, mỗi giá trị phân tách nhau bằng dấu `,` hỗ trợ
  cấu hình nhiều `product_id` khác nhau để hỗ trợ trong việc AB Test Giá, mặc định sẽ
  chọn `product_id` đầu tiên.
