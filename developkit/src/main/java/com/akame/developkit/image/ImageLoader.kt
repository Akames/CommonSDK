package com.akame.developkit.image

import android.content.Context
import android.widget.ImageView

class ImageLoader<T : Context>(private val context: T) {

    private val imageOptions by lazy { ImageOptions().also { it.context = context } }

    private val loaderManager by lazy { GliderLoaderManger() }

    fun load() {
        loaderManager.displayImage(imageOptions)
    }

    fun cleanMemory(context: Context) {
        loaderManager.cleanMemory(context)
    }

    fun clearMemoryCache(context: Context) {
        loaderManager.clearMemoryCache(context)
    }

    fun pauseLoad(context: Context) {
        loaderManager.pauseLoad(context)
    }

    fun resumeLoad(context: Context) {
        loaderManager.resumeLoad(context)
    }

    fun imagePath(imagePath: Any?): ImageLoader<*> {
        imageOptions.imagePath = imagePath
        return this
    }

    fun blurry(gsBlur: Int, gsEnlarge: Int = 30): ImageLoader<*> {
        imageOptions.gsBlur = gsBlur
        imageOptions.gsEnlarge = gsEnlarge
        return this
    }

    fun round(round: Int, roundType: ImageCornerType = ImageCornerType.ALL): ImageLoader<*> {
        imageOptions.round = round
        imageOptions.roundType = roundType
        return this
    }

    fun border(
        borderWidth: Int,
        borderRound: Int = 0,
        borderColor: Int = -1,
        borderColors: IntArray = intArrayOf()
    ): ImageLoader<*> {
        imageOptions.borderWidth = borderWidth
        imageOptions.borderRound = borderRound
        imageOptions.borderColor = borderColor
        imageOptions.borderColors = borderColors
        return this
    }

    fun isCenterCrop(): ImageLoader<*> {
        imageOptions.isCenterCrop = true
        return this
    }

    fun isCircleCrop(): ImageLoader<*> {
        imageOptions.isCircleCrop = true
        return this
    }

    fun isConstrain(): ImageLoader<*> {
        imageOptions.isConstrain = true
        return this
    }

    fun imageView(imageView: ImageView): ImageLoader<*> {
        imageOptions.imageView = imageView
        return this
    }

    fun callBack(imageCallBack: ImageCallBack): ImageLoader<*> {
        imageOptions.imageCallBack = imageCallBack
        return this
    }

    fun errorRes(errorRes: Int): ImageLoader<*> {
        imageOptions.errorRes = errorRes
        return this
    }
}