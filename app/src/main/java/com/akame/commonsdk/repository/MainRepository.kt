package com.akame.commonsdk.repository

import com.akame.commonsdk.bean.AppResponse
import com.akame.commonsdk.bean.BannerBean
import com.akame.commonsdk.net.AppClient

class MainRepository {
    suspend fun loadBanner(): AppResponse<List<BannerBean>> {
        return AppClient.client.getBanner()
    }
}