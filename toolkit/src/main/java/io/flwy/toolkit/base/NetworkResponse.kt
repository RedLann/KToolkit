package io.flwy.toolkit.base

sealed class NetworkResponse<out T> {
    data class Success<out T>(val value: T): NetworkResponse<T>()
    data class Error(val code: Int = 0, val message: String = "", val throwable: Throwable? = null) : NetworkResponse<Nothing>()
}

suspend inline infix fun <T, R> NetworkResponse<T>.ifSuccess(crossinline block: (T) -> R) = if (this is NetworkResponse.Success) block(this.value) else null

object None

suspend fun <T> NetworkResponse<T>.onSuccess(block: suspend (T) -> Unit): NetworkResponse<T> {
    if (this is NetworkResponse.Success) block(this.value)
    return this
}

suspend fun <T> NetworkResponse<T>.onFailure(block: suspend (NetworkResponse.Error) -> Unit): NetworkResponse<T> {
    if (this is NetworkResponse.Error) block(this)
    return this
}