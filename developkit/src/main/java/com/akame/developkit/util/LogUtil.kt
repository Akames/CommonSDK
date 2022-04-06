package com.akame.developkit.util

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Method

object LogUtil {

    fun d(msg: String) {
        log(PRIORITY.DEBUG, msg)
    }

    fun i(msg: String) {
        log(PRIORITY.INFO, msg)
    }

    fun w(msg: String) {
        log(PRIORITY.WARN, msg)
    }

    fun e(msg: String) {
        log(PRIORITY.ERROR, msg)
    }

    private fun log(priority: PRIORITY, msg: String) {
        val stackTrace = Thread.currentThread().stackTrace
        val index = 4
        val className = stackTrace[index].fileName
        val methodName = stackTrace[index].methodName
        val lineNun = stackTrace[index].lineNumber
        val methodInfo = "[(${className}:${lineNun})#${methodName}]"
        try {
            printLog(priority, className, "╔═══════════════════════════════════════════════════════════════════════════════════════")
            print(priority, className, "ThreadName：${Thread.currentThread().name}")
            print(priority, className, "invoke: $methodInfo")
            print(priority, className, "${print(priority, className, msg)}")
            printLog(priority, className, "╚═══════════════════════════════════════════════════════════════════════════════════════")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun print(priority: PRIORITY, className: String, msg: String) {
        val lines = msg.split("\n")
        lines.forEach {
            if (isJson(it)) {
                printJson(priority, className, it)
            } else {
                printLog(priority, className, "║ $it")
            }
        }
    }

    private fun isJson(json: String) = json.startsWith("{") || json.startsWith("[")

    private fun printJson(priority: PRIORITY, className: String, json: String) {
        val jsonFlit = when {
            json.startsWith("{") -> {
                val jsonObject = JSONObject(json)
                jsonObject.toString(4)
            }
            json.startsWith("[") -> {
                val jsonArray = JSONArray(json)
                jsonArray.toString(4)
            }
            else -> {
                printLog(priority, className, "║ $json")
                return
            }
        }
        val message = System.getProperty("line.separator") + jsonFlit
        val toTypedArray = message.split(System.getProperty("line.separator")).toTypedArray()
        for (line in toTypedArray) {
            printLog(priority, className, "║ $line")
        }
    }

    private fun printLog(priority: PRIORITY, tag: String, logMsg: String) {
        when (priority) {
            PRIORITY.DEBUG -> Log.d(tag, logMsg)
            PRIORITY.INFO -> Log.i(tag, logMsg)
            PRIORITY.WARN -> Log.w(tag, logMsg)
            PRIORITY.ERROR -> Log.e(tag, logMsg)
        }
    }

    enum class PRIORITY {
        DEBUG,
        INFO,
        WARN,
        ERROR
    }
}