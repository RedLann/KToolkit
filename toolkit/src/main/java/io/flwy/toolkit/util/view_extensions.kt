package com.cwsinformatica.photocloud.util

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import com.cwsinformatica.photocloud.R
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()
fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

fun <T : ViewDataBinding> Fragment.bindingInflate(
    inflater: LayoutInflater,
    layout: Int,
    container: ViewGroup?
): T {
    return DataBindingUtil.inflate(inflater, layout, container, false)
}

fun <T : ViewDataBinding> Activity.bindingInflate(layout: Int): T {
    return DataBindingUtil.setContentView(this, layout)
}

@BindingAdapter("app:errorText")
fun setErrorMessage(view: TextInputLayout, errorMessage: String?) {
    view.error = errorMessage
}

@BindingAdapter("app:imageUrl")
fun bindGlideImage(view: ImageView, url: String) {
    if (url.isNotEmpty())
        GlideApp.with(view).load(url).into(view)
}

@BindingAdapter("app:formattedDate")
fun formattedDate(tv: TextView, timestamp: Long) {
    tv.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(timestamp))
}

val NAV_OPTIONS_SLIDE = NavOptions.Builder()
    .setEnterAnim(R.anim.enter_from_right)
    .setExitAnim(R.anim.exit_to_left)
    .setPopEnterAnim(R.anim.enter_from_left)
    .setPopExitAnim(R.anim.exit_to_right)
    .build()

val NAV_OPTIONS_DEFAULT = NavOptions.Builder()
    .setEnterAnim(R.anim.nav_default_enter_anim)
    .setExitAnim(R.anim.nav_default_exit_anim)
    .setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
    .setPopExitAnim(R.anim.nav_default_pop_exit_anim)
    .build()

fun ImageView.setImageDrawableInt(@DrawableRes drawableRes: Int?) {
    drawableRes?.let {
        setImageDrawable(ContextCompat.getDrawable(this.context, it))
    } ?: setImageDrawable(null)
}

fun Fragment.hideKeyboard() {
    requireActivity().hideKeyboard(requireView())
}

fun AppCompatActivity.hideKeyboard() {
    hideKeyboard(if (currentFocus == null) View(this) else currentFocus!!)
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager =
        getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun ImageView.applySaturationFilter(value: Float = 0f) {
    val matrix = ColorMatrix()
    matrix.setSaturation(0.5f)
    this.colorFilter = ColorMatrixColorFilter(matrix)
}

fun View.setMargins(
    left: Int = this.marginLeft,
    top: Int = this.marginTop,
    right: Int = this.marginRight,
    bottom: Int = this.marginBottom,
) {
    layoutParams = (layoutParams as ViewGroup.MarginLayoutParams).apply {
        setMargins(left, top, right, bottom)
    }
}