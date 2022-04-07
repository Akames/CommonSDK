package com.akame.developkit

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.akame.developkit.image.ImageLoader
import com.akame.developkit.util.LogUtil
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

fun Activity.showToast(msg: Any) {
    Toast.makeText(this, msg.toString(), Toast.LENGTH_SHORT).show()
}

fun Fragment.showToast(msg: Any) {
    if (!requireActivity().isFinishing) {
        Toast.makeText(activity, msg.toString(), Toast.LENGTH_SHORT).show()
    }
}

fun showMsg(context: Context, msg: Any) {
    Toast.makeText(context, msg.toString(), Toast.LENGTH_SHORT).show()
}

inline fun <reified T> Context.launchActivity(block: (Bundle.() -> Unit) = {}) {
    val bundle = Bundle()
    block.invoke(bundle)
    val intent = Intent(this, T::class.java)
    startActivity(intent.putExtras(bundle))
}

@SuppressLint("SetJavaScriptEnabled")
fun WebView.configSetting() {
    val webSettings = settings
    //5.0以上开启混合模式加载
    webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
    webSettings.loadWithOverviewMode = true
    webSettings.useWideViewPort = true
    //允许js代码
    webSettings.javaScriptEnabled = true
    //允许js弹框
    webSettings.javaScriptCanOpenWindowsAutomatically = true
    //禁用放缩
    webSettings.displayZoomControls = false
    webSettings.builtInZoomControls = false
    //禁用文字缩放
    webSettings.textZoom = 100
    // 调整到适合webView大小
    webSettings.useWideViewPort = true
    // 调整到适合webView大小
    webSettings.loadWithOverviewMode = true
    //自动加载图片
    webSettings.loadsImagesAutomatically = true
    // 开启 DOM storage API 功能
    webSettings.domStorageEnabled = true
}

var itemTime = 0L
fun View.setOnClickListen(method: () -> Unit) {
    setOnClickListener {
        if (System.currentTimeMillis() - itemTime > 300) {
            itemTime = System.currentTimeMillis()
            method.invoke()
        }
    }
}

fun Map<*, *>.covertRequestBody() =
    Gson().toJson(this).toRequestBody("application/json;charset=utf-8".toMediaType())


fun ImageView.loadEngine(imagePath: Any?): ImageLoader<*> {
    return ImageLoader(this.context).imageView(this).imagePath(imagePath)
}




