package io.flwy.toolkit.base

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController
import com.cwsinformatica.photocloud.ui.base.BaseFragment
import com.cwsinformatica.photocloud.ui.base.Consumable.Companion.asConsumable
import com.cwsinformatica.photocloud.util.NAV_OPTIONS_SLIDE
import io.flwy.toolkit.util.darkSnack
import io.flwy.toolkit.util.lightSnack

abstract class NavigationFragment<BindingType : ViewDataBinding>(@LayoutRes layout: Int) :
    BaseFragment<BindingType>(layout) {
    protected abstract val vm: UiInteractorViewModel
    protected abstract val snackbarAnchorViewId: Int

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.uiEventsBus.consumeIfAvailable {
            when (it) {
                is UiEvent.WithDirections -> navigate(it.directions)
                is UiEvent.Sheet -> it.sheet.show(parentFragmentManager, it.tag)
                is UiEvent.Back -> navigateBack()
                is UiEvent.BackHome -> navigateBackHome()
                is UiEvent.ToRes -> navigate(it.resId)
                is UiEvent.SnackError -> uiVm.uiEventsBus.postValue(it.asConsumable())
                is UiEvent.SnackSuccess -> uiVm.uiEventsBus.postValue(it.asConsumable())
                is UiEvent.ToActivity -> {
                    startActivity(it.intent, it.bundle)
                    if (it.finish) {
                        requireActivity().finish()
                    }
                }
                is UiEvent.ToActivityForResult -> {
                    throw IllegalStateException("Not handling Activity for result here")
                }
                is UiEvent.SnackMessage -> with(binding.root) {
                    if (it.light) lightSnack(it.message, snackbarAnchorViewId)
                    else darkSnack(it.message, snackbarAnchorViewId)
                }
            }
        }
    }

    protected fun navigateWithExtras(directions: NavDirections, extras: Navigator.Extras) = findNavController().navigate(directions, extras)

    protected fun navigate(
        @IdRes resId: Int,
        args: Bundle? = null,
        navOptions: NavOptions? = NAV_OPTIONS_SLIDE
    ) =
        findNavController().navigate(resId, args, navOptions)

    protected fun navigate(directions: NavDirections, navOptions: NavOptions? = NAV_OPTIONS_SLIDE) =
        findNavController().navigate(directions, navOptions)

    protected fun navigateBack() = findNavController().popBackStack()
    protected fun navigateBackHome() = navigateUpTo(findNavController().graph.startDestination)

    protected fun navigateUpTo(@IdRes resId: Int) =
        navigate(resId, navOptions = NavOptions.Builder().setPopUpTo(resId, true).build())

}