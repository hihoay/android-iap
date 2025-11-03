package taymay.firebase.utils

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings


fun getFirebaseRemoteData(
    isTesting: Boolean, key: String, defaultValue: String, callback: (content: String) -> Unit
) {

    val mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
    val configSettings = FirebaseRemoteConfigSettings.Builder()
        .setMinimumFetchIntervalInSeconds(if (isTesting) 0 else 3600).build()
    mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings)
    mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener { task ->
        var value = mFirebaseRemoteConfig.getString(key)
        callback(value)
    }.addOnFailureListener {
        callback(defaultValue)
    }
}