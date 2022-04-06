package com.akame.developkit.http

import com.akame.developkit.util.LogUtil
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.StringBuilder
import java.util.concurrent.TimeUnit

abstract class BaseRetrofitClient {
    fun <S> create(serverClass: Class<S>, baseUrl: String): S =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(serverClass)

    private val client: OkHttpClient
        get() {
            val builder = OkHttpClient.Builder()
                .connectTimeout(HttpConfig.connectTimeout, TimeUnit.SECONDS)
                .writeTimeout(HttpConfig.writeTimeout, TimeUnit.SECONDS)
                .readTimeout(HttpConfig.readTimeout, TimeUnit.SECONDS)
                .retryOnConnectionFailure(HttpConfig.retryConnect)
                .addInterceptor(createLoggerInterceptor())
            return configOkHttpBuilder(builder).build()
        }

    protected abstract fun configOkHttpBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder

    private fun createLoggerInterceptor(): Interceptor {
        var logBuilder = StringBuilder()
        val logger = object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                when {
                    message.startsWith("--> END") || message.startsWith("<-- END") -> {
                        logBuilder.append("\n")
                        logBuilder.append(message)
                        LogUtil.d(logBuilder.toString())
                    }
                    message.startsWith("-->") || message.startsWith("<--") -> {
                        logBuilder = StringBuilder()
                        logBuilder.append(message)
                    }
                    else -> {
                        logBuilder.append("\n")
                        logBuilder.append(message)
                    }
                }
            }
        }
        val httpLoggingInterceptor = HttpLoggingInterceptor(logger)
        val level = if (HttpConfig.printLogEnable) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        httpLoggingInterceptor.setLevel(level)
        return httpLoggingInterceptor
    }
}