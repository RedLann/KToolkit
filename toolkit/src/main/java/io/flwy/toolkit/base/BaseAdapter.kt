package com.cwsinformatica.photocloud.ui.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cwsinformatica.photocloud.data.model.UiModel

abstract class BaseAdapter<N : UiModel> :
    RecyclerView.Adapter<BaseAdapter<N>.BaseViewHolder>() {
    protected open val differ: AsyncListDiffer<N> by lazy { AsyncListDiffer(this, ItemDiffCallback()) }
    protected val dataset: List<N> get() = differ.currentList
    var onItemSelected: (N) -> Unit = {}

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder as BaseNodeViewHolder
        holder.bindNode(dataset[position])
    }

    abstract inner class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view)

    abstract inner class BaseNodeViewHolder(view: View) : BaseViewHolder(view) {
        abstract fun bindNode(item: N)
    }

    open fun submitNodes(items: List<N>) {
        differ.submitList(items)
    }

    inner class ItemDiffCallback : DiffUtil.ItemCallback<N>() {
        override fun areItemsTheSame(oldItem: N, newItem: N): Boolean {
            return oldItem.key == newItem.key
        }

        override fun areContentsTheSame(oldItem: N, newItem: N): Boolean {
            return oldItem.areContentsTheSame(newItem)
        }
    }

    protected fun <T : ViewDataBinding> bindingInflate(
        parent: ViewGroup,
        layout: Int
    ): T = DataBindingUtil.inflate(
        LayoutInflater.from(parent.context),
        layout,
        parent,
        false
    )
}