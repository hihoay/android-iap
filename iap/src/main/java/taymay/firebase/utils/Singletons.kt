package taymay.firebase.utils

import com.google.android.ump.ConsentInformation
import taymay.frameworks.utils.AppEnvironment
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicBoolean

object Singletons {
    var userGeoIP: UserGeoIP? = null
    var REMOTE_VIEW_API = "${AppEnvironment.REMOTE_SERVICE_URL}api/v1/remotes/view/"
    var isMobileAdsInitializeCalled = AtomicBoolean(false)

}
