package io.flwy.toolkit.data.remote

import io.flwy.toolkit.BuildConfig
import okhttp3.*
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

open class RetrofitFactory private constructor(
    interceptors: MutableList<Interceptor> = mutableListOf(),
    networkInterceptors: MutableList<Interceptor> = mutableListOf(),
    converterFactories: MutableList<Converter.Factory> = mutableListOf(),
    callAdapterFactories: MutableList<CallAdapter.Factory> = mutableListOf(),
    authenticator: Authenticator? = null
) {
    private val retrofitBuilder: Retrofit.Builder = Retrofit.Builder()
    private lateinit var retrofit: Retrofit

    init {
        val httpClientBuilder = if (BuildConfig.DEBUG)
            getUnsafeOkHttpClient()
        else
            OkHttpClient.Builder()

        interceptors.forEach { httpClientBuilder.addInterceptor(it) }
        networkInterceptors.forEach { httpClientBuilder.addInterceptor(it) }
        converterFactories.forEach { retrofitBuilder.addConverterFactory(it) }
        callAdapterFactories.forEach { retrofitBuilder.addCallAdapterFactory(it) }
        if (authenticator != null)
            httpClientBuilder.authenticator(authenticator)
        retrofitBuilder.client(httpClientBuilder.build())
    }

    fun <S> createService(baseUrl: String, serviceClass: Class<S>): S {
        retrofit = retrofitBuilder.baseUrl(baseUrl).build()
        return retrofit.create(serviceClass)
    }

    private fun getUnsafeOkHttpClient(): OkHttpClient.Builder {
        val trustAllCerts = arrayOf<TrustManager>(
            object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(
                    chain: Array<X509Certificate>, authType: String
                ) = Unit

                @Throws(CertificateException::class)
                override fun checkServerTrusted(
                    chain: Array<X509Certificate>,
                    authType: String
                ) = Unit

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            }
        )

        val sslSocketFactory = SSLContext.getInstance("SSL").apply {
            init(null, trustAllCerts, SecureRandom())
        }.socketFactory

        val httpClientBuilder = OkHttpClient.Builder()

        httpClientBuilder.sslSocketFactory(
            sslSocketFactory,
            trustAllCerts[0] as X509TrustManager
        )

        httpClientBuilder.hostnameVerifier { hostname, session -> true }
        return httpClientBuilder
    }

    data class Builder(
        private var interceptors: MutableList<Interceptor> = mutableListOf(),
        private var networkInterceptors: MutableList<Interceptor> = mutableListOf(),
        private var converterFactories: MutableList<Converter.Factory> = mutableListOf(),
        private var callAdapterFactories: MutableList<CallAdapter.Factory> = mutableListOf(),
        private var authenticator: Authenticator? = null
    ) {
        fun addInterceptors(vararg interceptors: Interceptor) = apply {
            this.interceptors.addAll(interceptors)
        }

        fun addNetworkInterceptors(vararg interceptors: Interceptor) = apply {
            this.networkInterceptors.addAll(interceptors)
        }

        fun addConverterFactories(vararg factories: Converter.Factory) = apply {
            this.converterFactories.addAll(factories)
        }

        fun addCallAdapterFactories(vararg factories: CallAdapter.Factory) = apply {
            this.callAdapterFactories.addAll(factories)
        }

        fun authenticator(authenticator: Authenticator) = apply {
            this.authenticator = authenticator
        }

        fun build() = RetrofitFactory(
            interceptors,
            networkInterceptors,
            converterFactories,
            callAdapterFactories,
            authenticator
        )
    }
}