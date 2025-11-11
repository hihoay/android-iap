package taymay.iap.frameworks

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager.BadTokenException
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.taymay.library.iap.R
import com.taymay.library.iap.databinding.DialogRemoveAdsBinding
import com.taymay.library.iap.databinding.DialogUpgradedBinding
import taymay.frameworks.utils.elog
import taymay.iap.frameworks.iap.DataWrappers
import taymay.iap.frameworks.iap.IapConnector
import taymay.iap.frameworks.iap.PurchaseServiceListener
import taymay.iap.frameworks.utils.MyCache
import taymay.iap.frameworks.utils.Singletons.trackingFunction

class DialogRemoveAd(var context: Context) {
    lateinit var dialogIAP: Dialog
    lateinit var dialogRemoveAdsBinding: DialogRemoveAdsBinding
    lateinit var iapConnector: IapConnector
    lateinit var dialogUpgradedBinding: DialogUpgradedBinding
    lateinit var product: String
    lateinit var clasHome: Class<*>
    var nonCons: List<String> = ArrayList()

    companion object {
        var IS_PREMIUM = "IS_PREMIUM"
    }

    var purchaseServiceListener: PurchaseServiceListener = object : PurchaseServiceListener {
        override fun onPricesUpdated(iapKeyPrices: Map<String, DataWrappers.ProductDetails>) {
            iapKeyPrices.forEach { entry -> elog(entry.key, entry.value) }
            if (iapKeyPrices.containsKey(product)) {
                dialogRemoveAdsBinding.tvCostPremium.text =
                    iapKeyPrices[product]?.offers?.first()?.pricingPhases?.first()?.price + " / Lifetime"
            } else {
                Toast.makeText(context, "Oops..Some Error..Try later!", Toast.LENGTH_SHORT).show()
                if (dialogIAP.isShowing) dialogIAP.dismiss()
                return
            }
            dialogRemoveAdsBinding.tvPay.setOnClickListener {
                iapConnector.purchase(context as Activity, product)
                dialogRemoveAdsBinding.tvPay.visibility = View.GONE
            }
            dialogRemoveAdsBinding.llDialogRemoveAd.visibility = View.VISIBLE
            dialogRemoveAdsBinding.llLoading.visibility = View.GONE
        }

        override fun onProductPurchased(purchaseInfo: DataWrappers.PurchaseInfo) {
//            logToServer(context, purchaseInfo)
            MyCache.putBooleanValueByName(
                context, IS_PREMIUM, true
            )
            showDialogUpgraded(clasHome)
        }

        override fun onProductRestored(purchaseInfo: DataWrappers.PurchaseInfo) {
            try {
                trackingFunction(
                    "restored_${purchaseInfo.sku}", mapOf(
                        "orderId" to "${purchaseInfo.orderId}",
                        "purchaseToken" to "${purchaseInfo.purchaseToken}",
                        "signature" to "${purchaseInfo.signature}",
                        "sku" to "${purchaseInfo.sku}",
                    )
                )
            } catch (e: Exception) {

            }

            if (purchaseInfo.sku in nonCons) MyCache.putBooleanValueByName(
                context, IS_PREMIUM, true
            )
            showDialogUpgraded(clasHome)
        }


        override fun onPurchaseFailed(
            purchaseInfo: DataWrappers.PurchaseInfo?, billingResponseCode: Int?
        ) {
        }
    }

    fun loadPrice(nonCons: List<String>, product: String, tvPrice: TextView) {
        val cons: List<String> = ArrayList()
        val subs: List<String> = ArrayList()
        iapConnector = IapConnector(context, nonCons, cons, subs)
        val mPurchaseServiceListener: PurchaseServiceListener = object : PurchaseServiceListener {


            override fun onPricesUpdated(iapKeyPrices: Map<String, DataWrappers.ProductDetails>) {
                if (iapKeyPrices.containsKey(product)) tvPrice.setText(iapKeyPrices[product]?.offers?.first()?.pricingPhases?.first()?.price + " / Lifetime") else tvPrice.setText(
                    ""
                )
                iapConnector.removePurchaseListener(this)
            }

            override fun onProductPurchased(purchaseInfo: DataWrappers.PurchaseInfo) {}
            override fun onProductRestored(purchaseInfo: DataWrappers.PurchaseInfo) {}
            override fun onPurchaseFailed(
                purchaseInfo: DataWrappers.PurchaseInfo?, billingResponseCode: Int?
            ) {

            }
        }
        iapConnector.addPurchaseListener(mPurchaseServiceListener)
    }


    fun showDialogRemoveAd(idProducts: String, clasHome: Class<*>) {
        this.product = idProducts.split(",").map { it.trim() }[0]
        this.clasHome = clasHome
        val cons: List<String> = ArrayList()
        val subs: List<String> = ArrayList()
        this.nonCons = idProducts.split(",").map { it.trim() }
        iapConnector = IapConnector(context, nonCons, cons, subs)
        dialogIAP = Dialog(context)
        dialogRemoveAdsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.dialog_remove_ads, null, false
        )
        dialogRemoveAdsBinding.llDialogRemoveAd.visibility = View.GONE
        dialogRemoveAdsBinding.llLoading.visibility = View.VISIBLE
        dialogIAP.setContentView(dialogRemoveAdsBinding.getRoot())
        try {
            dialogIAP.show()
        } catch (e: Exception) {
        }
        dialogRemoveAdsBinding.icClose.setOnClickListener { v: View? ->
            if (dialogIAP.isShowing) {
                dialogIAP.dismiss()
            }
        }
        iapConnector.addPurchaseListener(purchaseServiceListener)
    }

    fun showDialogUpgraded(classHome: Class<*>?) {
        dialogUpgradedBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.dialog_upgraded, null, false
        )
        var dialog = Dialog(context)
        dialog.setCancelable(false)
        dialog.setContentView(dialogUpgradedBinding.getRoot())
        object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                dialog.dismiss()

                if (purchaseServiceListener != null && iapConnector != null) iapConnector.removePurchaseListener(
                    purchaseServiceListener
                )
                val restartApp = Intent(context, classHome)
                restartApp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(restartApp)
            }
        }.start()
        if (dialogIAP != null && dialogIAP.isShowing) dialogIAP.dismiss()
        try {
            dialog.show()
        } catch (e: BadTokenException) {
            if (purchaseServiceListener != null && iapConnector != null) iapConnector.removePurchaseListener(
                purchaseServiceListener
            )
            val restartApp = Intent(context, classHome)
            restartApp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(restartApp)
        }
    }


}