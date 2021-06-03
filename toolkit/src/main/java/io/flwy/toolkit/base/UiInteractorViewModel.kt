package io.flwy.toolkit.base

import androidx.lifecycle.MutableLiveData
import com.cwsinformatica.photocloud.ui.base.Consumable

interface UiInteractorViewModel {
    val uiEventsBus: MutableLiveData<Consumable<UiEvent>>
    fun Consumable<UiEvent>.notify() = uiEventsBus.postValue(this)
}