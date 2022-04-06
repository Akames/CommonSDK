package com.akame.developkit.image

import android.content.Context

interface ILoader {
    fun displayImage(options: ImageOptions)

    fun pauseLoad(context: Context)

    fun resumeLoad(context: Context)

    fun clearMemoryCache(context: Context)

    fun cleanMemory(context: Context)
}