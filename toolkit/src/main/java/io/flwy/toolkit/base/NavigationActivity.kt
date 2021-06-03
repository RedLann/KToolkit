package io.flwy.toolkit.base

import android.content.Intent
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import io.flwy.toolkit.util.darkSnack
import io.flwy.toolkit.util.errorSnack
import io.flwy.toolkit.util.lightSnack
import io.flwy.toolkit.util.successSnack

abstract class NavigationActivity<BindingType : ViewDataBinding>(
    @LayoutRes layout: Int
) : BaseActivity<BindingType>(layout) {
    protected abstract val uiVm: UiInteractorViewModel
    protected abstract val snackbarAnchorViewId: Int
    protected abstract val defaultNavHostId: Int

    protected fun getForegroundFragment(): Fragment? {
        val navHostFragment = supportFragmentManager.findFragmentById(defaultNavHostId)
        return navHostFragment?.childFragmentManager?.fragments?.get(0)
    }

    protected fun navigateUpTo(@IdRes resId: Int) =
        navigate(resId, navOptions = NavOptions.Builder().setPopUpTo(resId, true).build())

    protected fun navigate(
        @IdRes resId: Int,
        navHostViewId: Int = defaultNavHostId,
        args: Bundle? = null,
        @Nullable navOptions: NavOptions? = null
    ) =
        findNavController(navHostViewId).navigate(resId, args, navOptions)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        uiVm.uiEventsBus.consumeIfAvailable {
            when (it) {
                is UiEvent.WithDirections -> {
                    throw IllegalStateException("Not handling directions from Activity")
                }
                is UiEvent.Sheet -> it.sheet.show(supportFragmentManager, it.tag)
                is UiEvent.Back -> navigateBack()
                is UiEvent.BackHome -> navigateBackHome()
                is UiEvent.ToRes -> navigate(it.resId)
                is UiEvent.SnackError -> binding.root.errorSnack(it.message, snackbarAnchorViewId)
                is UiEvent.SnackSuccess -> binding.root.successSnack(it.message, snackbarAnchorViewId)
                is UiEvent.SnackMessage -> with(binding.root) {
                    if (it.light) lightSnack(it.message, snackbarAnchorViewId)
                    else darkSnack(it.message, snackbarAnchorViewId)
                }
                is UiEvent.ToActivity -> {
                    startActivity(it.intent, it.bundle)
                    if (it.finish) {
                        finish()
                    }
                }
                is UiEvent.ToActivityForResult -> {
                    startActivityForResult(Intent(this, it.activityClass), it.reason)
                }
            }
        }
    }

    protected fun navigateBack(navHostViewId: Int = defaultNavHostId) = findNavController(navHostViewId).popBackStack()
    protected fun navigateBackHome(navHostViewId: Int = defaultNavHostId) = navigateUpTo(findNavController(navHostViewId).graph.startDestination)
}