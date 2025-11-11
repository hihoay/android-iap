package taymay.iap.frameworks.iap

interface BillingClientConnectionListener {
    fun onConnected(status: Boolean, billingResponseCode: Int)
}