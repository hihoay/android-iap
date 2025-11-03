package taymay.firebase.utils

import android.content.Context
import android.os.Bundle
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics

fun Map<String, Any>.toBundle(): Bundle {
    val bundle = Bundle()
    for ((key, value) in this) {
        when (value) {
            null -> continue // ✅ Bỏ qua nếu value null
            is Int -> bundle.putInt(key, value)
            is Long -> bundle.putLong(key, value)
            is Float -> bundle.putFloat(key, value)
            is Double -> bundle.putDouble(key, value)
            is Boolean -> bundle.putBoolean(key, value)
            is String -> bundle.putString(key, value)
            is Bundle -> bundle.putBundle(key, value)
            is Array<*> -> bundle.putSerializable(key, value)
            is java.io.Serializable -> bundle.putSerializable(key, value)
            else -> throw IllegalArgumentException("Unsupported type for key '$key': ${value::class.java}")
        }
    }
    return bundle
}

fun Context.screenTracking(
    screenName: String,
    screenClass: String,
    callback: (screenName: String, screenClass: String) -> Unit = { n, c -> }
) {
    val firebaseAnalytics = FirebaseAnalytics.getInstance(this)
    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundleOf().apply {
        putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        putString(FirebaseAnalytics.Param.SCREEN_CLASS, screenClass)
    })
    callback(screenName, screenClass)
}


fun Context.eventTracking(
    event: String,
    params: Map<String, Any> = mapOf(),
    callback: (event: String, params: Map<String, Any>) -> Unit = { e, p -> }
) {
    try {
        FirebaseAnalytics.getInstance(this).logEvent(event, params.toBundle())
    } catch (e: Exception) {
        e.message?.let { elog("Firebase logEvent  error", it) }
    }
    callback(event, params)
}

