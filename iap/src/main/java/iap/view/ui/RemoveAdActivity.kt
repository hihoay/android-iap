package iap.view.ui

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.WindowManager.BadTokenException
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.taymay.library.iap.R
import com.taymay.library.iap.databinding.ActivityRemoveAdBinding
import com.taymay.library.iap.databinding.DialogUpgradedBinding
import iap.entity.ItemSubscriptionContent
import iap.view.component.adapter.RemoveAdContentAdapter
import iap.view.component.adapter.RemoveAdPriceAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class RemoveAdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRemoveAdBinding
    private val contentAdapter by lazy {
        RemoveAdContentAdapter()
    }
    lateinit var dialogUpgradedBinding: DialogUpgradedBinding

    private val priceAdapter by lazy {
        RemoveAdPriceAdapter()
    }
    val viewModel by lazy {
        ViewModelProvider(
            this,
            RemoveAdViewModelFactory(isSubscription, key)
        )[RemoveAdViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRemoveAdBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()
        initData()
        initView()
        initObserver()
        initEvent()
    }

    private fun init() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        binding.txtTitle.text = Companion.title
        binding.btnUpgrade.text = buttonTitle
        if (isPremium) {
            binding.btnUpgrade.isEnabled = true
            binding.txtInformation.text = "Privacy | Terms"
            binding.txtInformation.setTextColor(Color.BLACK)
            binding.txtInformation.setOnClickListener {
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://taymay.io/policy.html"))
                ContextCompat.startActivity(this, browserIntent, null)
            }
        } else {
            setupClickableText(binding.txtInformation, this)
        }
    }

    private fun initData() {
        viewModel.setupIapConnector(this)
        contentAdapter.submitList(listItemSubscriptionContent)
    }

    private fun initView() {
        with(binding) {
            rcvPrice.apply {
                adapter = priceAdapter
            }
            rcvContent.apply {
                setHasFixedSize(true)
                adapter = contentAdapter
            }
        }
    }

    private fun initObserver() {
        priceAdapter.listener = { item, pos ->
            priceAdapter.selectedOfferID = item.id.toString()
            priceAdapter.notifyDataSetChanged()
        }
        lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                with(state.isShowDialog) {
                    if (this) {
                        viewModel.destroy()
                        showDialogUpgraded()
                    }
                }

                with(state.listOffer) {
                    if (this.isEmpty()) {
                        Log.e("listOffer", "Empty!")
                        return@with
                    }
                    binding.btnUpgrade.isEnabled = true
                    if (isSubscription) {
                        priceAdapter.selectedOfferID = this.first().id.toString()
                        priceAdapter.submitList(this)
                    } else {
                        val offer = this[0]
                        binding.txtPrice.text = offer.pricingPhases[0].price + " / " + "Lifetime"
                    }
                }
            }
        }
    }

    private fun initEvent() {
        binding.apply {
            btnBack.setOnClickListener {
                finish()
            }
            btnRestore.setOnClickListener {
            }
            btnUpgrade.setOnClickListener {
                Log.e("setOnClickListener", "btnUpgrade!")

                if (isPremium) {
                    viewModel.destroy()
                    showDialogUpgraded()
                    return@setOnClickListener
                }
                if (isSubscription) {
                    viewModel.subscribeProduct(this@RemoveAdActivity, priceAdapter.selectedOfferID)
                } else {
                    viewModel.buyProduct(this@RemoveAdActivity)
                }
            }
        }
    }

    private fun setupClickableText(textView: TextView, context: Context) {
        val fullText =
            "Payment will be charged to your Google Play account at trial end or purchase confirmation. " +
                    "Subscription auto-renews unless canceled at least 24 hours before the end of the trial or current period. " +
                    "Manage or cancel anytime in your Google Play settings. See our Privacy Policy and Cancel Subscription for more info."

        val privacyPolicy = "Privacy Policy"
        val cancelSub = "Cancel Subscription"

        val spannableString = SpannableString(fullText)

        val privacyStart = fullText.indexOf(privacyPolicy)
        val cancelStart = fullText.indexOf(cancelSub)

        if (privacyStart != -1) {
            spannableString.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    val browserIntent =
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://taymay.io/policy.html"))
                    context.startActivity(browserIntent)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = true
                    ds.color = Color.BLUE
                }
            }, privacyStart, privacyStart + privacyPolicy.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        if (cancelStart != -1) {
            spannableString.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    viewModel.cancelProduct(this@RemoveAdActivity)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = true
                    ds.color = Color.BLUE
                }
            }, cancelStart, cancelStart + cancelSub.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        textView.text = spannableString
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.highlightColor = Color.TRANSPARENT // Không hiện màu nền khi click
    }

    fun showDialogUpgraded() {
        dialogUpgradedBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this), R.layout.dialog_upgraded, null, false
        )
        val dialog = Dialog(this)
        dialog.setCancelable(false)
        dialog.setContentView(dialogUpgradedBinding.root)
        object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                dialog.dismiss()
                restartToTargetActivity()
            }
        }.start()
        try {
            dialog.show()
        } catch (e: BadTokenException) {
            restartToTargetActivity()
        }
    }

    private fun restartToTargetActivity() {
        val className = intent.getStringExtra("className") ?: return
        val packageName = intent.getStringExtra("packageName") ?: return
        try {
            val intent = Intent().apply {
                setClassName(packageName, className)
                putExtra("isRemoveAds", true)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private var listItemSubscriptionContent = emptyList<ItemSubscriptionContent>()
        private var isSubscription = false
        private var key = ""
        private var title = ""
        private var buttonTitle = ""
        private var isPremium = false
        fun Context.hihoayGoToPremiumActivity(
            listItemSubContent: List<ItemSubscriptionContent>,
            key: String,
            className: String,
            title: String,
            buttonTitle: String,
        ) {
            isPremium = true
            listItemSubscriptionContent = listItemSubContent
            Companion.key = key
            Companion.title = title
            Companion.buttonTitle = buttonTitle
            startActivity(Intent(this, RemoveAdActivity::class.java).apply {
                putExtra("packageName", this@hihoayGoToPremiumActivity.packageName)
                putExtra("className", className)
            })
        }

        fun Context.hihoayGoToIAPActivity(
            listItemSubContent: List<ItemSubscriptionContent>,
            key: String,
            className: String,
            title: String,
            buttonTitle: String,
            isSub: Boolean
        ) {
            listItemSubscriptionContent = listItemSubContent
            Companion.key = key
            Companion.isSubscription = isSub
            Companion.title = title
            Companion.buttonTitle = buttonTitle
            startActivity(Intent(this, RemoveAdActivity::class.java).apply {
                putExtra("packageName", this@hihoayGoToIAPActivity.packageName)
                putExtra("className", className)
            })
        }
    }
}