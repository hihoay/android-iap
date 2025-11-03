




## Tracking màn hình:
- Gói sử dụng: `import com.hihoay.adx.service.objecs.hihoayFirebaseScreenTracking` và là 1 `Context`


```kotlin
override fun onResume() {
    hihoayFirebaseScreenTracking("main_activity",TestActivity::javaClass.name) // để Tracking màn hình ở phương thức `onResume` của `Activity` hoặc `Fragment`
    super.onResume()
}
```

## Tracking sự kiện:

- Gói sử dụng: `import com.hihoay.adx.service.objecs.hihoayFirebaseEventTracking` và là 1 `Context`

- Đối với các sự kiện không cần Data kèm theo là (key,value) hay là kiểu dict thì:
```kotlin
// mẫu thiết kế sự kiện dạng `<tên màn hình, hoặc giao diện>:<sự kiện>:<đối tượng nhận sự kiện>`
hihoayFirebaseEventTracking("home:open")
hihoayFirebaseEventTracking("dialog_choose:open")
hihoayFirebaseEventTracking("dialog_choose:tap:btn_yes")
hihoayFirebaseEventTracking("home:tap:ic_setting")
hihoayFirebaseEventTracking("home:tap:ic_setting")
```

- Đối với các sự kiện kèm theo Data (key,value) hay là kiểu dict thì:
```kotlin


hihoayFirebaseEventTracking(
    "video_downloaded:tap:ic_download", ("format" to "240p"), ("key" to "any"),....) // thêm nhiều cặp ở đây
)
```
