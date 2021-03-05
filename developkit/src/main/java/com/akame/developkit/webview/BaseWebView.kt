package com.akame.developkit.webview

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.webkit.WebSettings
import android.webkit.WebView

/**
 * @Author: Administrator
 * @Date: 2018/10/16
 * @Description: 基础webView
 */
class BaseWebView(context: Context, attributeSet: AttributeSet? = null) :
    WebView(context, attributeSet) {
    init {
        config()
    }

    private fun config() {
        val webSettings = settings
        //5.0以上开启混合模式加载
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
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
        /* this.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });*/
    }
}