## Kiểm tra Remove Ads trong lần đầu vào App

> Để kiểm tra xem người dùng đã mua gói Remove ads hay chưa trong lần đầu vào app  thông qua `RemoveAdsUtils`

```kotlin  
fun Context.checkRemoveAd(key: String, isSub: Boolean, callback : (Boolean) -> Unit)
```  

| Tên biến | Ví dụ                       | Mô tả                                                  |     |
| -------- | --------------------------- | ------------------------------------------------------ | --- |
| key      | "remove_ads", "sub_premium" | Là sku của sản phẩm cần thanh toán                     |     |
| isSub    | `true` or `false`           | True nếu như key là subscription                       |     |
| callback |                             | Callback đươc trả về khi người dùng đã thanh toán rồi. |     |

**Hướng dẫn sử dụng**  
**Kotlin**
```kotlin  
checkRemoveAd("remove_ads", false) { isSuccess ->  
		// Có thể kết hợp với adx service để lưu thông tin            MyCache.putBooleanValueByName(context, IS_PREMIUM, isSuccess)        
}  
```

**Java**
```
RemoveAdsUtils.INSTANCE.checkRemoveAd(this, "remove_ad", false, (isSuccess) -> { 
    // True nếu key đã đực thanh toán trước đó
    // Ngược lại là false
    return null;  
});
```
--- 
	## 💳 Mở màn hình thanh toán trong app
```kotlin  
fun Context.hihoayGoToIAPActivity(  
    listItemSubContent: List<ItemSubscriptionContent>,  
    key: String,  
    className: String,  
    title: String,  
    buttonTitle: String,  
    isSub: Boolean  
)
```  
**Tham số:**

| Tên biến           | Ví dụ                                                                                                                                          | Mô  tả                                                        |     |
| ------------------ | ---------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------- | --- |
| listItemSubContent | listOf(ItemSubscriptionContent( <br>    R.drawable.ic_doc,  <br>    "This is a content 1",  <br>    "This is a content 1 description"  <br>),) | List danh sách các đối tượng ItemSubscriptionContent          |     |
| key                | "remove_ads", "sub_premium"                                                                                                                    | Là sku của sản phẩm cần thanh toán                            |     |
| className          | SplashActivity::class.java.name                                                                                                                | Là tên class cần quay về khi người dùng thanh toán thành công |     |
| isSub              | `true` or `false`                                                                                                                              | True nếu như key là subscription                              |     |
| title              | "Unlock premium"                                                                                                                               | Title bạn muốn hiển thị                                       |     |
| buttonTitle        | "Continue"                                                                                                                                     | Nội dung text bên trong button                                |     |
**Hướng dẫn sử dụng**
```kotlin  
layoutRemoveAds.setOnClickListener {    
    val key = "remove_ad"    
    val listItemSubscriptionContent = listOf(    
        ItemSubscriptionContent(    
            R.drawable.ic_doc,    
            "This is a content 1",    
            "This is a content 1 description"    
        ),    
        ItemSubscriptionContent(    
            R.drawable.ic_pdf,    
            "This is a content 2",    
            "This is a content 2 description"    
        ),    
        ItemSubscriptionContent(    
            R.drawable.ic_pdf,    
            "This is a content 3",    
            "This is a content 3 description"    
        )    
    )    
    hihoayGoToIAPActivity(  
        listItemSubscriptionContent,  
        key,  
        MainActivityController::class.java.name,  
        isSub = isSubscription,  
        title = "Unlock Pro Version",  
        buttonTitle = "Continue"  
    )
}
```  

Trong màn hình đích như ở trên là Splash ta có thể lấy ra được người dùng thanh toán hay chưa thông qua:
```kotlin  
val isPremium = intent.getBooleanExtra("isRemoveAds", false)  
// Có thể kết hợp với adx service để lưu thông tin  
MyCache.putBooleanValueByName(context, IS_PREMIUM, isPremium)  
```  
**📝 Ghi chú**  
Có thể xem Log thông qua tag `SubscriptionActivity_Log`

---
## 🌟 Mở Màn Hình Premium

> Mở màn hình khi người dùng đã là Premium

```kotlin
fun Context.hihoayGoToPremiumActivity(
    listItemSubContent: List<ItemSubscriptionContent>,
    key: String,
    className: String,
    title: String,
    buttonTitle: String
)
```
**Tham số:**

| Tên biến             | Ví dụ                                                | Mô tả                                                 |
| -------------------- | ---------------------------------------------------- | ----------------------------------------------------- |
| `listItemSubContent` | `listOf(ItemSubscriptionContent(...))`               | Danh sách nội dung tính năng của gói IAP              |
| `key`                | `"remove_ads"` / `"sub_premium"`                     | SKU hoặc product ID của gói thanh toán                |
| `className`          | `MainActivity::class.java.name`                      | Tên màn hình sẽ quay về sau khi thanh toán thành công |
| `title`              | `"Unlock Pro Version"`                               | Tiêu đề hiển thị trên màn hình                        |
| `buttonTitle`        | `"Continue"`                                         | Text của nút xác nhận mua                             |

**Cách sử dụng:**
```kotlin
val key = "sub_premium"
val listItemSubscriptionContent = listOf(  
    ItemSubscriptionContent(  
        R.drawable.ic_doc,  
        "This is a content 1",  
        "This is a content 1 description"  
    ),  
    ItemSubscriptionContent(  
        R.drawable.ic_pdf,  
        "This is a content 2",  
        "This is a content 2 description"  
    ),  
    ItemSubscriptionContent(  
        R.drawable.ic_pdf,  
        "This is a content 3",  
        "This is a content 3 description"  
    )  
)
hihoayGoToPremiumActivity(  
    listItemSubscriptionContent,  
    key,  
    MainActivityController::class.java.name,  
    "You're a Pro User!",  
    "Continue"  
)
```