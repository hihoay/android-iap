package iap.listener

interface BillingClientConnectionListener {
    fun onConnected(status: Boolean, billingResponseCode: Int)
}