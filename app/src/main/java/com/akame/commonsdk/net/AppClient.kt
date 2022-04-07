package com.akame.commonsdk.net

object AppClient {

    val client by lazy {
        crateRetrofit()
    }

    private fun crateRetrofit(): Api {
        val client = AppHttpClient()
        return client.create(Api::class.java, "https://www.wanandroid.com/")
    }
}