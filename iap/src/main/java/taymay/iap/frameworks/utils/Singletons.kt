package taymay.iap.frameworks.utils

import taymay.frameworks.utils.AppEnvironment

object Singletons {
    var trackingFunction = fun(event: String, params: Map<String, String>) {}
}
