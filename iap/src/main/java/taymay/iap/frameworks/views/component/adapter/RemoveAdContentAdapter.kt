package taymay.iap.frameworks.views.component.adapter

import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.taymay.library.iap.R
import com.taymay.library.iap.databinding.ItemRemoveAdContentBinding
import taymay.iap.frameworks.entity.ItemSubscriptionContent

internal class RemoveAdContentAdapter : BaseListAdapter<ItemSubscriptionContent, ItemRemoveAdContentBinding>(DIFF_CALLBACK) {
    override fun getLayout(viewType: Int): Int {
        return R.layout.item_remove_ad_content
    }

    override fun onBindViewHolder(
        holder: BaseListAdapter.Companion.BaseViewHolder<ItemRemoveAdContentBinding>,
        position: Int
    ) {
        val itemData = getItem(position)
        with(holder.binding) {
            Glide.with(root).load(itemData.imageRes).into(this.imgItem)
            txtTitle.text = itemData.title
            txtDes.text = itemData.description
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemSubscriptionContent>() {
            override fun areItemsTheSame(
                oldItem: ItemSubscriptionContent,
                newItem: ItemSubscriptionContent
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ItemSubscriptionContent,
                newItem: ItemSubscriptionContent
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}