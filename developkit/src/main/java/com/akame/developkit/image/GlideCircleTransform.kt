package com.akame.developkit.image

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import com.akame.developkit.util.ScreenUtil
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest
import kotlin.math.min

class GlideCircleTransform(
    private val context: Context,
    borderWidth: Int,
    borderColor: Int,
    private val round: Int,
    private var borderColors: IntArray
) : BitmapTransformation() {
    private val mBorderPaint: Paint?
    private val mBorderWidth: Float = Resources.getSystem().displayMetrics.density * borderWidth

    init {
        mBorderPaint = Paint()
        mBorderPaint.isDither = true
        mBorderPaint.isAntiAlias = true
        mBorderPaint.color = if (borderColor == -1) Color.BLACK else borderColor
        mBorderPaint.style = Paint.Style.STROKE
        mBorderPaint.strokeWidth = if (borderWidth == -1) 4f else borderWidth.toFloat()
    }

    override fun transform(
        pool: BitmapPool, toTransform: Bitmap,
        outWidth: Int, outHeight: Int
    ): Bitmap? {
        return if (round > 0)
            filletCrop(context, pool, toTransform)
        else
            circleCrop(pool, toTransform)
    }

    private fun circleCrop(pool: BitmapPool, source: Bitmap?): Bitmap? {
        if (source == null) return null
        val size = (min(source.width, source.height) - mBorderWidth / 2).toInt()
        val x = (source.width - size) / 2
        val y = (source.height - size) / 2
        val squared = Bitmap.createBitmap(source, x, y, size, size)
        val result = pool.get(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        val paint = Paint()
        paint.shader =
            BitmapShader(squared, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.isAntiAlias = true
        val r = size / 2f
        canvas.drawCircle(r, r, r, paint)
        if (mBorderPaint != null) {
            if (borderColors.isNotEmpty() && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                val sw = SweepGradient(
                    r, r, borderColors,
                    null
                )
                mBorderPaint.shader = sw
            }
            val borderRadius = r - mBorderWidth / 2
            canvas.drawCircle(r, r, borderRadius, mBorderPaint)
        }
        return result
    }

    private fun filletCrop(context: Context, pool: BitmapPool, source: Bitmap?): Bitmap? {
        if (source == null) return null
        val w = min(source.width, ScreenUtil.getScreenWidth(context))
        val h = min(source.height, ScreenUtil.getScreenHeight(context))
        val width = (w - mBorderWidth).toInt()
        val height = (h - mBorderWidth).toInt()
        val squared = Bitmap.createBitmap(source, 0, 0, width, height)
        val result = pool.get(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        val paint = Paint()
        paint.shader =
            BitmapShader(squared, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.isAntiAlias = true
        canvas.drawRoundRect(
            RectF(mBorderWidth, mBorderWidth, width.toFloat(), height.toFloat()),
            round.toFloat(), round.toFloat(), paint
        )
        if (mBorderPaint != null) {
            if (borderColors.isNotEmpty() && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                val r = min(w, h) / 2f
                val sw = SweepGradient(
                    r, r, borderColors,
                    null
                )
                mBorderPaint.shader = sw
            }
            canvas.drawRoundRect(
                RectF(
                    mBorderWidth, mBorderWidth,
                    width.toFloat(), height.toFloat()
                ), round.toFloat(), round.toFloat(), mBorderPaint
            )
        }
        return result
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {

    }
}
