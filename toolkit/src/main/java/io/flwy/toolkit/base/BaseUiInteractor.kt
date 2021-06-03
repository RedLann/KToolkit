package com.cwsinformatica.photocloud.ui.base

import com.cwsinformatica.photocloud.ui.base.Consumable.Companion.asConsumable
import io.flwy.toolkit.base.UiEvent

abstract class BaseUiInteractor {
    fun goBack() = UiEvent.Back.asConsumable()
    fun goBackHome() = UiEvent.BackHome.asConsumable()
    fun snackMessage(message: String, light: Boolean = true) = UiEvent.SnackMessage(message, light).asConsumable()
    fun snackError(message: String) = UiEvent.SnackError(message).asConsumable()
    fun snackSuccess(message: String) = UiEvent.SnackSuccess(message).asConsumable()
}

object MinimalUiInteractor: BaseUiInteractor()