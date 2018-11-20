package com.junlong0716.base.module.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.AttributeSet

import com.junlong0716.base.module.R
import com.youth.banner.Banner

/**
 * @author: 巴黎没有摩天轮Li
 * @description: 圆角banner
 * @date: Created in 下午8:22 2018/1/11
 * @modified by:
 */
class RoundedBanner @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : Banner(context, attrs, defStyle) {

    private var topLeftRadius: Float = 0.toFloat()
    private var topRightRadius: Float = 0.toFloat()
    private var bottomLeftRadius: Float = 0.toFloat()
    private var bottomRightRadius: Float = 0.toFloat()

    private val roundPaint: Paint
    private val imagePaint: Paint

    init {
        setWillNotDraw(false)//如果你继承的是ViewGroup,注意此行,否则draw方法是不会回调的;
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.RoundAngleFrameLayout)
            val radius = ta.getDimension(R.styleable.RoundAngleFrameLayout_bannerRadius, 0f)
            topLeftRadius = ta.getDimension(R.styleable.RoundAngleFrameLayout_bannerTopLeftRadius, radius)
            topRightRadius = ta.getDimension(R.styleable.RoundAngleFrameLayout_bannerTopRightRadius, radius)
            bottomLeftRadius = ta.getDimension(R.styleable.RoundAngleFrameLayout_bannerBottomLeftRadius, radius)
            bottomRightRadius = ta.getDimension(R.styleable.RoundAngleFrameLayout_bannerBottomRightRadius, radius)
            ta.recycle()
        }
        roundPaint = Paint()
        roundPaint.color = Color.WHITE
        roundPaint.isAntiAlias = true
        roundPaint.style = Paint.Style.FILL
        roundPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)

        imagePaint = Paint()
        imagePaint.xfermode = null
    }


    //    @Override
    //    protected void dispatchDraw(Canvas canvas) {
    //        canvas.saveLayer(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), imagePaint, Canvas.ALL_SAVE_FLAG);
    //        super.dispatchDraw(canvas);
    //        drawTopLeft(canvas);
    //        drawTopRight(canvas);
    //        drawBottomLeft(canvas);
    //        drawBottomRight(canvas);
    //        canvas.restore();
    //    }

    override fun draw(canvas: Canvas) {
        canvas.saveLayer(RectF(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat()), imagePaint, Canvas.ALL_SAVE_FLAG)
        super.draw(canvas)
        drawTopLeft(canvas)
        drawTopRight(canvas)
        drawBottomLeft(canvas)
        drawBottomRight(canvas)
        canvas.restore()
    }

    private fun drawTopLeft(canvas: Canvas) {
        if (topLeftRadius > 0) {
            val path = Path()
            path.moveTo(0f, topLeftRadius)
            path.lineTo(0f, 0f)
            path.lineTo(topLeftRadius, 0f)
            path.arcTo(RectF(0f, 0f, topLeftRadius * 2, topLeftRadius * 2),
                    -90f, -90f)
            path.close()
            canvas.drawPath(path, roundPaint)
        }
    }

    private fun drawTopRight(canvas: Canvas) {
        if (topRightRadius > 0) {
            val width = width
            val path = Path()
            path.moveTo(width - topRightRadius, 0f)
            path.lineTo(width.toFloat(), 0f)
            path.lineTo(width.toFloat(), topRightRadius)
            path.arcTo(RectF(width - 2 * topRightRadius, 0f, width.toFloat(),
                    topRightRadius * 2), 0f, -90f)
            path.close()
            canvas.drawPath(path, roundPaint)
        }
    }

    private fun drawBottomLeft(canvas: Canvas) {
        if (bottomLeftRadius > 0) {
            val height = height
            val path = Path()
            path.moveTo(0f, height - bottomLeftRadius)
            path.lineTo(0f, height.toFloat())
            path.lineTo(bottomLeftRadius, height.toFloat())
            path.arcTo(RectF(0f, height - 2 * bottomLeftRadius,
                    bottomLeftRadius * 2, height.toFloat()), 90f, 90f)
            path.close()
            canvas.drawPath(path, roundPaint)
        }
    }

    private fun drawBottomRight(canvas: Canvas) {
        if (bottomRightRadius > 0) {
            val height = height
            val width = width
            val path = Path()
            path.moveTo(width - bottomRightRadius, height.toFloat())
            path.lineTo(width.toFloat(), height.toFloat())
            path.lineTo(width.toFloat(), height - bottomRightRadius)
            path.arcTo(RectF(width - 2 * bottomRightRadius, height - 2 * bottomRightRadius, width.toFloat(), height.toFloat()), 0f, 90f)
            path.close()
            canvas.drawPath(path, roundPaint)
        }
    }
}
