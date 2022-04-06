package com.akame.developkit.image

import android.graphics.drawable.Drawable

interface ImageCallBack {

    fun success(drawable: Drawable?)

    fun error()
}