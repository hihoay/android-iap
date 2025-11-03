
Gói sử dụng `package com.hihoay.app.service`

- Hiển thị màn hình Intro/Tutorial thông qua đối tượng`IntroPager`:

- Màn hình được khỏi tạo thông qua 2 phương thức `showIntroFromPages()`, `showIntroFromViews()` sử
  dụng với các mục đích khác nhau.

- Cấu hình màu màn Intro, mặc định sẽ là dưới đây, nếu bạn muốn thay đổi thì thêm 1 thẻ tương ứng muốn thay đổi trong `app` ở tệp `colors.xml`:

```xml
    <color name="intro_theme_color">#03A9F4</color>
```

##### 1. `showIntroFromPages()` sử dụng các `IntroPager` để thiết lập các dữ liệu cho các `Page`

```kotlin
/**
  * Cho phép khởi tạo bằng các đối tượng `IntroPager`
  * isCanShowAd thể hiện đã show Intro và người dùng đã click Done thì có thể hiển thị được quảng cáo sau Intro trả về là true, còn nếu Intro đã hiển thị thì sẽ trả về false
  */
fun hihoayShowIntroFromPages(
          currentContext: Context,
          showType: ShowType, //tủy chọn hiển thị một lần hoặc nhiều lần
          ad_top_name: String,// tên vị trí quảng cáo
          ad_bottom_name: String,// Vị trí quảng cáo được sử dụng trong màn hình
          backgroundDrawable: Int, // background của màn hình
          vararg introPager: IntroPager // các IntroPager
      )
```

```kotlin
enum class ShowType {
    Once, Multi
}

class IntroPager(
    var isAnim: Boolean, // true là sử dụng animation thông qua file .json false là sử dụng thông qua ảnh từ drawable
    var image: Int,// true: R.raw.anim_tut_01 or false:  R.drawable.im_tut_1
    var title: String, // title
    var titleColor: Int, // Màu của Title , ex: R.color.colorTitle
    var body: String, // Mô tả
    var bodyColor: Int // Màu của mô tả, ex: R.color.colorBody
)
```

- Ví dụ `showIntroFromPages()`:

```kotlin
 Context.hihoayShowIntroFromPages(
                    context,
                    ShowType.Once,
                    "au:intro_top_small",
                    "au:intro_bottom_small",
                    R.drawable.bg_tut,
                    IntroPager(
                        false,
                        R.drawable.im_tut_1,
                        context.resources.getString(R.string.title_tut_1),
                        R.color.colorTu1,
                        context.resources.getString(R.string.body_tut_1),
                        R.color.colorBody1
                    ),
                    IntroPager(
                        false,
                        R.drawable.im_tut_2,
                        context.resources.getString(R.string.title_tut_2),
                        R.color.colorTut2,
                        context.resources.getString(R.string.body_tut_2),
                        R.color.colorBody2
                    ),
                    IntroPager(
                        false,
                        R.drawable.im_tut_3,
                        context.resources.getString(R.string.title_tut_3),
                        R.color.colorTut3,
                        context.resources.getString(R.string.body_tut_3),
                        R.color.colorBody3
                    )
                ) 

```

##### 2. `hihoayShowIntroFromViews()` sử dụng các `View` để thiết lập các dữ liệu `Page`

```kotlin

enum class ShowType {
    Once, Multi
}

fun hihoayShowIntroFromViews(
    currentContext: Context,
    showType: ShowType, //tủy chọn hiển thị một lần hoặc nhiều lần
    ad_top_name: String,
    ad_bottom_name: String,
    backgroundDrawable: Int, // background của màn hình
    vararg view: View, // Các View của mỗi Page

) 
```

- Ví dụ `hihoayShowIntroFromViews`:

```kotlin

fun getImageView(currentContext: Context, resId: Int): ImageView {
          var imv: ImageView = ImageView(context)
          imv.setImageResource(resId)
          imv.scaleType = ImageView.ScaleType.CENTER_INSIDE
          return imv
}

Context.hihoayShowIntroFromViews(
    context,
    ShowType.Once,
    "au:intro_top_small",
    "au:intro_bottom_small",
    R.drawable.bg_tut,
    getImageView(context, R.drawable.im_tut_1),
    getImageView(context, R.drawable.im_tut_2),
    getImageView(context, R.drawable.im_tut_3)
)
```
