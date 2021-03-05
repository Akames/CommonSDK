package com.akame.developkit.util

import android.util.Log
import com.akame.developkit.BuildConfig
import org.json.JSONArray
import org.json.JSONObject

object LogUtil {
    fun showLog(msg: String) {
        if (BuildConfig.DEBUG) {
            val stackTrace = Thread.currentThread().stackTrace
            val index = 5
            val className = stackTrace[index].fileName
            val methodName = stackTrace[index].methodName
            val lineNun = stackTrace[index].lineNumber
            Log.d(className, "[(${className}:${lineNun}#${methodName})]: $msg")
        }
    }


    fun showJson(msg: String) {
        val stackTrace = Thread.currentThread().stackTrace
        val index = 5
        val className = stackTrace[index].fileName
        var message = ""
        message = when {
            msg.startsWith("{") -> {
                val jsonObject = JSONObject(msg)
                jsonObject.toString(4)
            }
            msg.startsWith("[") -> {
                val jsonArray = JSONArray(msg)
                jsonArray.toString(4)
            }
            else -> {
                msg
            }
        }

        printLine(className, true)
        message = System.getProperty("line.separator") + message
        val toTypedArray = message.split(System.getProperty("line.separator")).toTypedArray()
        for (line in toTypedArray) {
            Log.d(className, "║ $line")
        }
        printLine(className, false)
    }


    private fun printLine(tag: String?, isTop: Boolean) {
        if (isTop) {
            Log.d(
                tag,
                "╔═══════════════════════════════════════════════════════════════════════════════════════"
            )
        } else {
            Log.d(
                tag,
                "╚═══════════════════════════════════════════════════════════════════════════════════════"
            )
        }
    }
}