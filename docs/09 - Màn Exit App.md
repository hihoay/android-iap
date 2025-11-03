- Cú pháp mở màn hình quảng cáo thoát App
```kotlin
hihoayAskExitApp("Exit_Bottom_Medium",1500){
    it.finishAffinity() // đóng ActivityExit
    finishAffinity()// // đóng Activity Hiện tại
}
```
