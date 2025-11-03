- Kiểm tra sẽ hiển thị màn cài đặt ngôn ngữ:

```kotlin
hihoayIsWillShowLanguageSetup() // true nếu màn cài ngôn ngữ chưa hiển thị lần nào, false là đã cài ngôn ngữ
```

- Kiểm tra và mở màn hình chọn cài Language lần đầu tiên:

```kotlin
hihoaySetupAppLanguage("adNameTop","adNameBottom")
```

- Mở màn hinh cài đặt ngôn ngữ, nếu cài đặt thành công thì sẽ restart lại App

```kotlin
hihoayOpenSetLanguageActivity( "au:language_top_small", "language_bottom_medium")
```

## Triển khai
- Đăng ký nghe ở phần onCreate

```kotlin
        HihoayInAppMessage.receiver(this) { event ->
            elog("onAppMessage", event.key, event.datas.map { "${it.key}: ${it.value}" })

            if (event.key == "hihoay:setup_language") {
                Log.d("sdjkfhasdkjf", "Come here :((")

                MyCache.putStringValueByName(
                    this@SplashActivity,
                    Constant.LANGUAGE_CODE,
                    event.datas.getValue("langCode").toString()
                )
                if (event.datas.getValue("isModified") as Boolean) {
                    showIntro(this@SplashActivity, ShowType.Once)
                } else {
                    if (!MainActivity.isAlive) {
                        goHomeScreen()
                    }
                }
            }
        }
````

- Gọi setup khi User thay đổi ngôn ngữ thì sẽ bắn về sự kiện đã lắng nghe ở trên

```kotlin
  hihoaySetupAppLanguage(
            "Language_Top_Small", "Language_Bottom_Medium"
        )
```
## Tích hợp vào màn hình Settings:


- Màn Setting:

```kotlin
   layoutLanguages.setSafeOnClickListener {
                HihoayInAppMessage.receiver(this@SettingActivity) { event ->
                    if (event.key == "hihoay:setup_language") {
                        if (event.datas.getValue("isModified") as Boolean) {
                            val intent = Intent(this@SettingActivity, SplashActivity::class.java)
                            intent.putExtra("isSetting", true)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                        HihoayInAppMessage.unregister(this@SettingActivity)
                    }
                }
                hihoayOpenSetLanguageActivity("Language_Top_Small", "Language_Bottom_Medium")
            }
```



## Thay đổi theme ở màn Language:


- Tối:
```xml

    <!--Màu của thanh bar và status bar-->
    <color name="color_bg_language_title">#1D1E22</color>
    <!--Màu nền của item language-->
    <color name="color_item_language">#1D1E22</color>
    <!--Màu nền của nút language-->
    <color name="color_btn_save_language">#0066FF</color>
    <!--Màu nền của language-->
    <color name="color_bg_language">#000000</color>
    <!--Màu của text language-->
    <color name="color_language_text">#ffffff</color>
    <!--Màu của nút back-->
    <color name="color_btn_back">#FFFFFF</color>
    <!--Màu của checkbox language off-->
    <color name="color_select_language_off">#3E434C</color>
    <!--Màu của checkbox language on-->
    <color name="color_select_language_on">#0066FF</color>
```
- Sáng:
```xml
Mã màu Theme Light (Xanh biển): 
    <!--Màu của thanh bar và status bar-->
<color name="color_bg_language_title">#F4F5F9</color>
    <!--Màu nền của item language-->
<color name="color_item_language">#F4F5F9</color>
    <!--Màu nền của nút language-->
<color name="color_btn_save_language">#0066FF </color>
    <!--Màu nền của language-->
<color name="color_bg_language">#ffffff</color>
    <!--Màu của text language-->
<color name="color_language_text">#000000</color>
    <!--Màu của nút back-->
<color name="color_btn_back">#000000</color>
    <!--Màu của checkbox language off-->
<color name="color_select_language_off">#BCBCBC</color>
    <!--Màu của checkbox language on-->
<color name="color_select_language_on">#0066FF</color>

```
