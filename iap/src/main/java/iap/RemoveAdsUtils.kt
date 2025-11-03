package iap

import android.content.Context
import iap.entity.DataWrappers
import iap.listener.PurchaseServiceListener
import iap.listener.SubscriptionServiceListener


object RemoveAdsUtils {
    fun Context.checkRemoveAd(key: String, isSub: Boolean, callback : (Boolean) -> Unit) {
        var subscriptionServiceListener: SubscriptionServiceListener = object : SubscriptionServiceListener {

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
            override fun onPricesUpdated(iapKeyPrices: kotlin.collections.Map<kotlin.String, DataWrappers.ProductDetails>) {

            }
            override fun onProductPurchased(purchaseInfo: DataWrappers.PurchaseInfo) {
                callback.invoke(false)
            }
            override fun onProductRestored(purchaseInfo: DataWrappers.PurchaseInfo) {
                callback.invoke(true)
            }
            override fun onPurchaseFailed(
                purchaseInfo: DataWrappers.PurchaseInfo?,
                billingResponseCode: kotlin.Int?
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