package com.akame.developkit

import android.app.Application
import android.app.Application.getProcessName
import android.content.Context
import android.os.Build
import android.webkit.WebView
import com.akame.developkit.util.SystemUtil
import com.tencent.smtt.sdk.QbSdk


object BaseApp {
    lateinit var app: Application
    fun attachApp(application: Application) {
        app = application
        initX5Web(application.applicationContext)
    }

    private fun initX5Web(context: Context){
        QbSdk.initX5Environment(context,object :QbSdk.PreInitCallback{
            override fun onCoreInitFinished() {

            }

            override fun onViewInitFinished(p0: Boolean) {

            }
        })
    }
}