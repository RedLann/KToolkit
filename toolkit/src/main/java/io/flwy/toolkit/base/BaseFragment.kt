package com.cwsinformatica.photocloud.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.distinctUntilChanged
import com.cwsinformatica.photocloud.util.bindingInflate
import com.cwsinformatica.photocloud.data.model.UiModel
import com.cwsinformatica.photocloud.ui.UiViewModel
import com.cwsinformatica.photocloud.ui.model.ScreenConfig
import com.google.firebase.analytics.FirebaseAnalytics
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import com.google.firebase.analytics.ktx.logEvent

abstract class BaseFragment<BindingType : ViewDataBinding>(@LayoutRes private val layout: Int) :
    Fragment() {
    protected lateinit var binding: BindingType
    protected val uiVm by sharedViewModel<UiViewModel>()
    protected open val screenConfiguration = ScreenConfig()
    protected open val hasOptionsMenu: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(hasOptionsMenu)
        binding = bindingInflate(inflater, layout, container)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiVm.screenConfigConfiguration.value = screenConfiguration
    }

    fun <T> MutableLiveData<Consumable<T>>.consumeIfAvailable(@NonNull observer: Observer<T>) = observe(viewLifecycleOwner) { event ->
        event.consume { observer.onChanged(it) }
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

    open fun onFabClicked(): Boolean {
        return false
    }

    override fun onResume() {
        super.onResume()

        FirebaseAnalytics.getInstance(requireContext()).logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, this::class.simpleName ?: "Anonymous class")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "MainActivity")
        }
    }
}