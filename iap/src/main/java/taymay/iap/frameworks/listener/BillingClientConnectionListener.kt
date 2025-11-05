package taymay.iap.frameworks.listener

interface BillingClientConnectionListener {
    fun onConnected(status: Boolean, billingResponseCode: Int)
}