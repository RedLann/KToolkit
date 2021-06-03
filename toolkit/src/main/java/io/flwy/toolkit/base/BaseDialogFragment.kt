package com.cwsinformatica.photocloud.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.cwsinformatica.photocloud.util.bindingInflate

abstract class BaseDialogFragment<BindingType : ViewDataBinding>(
    @LayoutRes private val layout: Int
): DialogFragment() {
    protected lateinit var binding: BindingType

    companion object {
        const val KEY_DIALOG_MODEL = "KEY_DIALOG_MODEL"
    }

    object Dismiss

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
        isCancelable = false
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
        }
    }
}