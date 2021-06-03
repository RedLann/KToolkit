package io.flwy.toolkit.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.cwsinformatica.photocloud.ui.base.BaseBottomSheetDialogFragment

abstract class NavigationSheetDialogFragment<BindingType : ViewDataBinding>(@LayoutRes layout: Int) :
    BaseBottomSheetDialogFragment<BindingType>(layout) {
    protected abstract val vm: UiInteractorViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.uiEventsBus.consumeIfAvailable {
            when (it) {
                is UiEvent.Sheet -> it.sheet.show(parentFragmentManager, it.tag)
                is UiEvent.Back -> dismiss()
                else -> {
                    throw Exception("Can't handle $it")
                }
            }
        }
    }
}