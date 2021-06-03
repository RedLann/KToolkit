package io.flwy.toolkit.data.remote

import io.flwy.toolkit.BuildConfig
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiFactory {
    fun getLoggingInterceptor(level: HttpLoggingInterceptor.Level) = HttpLoggingInterceptor().apply { this.level = level }
    val defaultCallAdapterFactory = NetworkCallAdapterFactory()
    val moshiConverterFactory = MoshiConverterFactory.create()

    fun <T> createService(
        baseUrl: String,
        clazz: Class<T>,
        interceptors: Array<Interceptor>,
        adapterFactories: Array<CallAdapter.Factory>,
        converterFactories: Array<Converter.Factory>
    ): T {
        return RetrofitFactory.Builder()
            .addInterceptors(*interceptors)
            .addCallAdapterFactories(*adapterFactories)
            .addConverterFactories(*converterFactories)
            .build()
            .createService(baseUrl, clazz)
    }
}