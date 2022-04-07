package com.akame.commonsdk.net

import com.akame.developkit.http.BaseRetrofitClient
import okhttp3.OkHttpClient

class AppHttpClient : BaseRetrofitClient() {
    override fun configOkHttpBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        return builder
    }
}