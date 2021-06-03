package com.cwsinformatica.photocloud.ui.base

import androidx.recyclerview.widget.ListUpdateCallback

abstract class ListUpdateCallbackAdapter: ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {
        // Stub
    }

    override fun onRemoved(position: Int, count: Int) {
        // Stub
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        // Stub
    }

    override fun onChanged(position: Int, count: Int, payload: Any?) {
        // Stub
    }
}