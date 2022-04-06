package com.akame.developkit.util

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

/**
 * 系统高度测量
 */
object ScreenUtil {
    private var statusBarHeight = 0 //状态栏高度

    val Number.dp get() = dip2px(this.toFloat())
    val Number.sp get() = sp2pix(this.toFloat())

    /**
     * dp转化px
     */
    fun dip2px(dpValue: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpValue,
            Resources.getSystem().displayMetrics
        ).toInt()
    }

    /**
     * px转话dp
     */
    fun pix2dip(pxValue: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX,
            pxValue,
            Resources.getSystem().displayMetrics
        ).toInt()
    }

    fun sp2pix(spValue: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            spValue,
            Resources.getSystem().displayMetrics
        ).toInt()
    }

    /**
     * 获取屏幕宽度
     */
    fun getScreenWidth(context: Context): Int = context.resources.displayMetrics.widthPixels

    /**
     * 获取屏幕高度
     */
    fun getScreenHeight(context: Context): Int = context.resources.displayMetrics.heightPixels

    /**
     * 获取状态栏高度
     */
    fun getStatusBarHeight(context: Context): Int {
        if (statusBarHeight != 0) {
            return statusBarHeight
        }
        //获取状态栏高度的资源id
        val resourceId =
            context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = context.resources.getDimensionPixelSize(resourceId)
        }
        return statusBarHeight
    }
}