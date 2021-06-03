package io.flwy.toolkit.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.core.os.bundleOf
import androidx.navigation.NavDirections
import com.cwsinformatica.photocloud.ui.base.BaseBottomSheetDialogFragment

sealed class UiEvent {
    data class WithDirections(val directions: NavDirections): UiEvent()
    data class ToRes(@IdRes val resId: Int): UiEvent()
    data class Sheet(val sheet: BaseBottomSheetDialogFragment<*>, val tag: String = sheet::class.java.simpleName): UiEvent()
    data class ToActivity(val intent: Intent, val bundle: Bundle = bundleOf(), val finish: Boolean = false): UiEvent()
    data class ToActivityForResult(val reason: Int, val activityClass: Class<out Activity>): UiEvent()

    object Back: UiEvent()
    object BackHome: UiEvent()
    data class SnackMessage(val message: String, val light: Boolean = true): UiEvent()
    data class SnackSuccess(val message: String): UiEvent()
    data class SnackError(val message: String): UiEvent()
}
