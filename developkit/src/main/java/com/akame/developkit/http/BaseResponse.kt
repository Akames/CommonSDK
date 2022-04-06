package com.akame.developkit.http

interface BaseResponse {
    fun isRequestSuccess(): Boolean

    fun getErrorMsg(): String

    fun onRequestFail()
}