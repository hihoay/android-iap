package taymay.firebase.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import taymay.firebase.utils.Singletons.isMobileAdsInitializeCalled
import taymay.frameworks.utils.AppEnvironment.IS_TESTING

lateinit var consentInformation: ConsentInformation

fun Context.getConsentInformation(): ConsentInformation {
    if (!::consentInformation.isInitialized) consentInformation =
        UserMessagingPlatform.getConsentInformation(this)
    return consentInformation
}


fun Context.isCanRequestAds(): Boolean {
    return getConsentInformation().canRequestAds()
}


fun Context.isPrivacyOptionsRequired(): Boolean {
    return getConsentInformation().privacyOptionsRequirementStatus == ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED
}


fun Context.setupUMP(hashUMPTest: String, onInited: (isCanRequestAds: Boolean) -> Unit) {
    isMobileAdsInitializeCalled.set(false)
    var consentInformation = getConsentInformation()
    val debugSettings = ConsentDebugSettings.Builder(this)
        .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
        .addTestDeviceHashedId(hashUMPTest).build()
    var params =
        if (IS_TESTING) ConsentRequestParameters.Builder().setConsentDebugSettings(debugSettings)
            .build()
        else ConsentRequestParameters.Builder().build()

    consentInformation.requestConsentInfoUpdate(this as Activity, params, {
        UserMessagingPlatform.loadAndShowConsentFormIfRequired(
            this, ConsentForm.OnConsentFormDismissedListener { loadAndShowError ->
                loadAndShowError?.let {
                    String.format(
                        "%s: %s", it.errorCode, loadAndShowError.message
                    )
                }
                if (!isMobileAdsInitializeCalled.getAndSet(true)) {
                    onInited(isCanRequestAds())
                }
            })
    }, { requestConsentError ->
        elog(
            String.format(
                "%s: %s", requestConsentError.errorCode, requestConsentError.message
            )
        )
        if (!isMobileAdsInitializeCalled.getAndSet(true)) {
            elog("requestConsentError", isCanRequestAds())
            onInited(isCanRequestAds())
        }
    })

    if (!MyConnection.isOnline(this)) if (!isMobileAdsInitializeCalled.getAndSet(
            true
        )
    ) {
        elog("!taymayIsOnline", isCanRequestAds())
        onInited(isCanRequestAds())
    }

    if (isCanRequestAds()) if (!isMobileAdsInitializeCalled.getAndSet(true)) {
        elog("taymayIsCanRequestAds", isCanRequestAds())
        onInited(isCanRequestAds())
    }
}


fun Context.showPrivacyOptionsForm(
    clHome: Class<*>, callback: (Int, String) -> Unit
) {
    UserMessagingPlatform.showPrivacyOptionsForm(this as Activity) {
        elog("showPrivacyOptionsForm")
        it?.let { it1 -> callback(it1.errorCode, it.message) }
        var intent = Intent(this, clHome)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}