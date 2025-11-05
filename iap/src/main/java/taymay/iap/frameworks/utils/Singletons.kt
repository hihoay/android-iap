package taymay.iap.frameworks.utils

import taymay.frameworks.utils.AppEnvironment
import java.util.concurrent.atomic.AtomicBoolean

object Singletons {
    var userGeoIP: UserGeoIP? = null
    var REMOTE_VIEW_API = "${AppEnvironment.REMOTE_SERVICE_URL}api/v1/remotes/view/"

}
