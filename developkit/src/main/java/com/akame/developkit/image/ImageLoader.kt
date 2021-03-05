package com.akame.developkit.image

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.signature.ObjectKey
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

object ImageLoader {
    fun with(context: Context): ImageOptions.Builder {
        return ImageOptions.Builder().apply {
            this.context = context
        }
    }

    fun with(context: Activity): ImageOptions.Builder {
        return ImageOptions.Builder().apply {
            this.activity = context
        }
    }

    fun with(context: Fragment): ImageOptions.Builder {
        return ImageOptions.Builder().apply {
            this.fragment = context
        }
    }

    fun pauseLoad(context: Context) {
        ImageOptions.Builder().builder().pauseLoad(context)
    }

    fun resumeLoad(context: Context) {
        ImageOptions.Builder().builder().resumeLoad(context)
    }

    fun cleanMemory(context: Context) {
        ImageOptions.Builder().builder().cleanMemory(context)
    }

    fun clearMemoryCache(context: Context) {
        ImageOptions.Builder().builder().clearMemoryCache(context)
    }

    fun loadListener(imageView: ImageView, url: String, imageCallBack: ImageCallBack? = null) {
        Glide.with(imageView.context)
            .load(url)
            .into(object : CustomViewTarget<ImageView, Drawable>(imageView) {
                override fun onLoadFailed(errorDrawable: Drawable?) {
                    imageCallBack?.error()
                }

                override fun onResourceCleared(placeholder: Drawable?) {

                }

                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    imageCallBack?.success()
                    imageView.setImageDrawable(resource)
                }
            })
    }


    fun loadBitmap(
        context: Context,
        url: String,
        bitmapCallBack: (Bitmap) -> Unit,
        error: () -> Unit
    ) {
        Glide.with(context)
            .asBitmap()
            .load(url)
            .into(object : SimpleTarget<Bitmap>() {
                override fun onLoadFailed(errorDrawable: Drawable?) {
                    error.invoke()
                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    bitmapCallBack.invoke(resource)
                }
            })
    }
}