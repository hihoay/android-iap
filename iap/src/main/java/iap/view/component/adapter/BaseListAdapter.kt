package iap.view.component.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.Executors

internal abstract class BaseListAdapter<T : Any, VB : ViewDataBinding>(diffCallback: DiffUtil.ItemCallback<T>) :
    ListAdapter<T, BaseListAdapter.Companion.BaseViewHolder<VB>>(
        AsyncDifferConfig.Builder(diffCallback)
            .setBackgroundThreadExecutor(Executors.newSingleThreadExecutor())
            .build()
    ) {
    var listener: ((item: T, position: Int) -> Unit)? = null
    abstract fun getLayout(viewType: Int): Int
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BaseViewHolder<VB>(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            getLayout(viewType),
            parent,
            false
        )
    )

    companion object {
        class BaseViewHolder<VB : ViewDataBinding>(val binding: VB) :
            RecyclerView.ViewHolder(binding.root)
    }
}