package com.example.open_pay_technical.data.service

import com.example.open_pay_technical.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiceGenerator {
    private val httpClient = OkHttpClient.Builder().addInterceptor { chain ->
        val defaultRequest = chain.request()

        val defaultHttpUrl = defaultRequest.url()
        val httpUrl = defaultHttpUrl.newBuilder()
            .addQueryParameter(KEY, BuildConfig.API_KEY)
            .build()

        val requestBuilder = defaultRequest.newBuilder().url(httpUrl)

        chain.proceed(requestBuilder.build())
    }

    private val builder = Retrofit.Builder()
        .baseUrl(BuildConfig.TMBD_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())

    fun <S> createService(serviceClass: Class<S>): S {
        val retrofit = builder.client(httpClient.build()).build()
        return retrofit.create(serviceClass)
    }

    companion object {
        private const val KEY = "api_key"
    }
}
