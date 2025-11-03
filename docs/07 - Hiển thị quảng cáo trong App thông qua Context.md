
- Các tiện ích sử dụng ở những gói sau, kiểm tra để tránh bị nhầm lẫn:

```kotlin

```
# Sử dụng `hihoayLoadAndShowAdWait()` tải và hiển thị quảng cáo nhanh gọn.

- Phương thức `hihoayLoadAndShowAdWait()` để yêu cầu và hiển thị quảng cáo cho các định dạng quảng cáo khác nhau và nhiều cách khác nhau:

```kotlin
fun Context.hihoayLoadAndShowAdWait(adName: String, adLoadAndShowConfig: AdLoadAndShowConfig)
```
- Tham số:
	- `adName` : tên vị trí quảng cáo
	- `adLoadAndShowConfig` :  Đối tượng cấu hình cách tải quảng cáo và hiển thị quảng cáo:
		- Các thể hiện: 
		-  `ByActivity` : Sử dụng `Activity` để tải và hiển thị quảng cáo, thường sử dụng cho quảng cáo `Interstitial`, `Reward` , `OpenAd`.
		-  `ByLayout` : Sử dụng để tải và hiển thị quảng cáo trong `View` thường sử dụng cho quảng cáo có định dạng `Native` `Banner`.
		-  `ByNone` : Sử dụng để tải và hiển thị quảng cáo không có giao diện và không hiển thị lên `View`, thường sử dụng cho quảng cáo ở màn `Splash` và đã có màn hình loading sẵn rồi.
		-  `ByDialog` : Sử dụng `Dialog` để tải và hiển thị quảng cáo, thường sử dụng cho quảng cáo `Interstitial`, `Reward` , `OpenAd`.
- Phương thức tĩnh: `onAdClose{}`: 
	- Khi sử dụng `hihoayLoadAndShowAdWait` hay `hihoayLoadAndShowAdWaitFor` nếu sử dụng `Dialog` hay `Activity` để loading thì sau khi tải -> hiển thị quảng -> đóng quảng cáo thì `Dialog`, `Activity` sẽ không tự động đóng bạn phải đóng bằng tay nhưng nếu sử dụng `onAdClose{}` phương thức sẽ tự động đóng màn Loading sau khi quảng cáo đóng và `callback` sẽ được gọi khi đã đóng Loading và Quảng cáo.
```kotlin 
fun onAdClose(onClose: () -> Unit): (MyAd?, AlertDialog?, AdLoadingActivity?) -> Unit
```

- Ví dụ:

```kotlin

// Tải quảng cáo hiển thị trạng thái Loading bằng Activity và hiển thị quảng cáo lên sau khi đã tải thành công
hihoayLoadAndShowAdWait("ad_name", ByActivity(onAdClose { 
// Sau khi đóng quảng cáo sẽ gọi vào đây
}))

// Tải quảng cáo hiển thị trạng thái Loading bằng Dialog và hiển thị quảng cáo lên sau khi đã tải thành công
hihoayLoadAndShowAdWait("ad_name", ByDialog(onAdClose { 
// Sau khi đóng quảng cáo sẽ gọi vào đây
}))

// Tải quảng cáo không cần hiển thị Loading và hiển thị quảng cáo tải được vào View ở đây mình thường dùng là LinearLayout
hihoayLoadAndShowAdWait("ad_name", ByLayout(activityBinding.layoutAd))

// Tải quảng cáo không cần hiển thị Loading và hiển thị quảng cáo tải được 
hihoayLoadAndShowAdWait("ad_name", ByNone(onAdClose { 
// Sau khi đóng quảng cáo sẽ gọi vào đây
}))

```




# Sử dụng `hihoayLoadAndShowAdWaitFor()` tải và hiển thị quảng cáo theo dõi thêm về trạng thái của quảng cáo.

- Sử dụng nếu bạn muốn kiểm tra các trạng thái từ việc tải và hiển thị quảng cáo:

```kotlin

fun Context.hihoayLoadAndShowAdWaitFor(  
    adName: String,  
    loadingType: LoadingType,  
    adLayoutContainer: LinearLayout,  
    processFeedback: (myAd: MyAd?, dialogLoading: androidx.appcompat.app.AlertDialog?, activityLoading: AdLoadingActivity?) -> Unit)
```

-  `adName`: tên quảng cáo

-  `adViewContainer`: là LinearLayout chứa quảng cáo hoặc  = LinearLayout(this) khi gọi để load và hiển thị dụng quảng cáo Interstitial hoặc Reward

-  `loadingType`: Là kiểu Loading có 3 giá trị:
	-  `LoadingType.None`: Là không hiển thị Loading khi tải quảng cáo để sử dụng cho trường hợp muốn hiển thị quảng cáo Banner,Native
	- `LoadingType.Dialog:` Là hiển thị Loading dạng Dialog khi tải quảng cáo để sử dụng.
	-  `LoadingType.Activity`: Là hiển thị Loading dạng Activity khi tải quảng cáo để sử dụng.


  - Ví dụ sử dụng cho quảng cáo màn `Splash`

```kotlin

    hihoayLoadAndShowAdWaitFor(
        "adName", LoadingType.None, LinearLayout(this)
    ) { myAd, dialogLoading, activityLoading ->
        when (myAd!!.adState) { // check trạng thái của quảng cáo
            AdState.Close, AdState.Done -> goToMain()
            AdState.Show -> setContentView(LinearLayout(this))
            AdState.Timeout, AdState.Error -> goToMain()
            else -> {}
        }
    } 
```

## Các trạng thái của quảng cáo

> `Init` : quảng cáo được khởi tạo

> `Loaded` : quảng cáo đã tải thành công

> `Timeout` : quảng cáo tải quá thời gian cho phép

> `Error` : quảng cáo bị lỗi

> `Show` : quảng cáo đã hiển thị

> `Close` : quảng cáo đã đóng

> `Done` : quảng cáo đã hoàn thành (Dành cho quảng cáo Reward)

> `AdClick` : quảng cáo được click

- Có thể kiểm tra tải được dữ liệu cấu hình quảng cáo thành công hay chưa:

```kotlin
var isAdsReady = isAdVersionReady()
```

### Mẫu khai báo ViewContainer chứa quảng cáo

```xml

<LinearLayout
  android:id="@+id/ll_top_banner"
  android:layout_width="match_parent"
  android:layout_height="wrap_content"
  android:orientation="vertical"
  tools:layout_height="10dp"
/>
```
# Chỉnh sửa Theme cho quảng cáo Native

- Cấu hình màu của định dạng quảng cáo Native Ad, mặc định sẽ là dưới đây, nếu bạn muốn thay đổi thì thêm 1 thẻ tương ứng muốn thay đổi trong `app` ở tệp `colors.xml`:

```xml

    <color name="native_ad_background">#1d222d</color>
    <color name="native_ad_stroke">#E91E63</color>
    <color name="native_ad_title">#7b58fd</color>
    <color name="native_ad_body">#e5e5e5</color>
    <color name="native_ad_button_background">#7b58fd</color>
    <color name="native_ad_button_text">#FFFFFF</color>
    <color name="native_ad_ad_tag_text">#FFFFFF</color>
    <color name="native_ad_ad_tag_background">#7b58fd</color>
```


- Nếu muốn gọi `HihoayActivity` trong `Fragment` sử dụng :

```kotlin
     requireActivity().getHihoayActivity { b, hihoayActivity ->
            hihoayActivity?.let { 
                // có thể gọi hihoayActivity ở đây và sử dụng các phương thức của HihoayActivity
                hihoayActivity.hihoayLoadAndShowAdWaitFor(
                    "adName",
                    LoadingType.None,
                    binding.llAdTop,
                    hihoayActivity.onAdClose { })
            }
        }
```
### Khởi tạo quảng cáo quay trờ lại `App (Open Ad)` sử dụng trong Activity

```kotlin
hihoaySetupReturnAppAd( "adName")
```

###  Kiểm tra xem quảng cáo đã được tải trước chưa:

```kotlin
hihoayIsAdLoaded("ad_name_here")
```

###  Tải trước quảng cáo

```kotlin
loadAdsToCache( "ad_name_1","ad_name_2","ad_name_3",...)
```


### Các ví dụ

- Sử dụng `Dialog` tải quảng cáo và sau khi tải xong thì tự động hiển thị quảng cáo chèn giữa và thực hiện
  công việc sau đó. (ví dụ: Mở một `Activity` mới)

```kotlin
    // Cách 1:
    hihoayLoadAndShowAdWaitFor(
            "Splash_OpenHome_Full", LoadingType.Dialog, LinearLayout(this)
        ) { myAd, dialogLoading, activityLoading ->
            if (dialogLoading?.isShowing == true) dialogLoading.dismiss()
            when (myAd!!.adState) { // check trạng thái của quảng cáo hiện tại
                AdState.Close, AdState.Done, AdState.Timeout, AdState.Error -> {
                    // Thực hiện lệnh tiếp theo sau khi quảng cáo đã đóng
                }
                else -> {}
            }
        }

    //Cách 2: tối ưu ngắn gọn, nên dùng Cách 2 nếu không cần can thiệp nhiều
	hihoayLoadAndShowAdWait("Splash_OpenHome_Full", ByDialog(onAdClose {  
	// Thực hiện lệnh tiếp theo sau khi quảng cáo đã đóng
	  
	}))
 
```

- Sử dụng `AdInterActivity` tải quảng cáo và sau khi tải xong thì tự động hiển thị quảng cáo chèn giữa, đóng `AdInterActivity` thực hiện
  công việc sau đó. (ví dụ: Mở một `Activity` mới) - Trường hợp này không nên sử dụng với `Navigation` vì sẽ gây ra lỗi do khác `Context`:

```kotlin
    // Cách 1:
    hihoayLoadAndShowAdWaitFor(
            "adName", LoadingType.Activity, LinearLayout(this)
        ) { myAd, dialogLoading, activityLoading ->
            if (!activityLoading!!.isDestroyed) activityLoading.finish()
            when (myAd!!.adState) { // check trạng thái của quảng cáo hiện tại
                AdState.Close, AdState.Done, AdState.Timeout, AdState.Error -> {
                    // Thực hiện lệnh tiếp theo sau khi quảng cáo đã đóng
                }

                else -> {}
            }
        }
    //Cách 2: tối ưu ngắn gọn, nên dùng Cách 2 nếu không cần can thiệp nhiều
	hihoayLoadAndShowAdWait("Splash_OpenHome_Full", ByActivity(onAdClose {  
	// Thực hiện lệnh tiếp theo sau khi quảng cáo đã đóng
	  
	}))
  
```

- Bật/tắt quảng cáo chèn giữa có thể hiện hoặc không hiện lên một màn hình: (lưu ý sau khi set
  là `False` thì bạn nhớ set lại là `True` sau đó để quảng cáo có thể tiếp tục hiện)

```kotlin
hihoaySetCanShowAd(true/false)
```
