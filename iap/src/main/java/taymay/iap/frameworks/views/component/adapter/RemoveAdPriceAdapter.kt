package taymay.iap.frameworks.views.component.adapter

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.taymay.library.iap.R
import com.taymay.library.iap.databinding.ItemRemoveAdPriceBinding
import taymay.iap.frameworks.entity.DataWrappers

internal class RemoveAdPriceAdapter :
    BaseListAdapter<DataWrappers.Offer, ItemRemoveAdPriceBinding>(DIFF_CALLBACK) {
    var selectedOfferID: String = ""
    override fun getLayout(viewType: Int): Int {
        return R.layout.item_remove_ad_price
    }

    override fun onBindViewHolder(
        holder: BaseListAdapter.Companion.BaseViewHolder<ItemRemoveAdPriceBinding>, position: Int
    ) {
        val offer = getItem(position)
        with(holder.binding) {
            val context = root.context
            if (offer.pricingPhases.size > 1) {
                val freePhase = offer.pricingPhases[0]
                val pricePhase = offer.pricingPhases[1]
                val priceFormat = pricePhase.formatPrice(context)
                val freeFormat = freePhase.formatFreeTrialDuration(context)
                txtTitle.text = priceFormat
                txtDes.text = context.getString(R.string.price_des, freeFormat, priceFormat)
            } else {
                txtTitle.text = offer.pricingPhases[0].formatPrice(context)
            }
            if (offer.id == selectedOfferID) {
                cardItem.isChecked = true
                cardItem.strokeColor =
                    ContextCompat.getColor(root.context, R.color.card_activate_color)
            } else {
                cardItem.isChecked = false
                cardItem.strokeColor =
                    ContextCompat.getColor(root.context, R.color.card_deactivate_color)
            }
            root.setOnClickListener {
                if (selectedOfferID != offer.id) {
                    listener?.invoke(offer, position)
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataWrappers.Offer>() {
            override fun areItemsTheSame(
                oldItem: DataWrappers.Offer, newItem: DataWrappers.Offer
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DataWrappers.Offer, newItem: DataWrappers.Offer
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}