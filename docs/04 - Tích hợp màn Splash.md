# Tham khảo `SplashActivity.kt`

```kotlin
package com.demo.example.os.ads

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.demo.example.os.colorcall.HomeActivity
import com.taymay.app.service.activities.HihoayActivity
import com.taymay.app.service.activities.HihoaySplashActivity
import com.taymay.app.service.activities.ShowType
import com.taymay.app.service.enums.AdState
import com.taymay.app.service.enums.LoadingType
import com.taymay.app.service.objecs.ByDialog
import com.taymay.app.service.objecs.HihoayInAppMessage
import com.taymay.app.service.objecs.hihoayGetDataConfigBoolean
import com.taymay.app.service.objecs.hihoayLoadAndShowAdWait
import com.taymay.app.service.objecs.hihoayLoadAndShowAdWaitFor
import com.taymay.app.service.objecs.hihoaySetupAdVersion
import com.taymay.app.service.objecs.hihoaySetupAppLanguage
import com.taymay.app.service.objecs.hihoaySetupUMP
import com.taymay.app.service.objecs.hihoayShowIntroFromViews
import com.taymay.app.service.objecs.onAdClose
import com.taymay.app.service.views.hihoaySplashViewNoAnim
import com.taymay.call.theme.R
import com.taymay.call.theme.databinding.IntroCustom01Binding
import com.taymay.call.theme.databinding.IntroCustom02Binding
import com.taymay.call.theme.databinding.IntroCustom03Binding

class SplashActivity : HihoaySplashActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            hihoaySplashViewNoAnim(
                getString(R.string.app_name),
                R.mipmap.ic_launcher,
                layoutInflater
            ).root
        )
        HihoayInAppMessage.receiver(this) { event ->
            if (event.key == "hihoay:setup_language") {
                if (event.datas.getValue("isModified") as Boolean) showIntro(this, ShowType.Once)
                else {
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
        hihoaySetupUMP { b, consentInformation ->
            hihoaySetupAdVersion { // Nên bọc lần đầu khi tải quảng cáo
                hihoayLoadAndShowAdWaitFor(
                    "Splash_OpenHome_Full", LoadingType.None, LinearLayout(this)
                ) { myAd, dialogLoading, activityLoading ->

                    if (myAd != null) {
                        when (myAd.adState) { // check trạng thái của quảng cáo hiện tại
                            AdState.Close, AdState.Done -> openLanguageSettings()
                            AdState.Show -> setContentView(LinearLayout(this))
                            AdState.Timeout, AdState.Error -> openLanguageSettings()
                            else -> {}
                        }
                    }
                }
            }
        }
    }

    companion object {
        fun showIntro(
            context: HihoayActivity, showType: ShowType
        ) {
            if (hihoayGetDataConfigBoolean("show_intro", true)) context.hihoayShowIntroFromViews(
                showType,
                "Intro_Top_Small",
                "Intro_Bottom_Small",
                0,
                IntroCustom01Binding.inflate(LayoutInflater.from(context)).root,
                IntroCustom02Binding.inflate(LayoutInflater.from(context)).root,
                IntroCustom03Binding.inflate(LayoutInflater.from(context)).root,
            )
        }
    }

    private fun openLanguageSettings() {
        hihoaySetupAppLanguage("Language_Top_Small", "Language_Bottom_Small")
    }

    private fun exitSplashOpenHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()

    }
}
```
