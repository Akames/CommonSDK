package com.akame.developkit.net

import com.akame.developkit.showLog
import com.google.gson.JsonParseException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

abstract class BaseServerEngine : ServerImpl, CoroutineScope by MainScope() {
    override fun launchService(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: (String) -> Unit,
        finalBlock: () -> Unit
    ) {
        launch(Dispatchers.Main) {
            try {
                tryBlock()
            } catch (e: Exception) {
                when (e) {
                    is HttpException -> {
                        catchBlock(analyzeError(NetError(EXCEPTION_HTTP, e)) ?: "网络错误")
                    }

                    is JsonParseException -> {
                        catchBlock(analyzeError(NetError(EXCEPTION_JSON, e)) ?: "解析错误")
                    }

                    is ConnectException, is SocketException, is UnknownHostException -> {
                        catchBlock(analyzeError(NetError(EXCEPTION_CONNECT, e)) ?: "连接失败")
                    }

                    is SSLHandshakeException -> {
                        catchBlock(analyzeError(NetError(EXCEPTION_SSL, e)) ?: "证书验证失败")
                    }

                    else -> {
                        catchBlock(analyzeError(NetError(EXCEPTION_OTHER, e)) ?: "未知错误")
                    }
                }
                showLog(e.message ?: "")
            } finally {
                finalBlock()
            }
        }
    }

    abstract fun analyzeError(err: NetError): String?

    data class NetError(val code: Int, val e: Exception)


    protected val EXCEPTION_HTTP = 0x11
    protected val EXCEPTION_JSON = 0x12
    protected val EXCEPTION_CONNECT = 0x13
    protected val EXCEPTION_SSL = 0x14
    protected val EXCEPTION_OTHER = 0x15
}