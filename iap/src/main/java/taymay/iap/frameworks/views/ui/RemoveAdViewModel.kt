package taymay.iap.frameworks.views.ui

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import taymay.iap.frameworks.IapConnector
import taymay.iap.frameworks.entity.DataWrappers
import taymay.iap.frameworks.listener.PurchaseServiceListener
import taymay.iap.frameworks.listener.SubscriptionServiceListener


data class RemoveAdState(
    val listOffer: List<DataWrappers.Offer> = emptyList(),
    val isShowDialog: Boolean = false,
)

class RemoveAdViewModel(private val isSubscription: Boolean, private val key: String) :
    ViewModel() {
    private lateinit var iapConnector: IapConnector
    private val TAG = "RemoveAdViewModel"

    private val _state = MutableStateFlow<RemoveAdState>(RemoveAdState())
    val state get() = _state

    var purchaseServiceListener: PurchaseServiceListener = object : PurchaseServiceListener {

        override fun onPricesUpdated(iapKeyPrices: Map<String, DataWrappers.ProductDetails>) {

            iapKeyPrices.forEach { key, value ->
                Log.e(TAG, "🔑 Key: $key\n${value.prettyToString()}")
                _state.value =
                    _state.value.copy(listOffer = value.offers ?: emptyList())
            }

        }

        override fun onProductPurchased(purchaseInfo: DataWrappers.PurchaseInfo) {
            _state.value = _state.value.copy(isShowDialog = true)
        }

        override fun onProductRestored(purchaseInfo: DataWrappers.PurchaseInfo) {
            Log.e(TAG, "onProductRestored")

        }

        override fun onPurchaseFailed(
            purchaseInfo: DataWrappers.PurchaseInfo?,
            billingResponseCode: Int?
        ) {
            Log.e(TAG, "onPurchaseFailed")
        }
    }

    var subscriptionServiceListener: SubscriptionServiceListener =
        object : SubscriptionServiceListener {

            override fun onPricesUpdated(iapKeyPrices: Map<String, DataWrappers.ProductDetails>) {
                iapKeyPrices.forEach { key, value ->
                    Log.e(TAG, "🔑 Key: $key\n${value.prettyToString()}")
                    val listOffer = value.offers?.filter { it.id != null } ?: emptyList()
                    _state.value = _state.value.copy(listOffer = listOffer)
                }
            }

            override fun onPurchaseFailed(
                purchaseInfo: DataWrappers.PurchaseInfo?,
                billingResponseCode: Int?
            ) {
                Log.e(TAG, "onPurchaseFailed")

            }

            override fun onSubscriptionRestored(purchaseInfo: DataWrappers.PurchaseInfo) {
                Log.e(TAG, "onSubscriptionRestored")

            }

            override fun onSubscriptionPurchased(purchaseInfo: DataWrappers.PurchaseInfo) {
                _state.value = _state.value.copy(isShowDialog = true)
                Log.e(TAG, "onSubscriptionPurchased")
            }
        }

    fun setupIapConnector(context: Context) {
        val subs = mutableListOf<String>()
        val nonCons = mutableListOf<String>()
        if (isSubscription) subs.add(key)
        else nonCons.add(key)
        iapConnector = IapConnector(context, nonCons, emptyList(), subs)
        if (isSubscription) iapConnector.addSubscriptionListener(subscriptionServiceListener)
        else iapConnector.addPurchaseListener(purchaseServiceListener)
    }

    fun subscribeProduct(activity: Activity, offerID: String) {
        iapConnector.subscribe(activity, key, offerID)
    }
    fun buyProduct(activity: Activity) {
        iapConnector.purchase(activity, key)
    }
    fun destroy() {
        iapConnector.destroy()
    }

    fun cancelProduct(activity: Activity) {
        iapConnector.unsubscribe(activity, key)
    }
}

class RemoveAdViewModelFactory(private val isSubscription: Boolean, private val key: String) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RemoveAdViewModel::class.java)) {
            return RemoveAdViewModel(isSubscription, key) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
