- `HihoayInAppMessage`: Sử dụng đối tượng này để trao đổi dữ liệu hoặc sự kiện trong ứng dụng khi Có nhiều Context mà không sử dụng được Callback:
  - Với Event là đối tượng `InAppMessage` chứ `key` là string và `datas` là map hay dict để chứa nhiều dữ liệu của sự kiện.
 
  - Đăng ký lắng nghe sự kiện:
 ```kotlin
      HihoayInAppMessage.receiver(this) { event ->
       
      }
```
  - Hủy lắng nghe sự kiện:
   ```kotlin
       HihoayInAppMessage.unregister(this)
  ```
 - Gửi sự kiện:

  ```kotlin
        HihoayInAppMessage.send(
            this, InAppMessage(
                "hihoay:intro_screen", datas = mutableMapOf(
                    ("isNext" to false), ("isDone" to true)
                )
            )
        )
  ```
- Ví dụ ở màn Splash sẽ có sự kiện đóng màn Language và Intro:

  ```kotlin
        HihoayInAppMessage.receiver(this) { event ->
            if (event.key == "hihoay:setup_language") {
                if (event.datas.getValue("isModified") as Boolean) showIntro(this, ShowType.Once)
                else{
                    HihoayInAppMessage.unregister(this)
                    exitSplashOpenHome()
                }
            }
            if (event.key == "hihoay:intro_screen") {
                if (event.datas.getValue("isDone") as Boolean) {
                    hihoayLoadAndShowAdWait(
                        "Intro_OpenHome_Full", ByDialog(onAdClose {
                            HihoayInAppMessage.unregister(this)
                            exitSplashOpenHome()
                        })
                    )
                }
            }
        }
  ```



  ## Các sự kiện mặc định:

  - Với `key` là  `hihoay:intro_screen` là sự kiện xuất phát từ màn *Intro* với datas là `isNext` và `isDone` đều là kiểu bool. `isNext` thể hiện người dùng đang bấm next ở Intro và `isDone` thể hiện người dùng đã bấm done ở màn Intro.
  - Với `key` là  `hihoay:setup_language` Là sự kiện xuất phát từ màn Cài đặt Ngôn ngữ với `datas` gồm có `isModified` kiểu bool và `langCode` kiển string có các giã trị là language code như là en,vi.... `isModified` thể hiện user có thay đổi ngôn ngữ ở màn Language không và trả về language code nếu đã thay đổi.
