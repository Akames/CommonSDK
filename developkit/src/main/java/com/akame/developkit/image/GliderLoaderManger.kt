package com.akame.developkit.image

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import javax.sql.DataSource

class GliderLoaderManger : ILoader {
    override fun displayImage(options: ImageOptions) {
        if (options.context == null) {
            throw Throwable("context参数不能为null")
        }

        if (options.imageView == null) {
            throw Throwable("imageView参数不能为null")
        }

        val rm: RequestManager = when (val context = options.context) {

            is Activity -> Glide.with(context)

            is Fragment -> Glide.with(context)

            is Context -> Glide.with(context)

            else -> throw Throwable("context参数不合法")
        }

        var apply = rm.load(options.imagePath)
            .apply(configOptions(options))

        options.imageCallBack?.also {
            apply = apply.addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    it.error()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    it.success(resource)
                    return false
                }

            })
        }


        //是否等比例加载
        if (options.isConstrain) {
            apply.into(object : CustomViewTarget<ImageView, Drawable>(options.imageView!!) {
                override fun onLoadFailed(errorDrawable: Drawable?) {

                }

                override fun onResourceCleared(placeholder: Drawable?) {
                }

                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    val width = resource.intrinsicWidth.toFloat()
                    val height = resource.intrinsicHeight.toFloat()
                    var ivWidth = options.imageView!!.width.toFloat()
                    if (ivWidth == 0f) {
                        ivWidth = options.imageView!!.resources.displayMetrics.widthPixels.toFloat()
                    }
                    val ivHeight = (height / width * ivWidth).toInt()
                    val lp = options.imageView!!.layoutParams
                    lp.height = ivHeight
                    options.imageView!!.layoutParams = lp
                    options.imageView!!.setImageDrawable(resource)
                }
            })
        } else {
            apply.into(options.imageView!!)
        }
    }


    private fun configOptions(op: ImageOptions): RequestOptions {
        var options = RequestOptions()
        val transList = ArrayList<Transformation<Bitmap>>()

        if (op.isCenterCrop) {
            transList.add(CenterCrop())
        }

        if (op.isCircleCrop) {
            transList.add(CircleCrop())
        }

        //添加圆角
        if (op.round != -1) {
            transList.add(
                RoundedCornersTransformation(
                    op.round, 0, getImageRoundType(op.roundType)
                )
            )
        }

        //高斯模糊
        if (op.gsBlur != -1) {
            transList.add(BlurTransformation(op.gsBlur, op.gsEnlarge))
        }

        //添加边框
        if (op.borderRound != -1 && op.borderWidth != -1) {
            transList.add(
                GlideCircleTransform(
                    op.context as Context,
                    op.borderWidth,
                    op.borderColor,
                    op.borderRound,
                    op.borderColors
                )
            )
        }

        if (transList.size > 0) {
            options = RequestOptions.bitmapTransform(MultiTransformation(transList))
        }

        if (op.placeholderRes != -1) {
            options = options.placeholder(op.placeholderRes)
        }

        if (op.errorRes != -1) {
            options = options.error(op.errorRes)
        }
        return options

    }

    private fun getImageRoundType(roundType: ImageCornerType): RoundedCornersTransformation.CornerType =
        when (roundType) {
            ImageCornerType.ALL -> RoundedCornersTransformation.CornerType.ALL
            ImageCornerType.TOP_LEFT -> RoundedCornersTransformation.CornerType.TOP_LEFT
            ImageCornerType.TOP_RIGHT -> RoundedCornersTransformation.CornerType.TOP_RIGHT
            ImageCornerType.BOTTOM_LEFT -> RoundedCornersTransformation.CornerType.BOTTOM_LEFT
            ImageCornerType.BOTTOM_RIGHT -> RoundedCornersTransformation.CornerType.BOTTOM_RIGHT
            ImageCornerType.TOP -> RoundedCornersTransformation.CornerType.TOP
            ImageCornerType.BOTTOM -> RoundedCornersTransformation.CornerType.BOTTOM
            ImageCornerType.LEFT -> RoundedCornersTransformation.CornerType.LEFT
            ImageCornerType.RIGHT -> RoundedCornersTransformation.CornerType.RIGHT
            ImageCornerType.OTHER_TOP_LEFT -> RoundedCornersTransformation.CornerType.OTHER_TOP_LEFT
            ImageCornerType.OTHER_TOP_RIGHT -> RoundedCornersTransformation.CornerType.OTHER_TOP_RIGHT
            ImageCornerType.OTHER_BOTTOM_LEFT -> RoundedCornersTransformation.CornerType.OTHER_BOTTOM_LEFT
            ImageCornerType.OTHER_BOTTOM_RIGHT -> RoundedCornersTransformation.CornerType.OTHER_BOTTOM_RIGHT
            ImageCornerType.DIAGONAL_FROM_TOP_LEFT -> RoundedCornersTransformation.CornerType.DIAGONAL_FROM_TOP_LEFT
            ImageCornerType.DIAGONAL_FROM_TOP_RIGHT -> RoundedCornersTransformation.CornerType.DIAGONAL_FROM_TOP_RIGHT
        }

    override fun cleanMemory(context: Context) {
        Glide.get(context).clearMemory()
    }

    override fun clearMemoryCache(context: Context) {
        Glide.get(context).clearDiskCache()
    }

    override fun pauseLoad(context: Context) {
        Glide.with(context).pauseRequests()
    }

    override fun resumeLoad(context: Context) {
        Glide.with(context).resumeRequests()
    }
}