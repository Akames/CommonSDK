package com.akame.developkit.util

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.WindowManager

class NotchDisplayUtils {
    /**
     * 判断是否有刘海屏
     */
    fun hasNotchInScreen(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val displayCutout = activity.window?.decorView?.rootWindowInsets?.displayCutout
            if (displayCutout != null) {
                // 说明有刘海屏
                return true
            }
        }
        when {
            RomUtil.isMiui -> return hasNotchMi(activity)
            RomUtil.isOppo -> return hasNotchOPPO(activity)
            RomUtil.isVivo -> return hasNotchVIVO(activity)
            RomUtil.isEmui -> return hasNotchHw(activity)
        }
        return false
    }


    /**
     * 判断vivo是否有刘海屏
     * https://swsdl.vivo.com.cn/appstore/developer/uploadfile/20180328/20180328152252602.pdf
     *
     * @param activity
     * @return
     */
    private fun hasNotchVIVO(activity: Activity): Boolean {
        return try {
            val c = Class.forName("android.util.FtFeature")
            val get = c.getMethod("isFeatureSupport", Int::class.javaPrimitiveType!!)
            get.invoke(c, 0x20) as Boolean
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }

    }

    /**
     * 判断oppo是否有刘海屏
     * https://open.oppomobile.com/wiki/doc#id=10159
     *
     * @param activity
     * @return
     */
    private fun hasNotchOPPO(activity: Activity): Boolean {
        return activity.packageManager.hasSystemFeature("com.oppo.feature.screen.heteromorphism")
    }

    /**
     * 判断xiaomi是否有刘海屏
     * https://dev.mi.com/console/doc/detail?pId=1293
     *
     * @param activity
     * @return
     */
    @SuppressLint("PrivateApi")
    private fun hasNotchMi(activity: Activity): Boolean {
        return try {
            val c = Class.forName("android.os.SystemProperties")
            val get = c.getMethod("getInt", String::class.java, Int::class.javaPrimitiveType)
            get.invoke(c, "ro.miui.notch", 0) as Int == 1
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }

    }

    /**
     * 判断华为是否有刘海屏
     * https://devcenter-test.huawei.com/consumer/cn/devservice/doc/50114
     *
     * @param activity
     * @return
     */
    private fun hasNotchHw(activity: Activity): Boolean {
        return try {
            val cl = activity.classLoader
            val hwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil")
            val get = hwNotchSizeUtil.getMethod("hasNotchInScreen")
            get.invoke(hwNotchSizeUtil) as Boolean
        } catch (e: Exception) {
            false
        }

    }
}