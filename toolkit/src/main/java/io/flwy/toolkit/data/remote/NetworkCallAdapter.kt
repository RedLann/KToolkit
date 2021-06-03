package io.flwy.toolkit.data.remote

import io.flwy.toolkit.base.NetworkResponse
import okhttp3.Request
import okio.Timeout
import retrofit2.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class NetworkCallAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) return null
        check(returnType is ParameterizedType) { "Return type must be a parameterized type." }

        val responseType = getParameterUpperBound(0, returnType)
        if (getRawType(responseType) != NetworkResponse::class.java) return null
        check(responseType is ParameterizedType) { "Response type must be a parameterized type." }

        val leftType = getParameterUpperBound(0, responseType)
        return NetworkCallAdapter<Any>(leftType)
    }
}

private class NetworkCallAdapter<R>(
    private val successType: Type
) : CallAdapter<R, Call<NetworkResponse<R>>> {

    override fun adapt(call: Call<R>): Call<NetworkResponse<R>> = NetworkCall(call, successType)

    override fun responseType(): Type = successType
}

private class NetworkCall<R>(
    private val delegate: Call<R>,
    private val successType: Type
) : Call<NetworkResponse<R>> {
    override fun enqueue(callback: Callback<NetworkResponse<R>>) = delegate.enqueue(
        object : Callback<R> {

            override fun onResponse(call: Call<R>, response: Response<R>) {
                callback.onResponse(this@NetworkCall, Response.success(response.toEither(call)))
            }

            private fun Response<R>.toEither(call: Call<R>): NetworkResponse<R> {
                // Http error response (4xx - 5xx)
                if (!isSuccessful) {
                    return errorBody()?.let {
                        NetworkResponse.Error(code(), it.string())
                    } ?: NetworkResponse.Error(code(), "Empty body error")
                }

                // Http success response with body
                body()?.let { body -> return NetworkResponse.Success(body) }

                // if we defined Unit as success type it means we expected no response body
                // e.g. in case of 204 No Content
                return if (successType == Unit::class.java) {
                    @Suppress("UNCHECKED_CAST")
                    NetworkResponse.Success(Unit) as NetworkResponse<R>
                } else {
                    @Suppress("UNCHECKED_CAST")
                    NetworkResponse.Error(message = "Response body was null") as NetworkResponse<R>
                }
            }

            override fun onFailure(call: Call<R>, throwable: Throwable) {
                val error = NetworkResponse.Error(message = throwable.message.toString(), throwable = throwable)
                callback.onResponse(this@NetworkCall, Response.success(error))
            }
        }
    )

    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun clone(): Call<NetworkResponse<R>> = NetworkCall(delegate.clone(), successType)

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun execute(): Response<NetworkResponse<R>> = throw UnsupportedOperationException()

    override fun request(): Request = delegate.request()
    override fun timeout(): Timeout = delegate.timeout()
}