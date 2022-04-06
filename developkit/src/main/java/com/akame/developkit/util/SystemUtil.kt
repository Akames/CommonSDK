package com.akame.developkit.util

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.text.TextUtils
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File

object SystemUtil {

    /**
     * 获取版本号
     */
    fun getVersionCode(context: Context): Long =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            context.packageManager.getPackageInfo(context.packageName, 0).longVersionCode
        } else {
            context.packageManager.getPackageInfo(context.packageName, 0).versionCode.toLong()
        }

    /**
     * 获取版本名称
     */
    fun getVersionName(context: Context): String =
        context.packageManager.getPackageInfo(context.packageName, 0).versionName

    /**
     * 获取包名
     */
    fun getPackName(context: Context): String = context.packageName

    /**
     * 是否有SD卡
     */
    fun hasSDCard() = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

    /**
     * 判断是否程序已安装
     */
    fun isInstallApk(context: Context, packName: String): Boolean {
        val packManger = context.packageManager
        val packList = packManger.getInstalledPackages(0) // 获取系统已经安装程序列表
        packList.forEach {
            if (it.packageName == packName) {
                return true
            }
        }
        return false
    }

    /**
     * 安装apk  调用这个方法需要判断是否拥有安装权限
     */
    fun installApk(context: Context, apkPath: String, authority: String) {
        val apk = File(apkPath)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            val contentUri = FileProvider.getUriForFile(context, authority, apk)
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive")
        } else {
            intent.setDataAndType(Uri.fromFile(apk), "application/vnd.android.package-archive")
        }
        context.startActivity(intent)
    }

    /**
     * 跳转外部应用安装权限
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun intentInstallApkPermission(context: Context) {
        val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    /**
     * 获取当前进程名字
     */
    fun getCurrentProcessName(context: Context): String {
        val pid = android.os.Process.myPid()
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        am.runningAppProcesses.forEach {
            if (it.pid == pid) {
                return it.processName
            }
        }
        return ""
    }

    /**
     * 判断当前进程是否为主进程
     */
    fun isMainProcess(context: Context) = TextUtils.equals(
        getCurrentProcessName(context), context.packageName
    )

    fun isTablet(context: Context): Boolean {
        return ((context.resources.configuration.screenLayout
                and Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE)
    }


    /**
     * 弹出软键盘
     */
    fun showSoftKeyboard(context: Context) {
        val inputMethodManager: InputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    fun showSoftKeyboard(view: View, delayTime: Long = 300) {
        view.postDelayed({
            val inputMethodManager: InputMethodManager =
                view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
        }, delayTime)
    }

    /**
     * 隐藏软键盘
     */
    fun hideSoftKeyboard(context: Context, view: View) {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * 震动反馈
     */
    fun hapticFeedback(view: View) {
        view.performHapticFeedback(
            HapticFeedbackConstants.LONG_PRESS,
            HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
        )
    }

    /**
     * 获取渠道号
     */
    fun getChannelId(context: Context, defChannel: String): String {
        var channelName = defChannel
        try {
            context.packageManager?.let {
                val applicationInfo =
                    it.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
                applicationInfo.metaData?.apply {
                    channelName = applicationInfo.metaData.get("UMENG_CHANNEL").toString()
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace();
        }
        return channelName
    }

    /**
     * 当前屏幕是否为横屏
     */
    fun isLandScape(activity: Activity) = activity.requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

    /**
     * 设置屏幕方向
     */
    fun setLayoutOrientation(activity: Activity, isLandScape: Boolean) {
        activity.requestedOrientation = if (isLandScape) {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    /**
     * 隐藏系统状态栏图标等信息
     */
    fun hideSystemUI(activity: Activity) {
        activity.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

}