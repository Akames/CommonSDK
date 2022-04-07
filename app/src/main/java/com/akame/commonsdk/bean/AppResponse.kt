package com.akame.commonsdk.bean

import com.akame.developkit.http.BaseResponse
import com.google.gson.annotations.SerializedName

data class AppResponse<T>(
    val data: T,
    val errorCode: Int,
    @SerializedName("errorMsg")
    val msg: String
) : BaseResponse {
    override fun isRequestSuccess(): Boolean = errorCode == 0

    override fun getErrorMsg(): String = msg

    override fun onRequestFail() {

    }
}