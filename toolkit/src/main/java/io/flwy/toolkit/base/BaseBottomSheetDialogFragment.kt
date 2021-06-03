package com.cwsinformatica.photocloud.ui.base

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.distinctUntilChanged
import com.cwsinformatica.photocloud.data.model.UiModel
import com.cwsinformatica.photocloud.util.bindingInflate
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


abstract class BaseBottomSheetDialogFragment<BindingType : ViewDataBinding>(@LayoutRes private val layout: Int) :
    BottomSheetDialogFragment() {
    protected lateinit var binding: BindingType

    fun <T> MutableLiveData<Consumable<T>>.consumeIfAvailable(@NonNull observer: Observer<T>) =
        observe(viewLifecycleOwner) { event ->
            event.consume { observer.onChanged(it) }
        }

    override fun onStart() {
        super.onStart()
        val bottomBehavior = BottomSheetBehavior.from(binding.root.parent as View)
        bottomBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = bindingInflate(
            inflater,
            layout,
            container
        )
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }


    protected fun setupFullHeight() {
        dialog?.let { d ->
            d as BottomSheetDialog
            val bottomSheetInternal =
                d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheetInternal)
            val layoutParams = bottomSheetInternal.layoutParams
            val windowHeight = getWindowHeight()
            if (layoutParams != null) {
                layoutParams.height = (windowHeight * 0.8).toInt()
            }
            bottomSheetInternal.layoutParams = layoutParams
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    protected fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    fun <T : UiModel> LiveData<List<T>>.attachToAdapter(
        adapter: BaseAdapter<T>,
        wrapAsDistinct: Boolean = true,
        doAfter: (List<T>) -> Unit = {}
    ) {
        val liveData = if (wrapAsDistinct) distinctUntilChanged() else this
        liveData.observe(viewLifecycleOwner) {
            adapter.submitNodes(it)
            doAfter(it)
        }
    }
}