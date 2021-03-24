package com.future.wanandroid.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toBitmap

/**
 * Created by yangqc on 2021/3/24
 * 圆形图片
 */
class CircleImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    AppCompatImageView(context, attrs, defStyleAttr) {

    private var mSize = 0
    private var mPaint: Paint = Paint()
    private var mPorterDuffXfermode: Xfermode

    init {
        mPaint.isDither = true
        mPaint.isAntiAlias = true
        mPorterDuffXfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measuredWidth
        val height = measuredHeight
        mSize = Math.min(width, height) //取宽高最小值
        setMeasuredDimension(mSize, mSize) //重新设置大小
    }

    override fun onDraw(canvas: Canvas) {
        if (drawable == null) return
        //获取sourceBitmap，即通过xml或者java设置进来的图片
        val sourceBitmap = drawable.toBitmap()
        val bitmap = resizeBitmap(sourceBitmap, width, height)
//        drawCircleBitmapByXfermode(canvas, bitmap)
            drawCircleBitmapByShader(canvas, bitmap)
    }

    private fun resizeBitmap(sourceBitmap: Bitmap, dstWidth: Int, dstHeight: Int): Bitmap {
        val width = sourceBitmap.width
        val height = sourceBitmap.height

        val widthScale = dstWidth / width.toFloat()
        val heightScale = dstHeight / width.toFloat()

        //取最大缩放比
        val scale = Math.max(widthScale, heightScale)
        val matrix = Matrix()
        matrix.postScale(scale, scale)
        return Bitmap.createBitmap(sourceBitmap, 0, 0, width, height, matrix, true)
    }

    private fun drawCircleBitmapByXfermode(canvas: Canvas, bitmap: Bitmap) {
        val sc = canvas.saveLayer(0F, 0F, width.toFloat(), height.toFloat(), null)
        //绘制dst层
        canvas.drawCircle((mSize / 2).toFloat(), (mSize / 2).toFloat(), (mSize / 2).toFloat(), mPaint)
        //设置图层混合模式为SRC_IN
        mPaint.setXfermode(mPorterDuffXfermode)
        //绘制src层
        canvas.drawBitmap(bitmap, 0F, 0F, mPaint)
        canvas.restoreToCount(sc)
    }

    private fun drawCircleBitmapByShader(canvas: Canvas, bitmap: Bitmap) {
        val shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        mPaint.shader = shader
        canvas.drawCircle((mSize / 2).toFloat(), (mSize / 2).toFloat(), (mSize / 2).toFloat(), mPaint)
    }


}