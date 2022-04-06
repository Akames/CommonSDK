package com.akame.developkit.http

sealed class SeverResult<T>(val data: T? = null, val exception: Exception? = null) {
    class Loading : SeverResult<Nothing>()

    class Success<T>(data: T?) : SeverResult<T>(data)

    class Error(exception: Exception?) : SeverResult<Nothing>(exception = exception)
}