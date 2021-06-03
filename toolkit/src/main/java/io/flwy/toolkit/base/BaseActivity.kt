package io.flwy.toolkit.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.cwsinformatica.photocloud.ui.base.Consumable
import com.cwsinformatica.photocloud.util.bindingInflate

abstract class BaseActivity<BindingType : ViewDataBinding>(@LayoutRes private val layout: Int): AppCompatActivity() {
    protected lateinit var binding: BindingType
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindingInflate(layout)
        binding.lifecycleOwner = this
    }

    fun <T> MutableLiveData<Consumable<T>>.consumeIfAvailable(@NonNull observer: Observer<T>) = observe(this@BaseActivity) { event ->
        event.consume { observer.onChanged(it) }
    }
}