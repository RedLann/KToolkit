package io.flwy.toolkit.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.cwsinformatica.photocloud.ui.base.BaseUiInteractor
import com.cwsinformatica.photocloud.ui.base.Consumable
import com.cwsinformatica.photocloud.ui.base.MinimalUiInteractor
import java.io.File

abstract class BaseAndroidViewModel(app: Application): AndroidViewModel(app),
    UiInteractorViewModel {
    protected fun getContext() = getApplication<Application>().applicationContext
    protected fun getString(resId: Int) = getApplication<Application>().applicationContext.getString(resId)
    override val uiEventsBus: MutableLiveData<Consumable<UiEvent>> = MutableLiveData()
    open val uiInteractor: BaseUiInteractor = MinimalUiInteractor

    fun createDirectory(dirName: String) {
        val cacheDir = getContext().cacheDir
        File(cacheDir, "albums/$dirName").mkdirs()
    }

    fun NetworkResponse.Error.notifyUser() {
        uiInteractor.snackError(this.message).notify()
    }
}