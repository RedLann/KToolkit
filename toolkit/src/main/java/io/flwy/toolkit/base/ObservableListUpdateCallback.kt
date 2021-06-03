package io.flwy.toolkit.base

import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView

class ObservableListUpdateCallback(private val mAdapter: RecyclerView.Adapter<*>, private val forwardCallback: ListUpdateCallback? = null) :
    ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {
        mAdapter.notifyItemRangeInserted(position, count)
        forwardCallback?.onInserted(position, count)
    }

    override fun onRemoved(position: Int, count: Int) {
        mAdapter.notifyItemRangeRemoved(position, count)
        forwardCallback?.onRemoved(position, count)
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        mAdapter.notifyItemMoved(fromPosition, toPosition)
        forwardCallback?.onMoved(fromPosition, toPosition)
    }

    override fun onChanged(position: Int, count: Int, payload: Any?) {
        mAdapter.notifyItemRangeChanged(position, count, payload)
        forwardCallback?.onChanged(position, count, payload)
    }
}