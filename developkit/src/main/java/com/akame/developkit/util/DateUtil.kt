package com.akame.developkit.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    private val YYYYMMDDHHMMSS_FORMAT: SimpleDateFormat =
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    private val HHMMSS_FORMAT =
        SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    private val HHMMSS_FORMAT2 =
        SimpleDateFormat("HH时mm分ss秒", Locale.getDefault())

    /**
     * 将年月日时分秒转成Long类型
     * @param dateTime
     * @return
     */
    fun dateTimeToTimeStamp(dateTime: String?): Long {
        return try {
            val e: Date = YYYYMMDDHHMMSS_FORMAT.parse(dateTime)
            java.lang.Long.valueOf(e.time / 1000)
        } catch (var2: ParseException) {
            var2.printStackTrace()
            java.lang.Long.valueOf(0L)
        }
    }

    /**
     * 将Long类型转成时分秒
     * @param timeStamp
     * @return
     */
    fun timeStampToDateTime(timeStamp: Long): String? {
        return HHMMSS_FORMAT.format(Date(timeStamp))
    }
}