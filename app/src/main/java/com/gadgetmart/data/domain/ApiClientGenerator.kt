package com.gadgetmart.data.domain

import com.gadgetmart.BuildConfig
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * ApiClientGenerator class is used as an mechanism to connect and communicate
 * the result of the API calls back to the app.
 */
object ApiClientGenerator {
    private var retrofit: Retrofit? = null
    private var apiClient: ApiClient? = null

//    private const val baseUrl ="http://ec2-65-0-3-229.ap-south-1.compute.amazonaws.com/admin/public/api/v1/"
//    private const val baseUrl ="http://ec2-13-233-148-41.ap-south-1.compute.amazonaws.com/public/api/v1/"

    private const val baseUrl ="https://gadgetmart.in/public/api/v1/"

    fun getClient(): ApiClient? {
        if (apiClient != null) return apiClient

        if (retrofit != null) return retrofit!!.create(ApiClient::class.java)

        val okHttpClient = OkHttpClient.Builder()
        val httpLoggingInterceptor = HttpLoggingInterceptor()

        okHttpClient.addNetworkInterceptor { chain ->
            val originalRequest: Request = chain.request()
            // Add authorization header with updated authorization value to intercepted request
            val authorisedRequest: Request = originalRequest.newBuilder()
                .header(
                    "Accept",
                    "application/json"
                )
                .build()
            chain.proceed(authorisedRequest)
        }

        httpLoggingInterceptor.level =
            if (BuildConfig.DEBUG_LOG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE

        okHttpClient.addInterceptor(httpLoggingInterceptor)
        okHttpClient.connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)


        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        return retrofit!!.create(ApiClient::class.java)
    }

    fun invalidate() {
        retrofit = null
        apiClient = null
    }
}