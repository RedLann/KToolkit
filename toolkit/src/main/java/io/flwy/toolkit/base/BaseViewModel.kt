package com.cwsinformatica.photocloud.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.flwy.toolkit.base.UiEvent
import io.flwy.toolkit.base.UiInteractorViewModel

abstract class BaseViewModel: ViewModel(), UiInteractorViewModel {
    override val uiEventsBus: MutableLiveData<Consumable<UiEvent>> = MutableLiveData()
    open val uiInteractor: BaseUiInteractor = MinimalUiInteractor
}