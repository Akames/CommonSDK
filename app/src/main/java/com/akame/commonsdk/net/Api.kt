package com.akame.commonsdk.net

import com.akame.commonsdk.bean.AppResponse
import com.akame.commonsdk.bean.BannerBean
import retrofit2.http.GET

interface Api {
    @GET("banner/json")
    suspend fun getBanner(): AppResponse<List<BannerBean>>
}