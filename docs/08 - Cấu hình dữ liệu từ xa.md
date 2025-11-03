
- Các thông tin cấu hình được lưu định dạng `json` và được lấy từ `Remote Configs` với key
  là `data_configs` hoặc là từ tập tin trong thư mục `assets` có tên tệp `data.json`

- Tệp `ads.json` có dạng

```json
[
  {
    "ad_id": "ca-app-pub-8474106102524491/8458557130",
    "ad_name": "splash",
    "ad_network": "admob",
    "ad_format": "interstitial",
    "ad_enable": true,
    "ad_index": 1,
    "ad_ctr": 10,
    "ad_impressions": 100,
    "ad_distance": 10,
    "ad_timeout": 10,
    "ad_tag": "",
    "ad_version": "default",
    "app_version_name": "1.0.1",
    "app_id": "com.taymay.haircut",
    "ad_update": "3/26/2022 5:05:21",
    "ad_reload": false,
    "ad_mediation": "",
    "ad_note": "",
    "open_bid": ""
  },
  ...
]

```

- Tệp `data.json` có dạng

```json
[
  {
    "app_id": <str>,
    "bool_value": <bool>,
    "data_version": <str>,
    "double_value": <double>,
    "key": <str>,
    "long_value": <long>,
    "string_value": <str>
  },
    ...
]
```

```kotlin
data class DataConfig(
    var app_id: String = "",
    var bool_value: Boolean = false,
    var key: String = "",
    var data_version: String = "",
    var double_value: Double = 0.0,
    var long_value: Long = 0,
    var string_value: String = ""
)
```

- Đối tượng `DataConfig` để cấu hình các loại dữ liệu khác nhau như `String`, `Boolean`
  , `Double` `Long` với các `key` khác nhau và được cấu hình dự trên các `data_version` để tiện
  trong quá trình AB Test các tính năng trong App

- Các phương thức sử dụng

```kotlin

// lấy giá trị String từ 1 key
var dataString = hihoayGetDataConfigString("<tên key>", "<giá trị mặc định>")

// lấy giá trị Boolean từ 1 key
var dataBoolean =  hihoayGetDataConfigBoolean("<tên key>", <giá trị mặc định>)

// lấy giá trị Double từ 1 key
var dataDouble = hihoayGetDataConfigDouble("<tên key>", <giá trị mặc định>)

// lấy giá trị Long từ 1 key
var dataLong = hihoayGetDataConfigLong("<tên key>", <giá trị mặc định>)

```

- Hệ thống sẽ tùy chỉnh các `data_version` tự động trên server tuy nhiên khi `Debug` thì có thể tùy
  chỉnh lựa chọn các phiên bản để cho việc kiểm thử

  - Đối với `Data Configs` sử dụng thuộc tính `DATA_CONFIG_VERSION_DEFAULT` để thay
    đổi `data_version` cho phiên bản chạy. (Mặc định bản `release` sẽ tự động lấy các giá trị sẽ
    đổi thành `default`)
  - Đối với `Ads Configs` sử dụng thuộc tính `AD_CONFIG_VERSION_DEFAULT` để thay đổi `ad_version`
    cho phiên bản chạy. (Mặc định bản `release` sẽ tự động lấy các giá trị sẽ đổi thành `default`)

- Ví dụ muốn test data 2 phiên bản `default` và `A` bạn có thể thay đổi để test phiên bản với dữ
  liệu cấu hình phiên bản `A`:

```kotlin
DATA_CONFIG_VERSION_DEFAULT="A"
```

- Tương tự như `Ads Configs`:

```kotlin
AD_CONFIG_VERSION_DEFAULT="A"
```
