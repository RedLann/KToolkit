package io.flwy.toolkit.util

import android.content.res.ColorStateList
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import com.google.android.material.snackbar.Snackbar
import io.flwy.toolkit.R

fun View.errorSnack(message: String, anchorViewId: Int) = snackD(
    message = message,
    drawable = R.drawable.ic_error,
    drawableTint = R.color.white,
    bgColor = R.color.snack_bg_error,
    textColor = R.color.white,
    anchorViewId = anchorViewId
)

fun View.successSnack(message: String, anchorViewId: Int) = snackD(
    message = message,
    drawable = R.drawable.ic_done,
    drawableTint = R.color.white,
    bgColor = R.color.snack_bg_success,
    anchorViewId = anchorViewId
)

fun View.darkSnack(message: String, anchorViewId: Int) = snackD(
    message = message,
    anchorViewId = anchorViewId
)

fun View.lightSnack(message: String, anchorViewId: Int) = snackD(
    message = message,
    bgColor = R.color.white,
    textColor = R.color.black,
    anchorViewId = anchorViewId
)

inline fun View.snackD(
    message: String,
    @DrawableRes drawable: Int = 0,
    @ColorRes drawableTint: Int? = null,
    @ColorRes bgColor: Int? = null,
    @ColorRes textColor: Int? = null,
    lenght: Int = Snackbar.LENGTH_LONG,
    top: Boolean = false,
    f: Snackbar.() -> Unit = {},
    anchorViewId: Int
) {
    val snack = Snackbar.make(this, message, lenght)
    bgColor?.let {
        snack.setBackgroundTint(ContextCompat.getColor(context, it))
    }
    val textView: TextView = snack.view.findViewById(com.google.android.material.R.id.snackbar_text)

//    textView.setTextAppearance(R.style.TextAppearance_MaterialComponents_Body2)

    textView.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)
    textView.compoundDrawablePadding =
        resources.getDimensionPixelOffset(R.dimen.snackbar_drawable_padding)

    drawableTint?.let {
        TextViewCompat.setCompoundDrawableTintList(
            textView,
            ColorStateList.valueOf(ContextCompat.getColor(context, it))
        )
    }

    textColor?.let {
        val color = ContextCompat.getColor(context, it)
        textView.setTextColor(color)
    }

    textView.gravity = Gravity.CENTER

    if (top) {
        snack.apply {
            when (view.layoutParams) {
                is FrameLayout.LayoutParams -> {
                    val params = view.layoutParams as FrameLayout.LayoutParams
                    params.gravity = Gravity.TOP
                    view.layoutParams = params
                }
                is CoordinatorLayout.LayoutParams -> {
                    val params = view.layoutParams as CoordinatorLayout.LayoutParams
                    params.gravity = Gravity.TOP
                    view.layoutParams = params
                }
            }

        }
    }

    snack.f()
    snack.anchorView = findViewById(anchorViewId)
    snack.show()
}
