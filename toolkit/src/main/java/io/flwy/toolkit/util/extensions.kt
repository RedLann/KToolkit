package io.flwy.toolkit.util

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*


fun now() = System.currentTimeMillis()

suspend fun <T> T.emitAsFlow(): Flow<T> = flow {
    emit(this@emitAsFlow)
}

fun <T> T.postOn(ld: MutableLiveData<T>) = ld.postValue(this)