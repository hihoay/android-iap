package taymay.iap.frameworks

import android.content.Context
import taymay.iap.frameworks.entity.DataWrappers
import taymay.iap.frameworks.listener.PurchaseServiceListener
import taymay.iap.frameworks.listener.SubscriptionServiceListener


object RemoveAdsUtils {
    fun Context.checkRemoveAd(key: String, isSub: Boolean, callback : (Boolean) -> Unit) {
        var subscriptionServiceListener: SubscriptionServiceListener = object :
            SubscriptionServiceListener {

            override fun onPricesUpdated(iapKeyPrices: Map<String, DataWrappers.ProductDetails>) {
                iapKeyPrices.forEach { key, value ->
                    val listOffer = value.offers?.filter { it.id != null } ?: emptyList()
                    if (listOffer.isEmpty()) {
                        callback.invoke(true)
                    } else {
                        callback.invoke(false)
                    }
                }
            }

            override fun onPurchaseFailed(
                purchaseInfo: DataWrappers.PurchaseInfo?,
                billingResponseCode: Int?
            ) {
            }
            override fun onSubscriptionRestored(purchaseInfo: DataWrappers.PurchaseInfo) {
                callback.invoke(true)
            }
            override fun onSubscriptionPurchased(purchaseInfo: DataWrappers.PurchaseInfo) {
            }
        }
        var purchaseServiceListener: PurchaseServiceListener = object : PurchaseServiceListener {
            override fun onPricesUpdated(iapKeyPrices: Map<String, DataWrappers.ProductDetails>) {

            }
            override fun onProductPurchased(purchaseInfo: DataWrappers.PurchaseInfo) {
                callback.invoke(false)
            }
            override fun onProductRestored(purchaseInfo: DataWrappers.PurchaseInfo) {
                callback.invoke(true)
            }
            override fun onPurchaseFailed(
                purchaseInfo: DataWrappers.PurchaseInfo?,
                billingResponseCode: Int?
            ) {
            }
        }
        val subs = mutableListOf<String>()
        val nonCons = mutableListOf<String>()
        if (isSub) subs.add(key)
        else nonCons.add(key)
        val iapConnector = IapConnector(this, nonCons, emptyList(), subs)
        if (isSub) iapConnector.addSubscriptionListener(subscriptionServiceListener)
        else iapConnector.addPurchaseListener(purchaseServiceListener)
    }
}