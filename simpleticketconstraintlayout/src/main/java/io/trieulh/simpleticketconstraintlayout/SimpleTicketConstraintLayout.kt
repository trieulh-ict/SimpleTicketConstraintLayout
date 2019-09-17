package io.trieulh.simpleticketconstraintlayout

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout

/**
 * Created by Trieulh on 16,September,2019
 */
class SimpleTicketConstraintLayout(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {

    //Attributes
    private var mCornerType: Int = ROUND
    private var mInnerRadius: Int = 0
    private var mShadowRadius: Int = 0
    private var mShadowColor: Int = Color.argb(200, 200, 200, 200)

    // Paint object for coloring and styling
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val points by lazy {
        listOf(
            Point(0 + mShadowRadius, mInnerRadius + mShadowRadius),
            Point(mInnerRadius + mShadowRadius, 0 + mShadowRadius),
            Point(measuredWidth - mInnerRadius - mShadowRadius, 0 + mShadowRadius),
            Point(measuredWidth - mShadowRadius, mInnerRadius + mShadowRadius),
            Point(measuredWidth - mShadowRadius, measuredHeight - mInnerRadius - mShadowRadius),
            Point(measuredWidth - mInnerRadius - mShadowRadius, measuredHeight - mShadowRadius),
            Point(mInnerRadius + mShadowRadius, measuredHeight - mShadowRadius),
            Point(0 + mShadowRadius, measuredHeight - mInnerRadius - mShadowRadius)
        )
    }


    init {
        setWillNotDraw(false)
        setLayerType(View.LAYER_TYPE_SOFTWARE, paint)
        initAttrs(attrs)
    }

    private fun initAttrs(attrs: AttributeSet) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.SimpleTicketConstraintLayout)
        mInnerRadius =
            ta.getDimensionPixelSize(
                R.styleable.SimpleTicketConstraintLayout_inner_corner_radius,
                0
            )
        mCornerType = ta.getInt(R.styleable.SimpleTicketConstraintLayout_corner_type, ROUND)
        mShadowRadius =
            ta.getDimensionPixelSize(R.styleable.SimpleTicketConstraintLayout_shadow_radius, 0)
        mShadowColor = ta.getColor(
            R.styleable.SimpleTicketConstraintLayout_shadow_color,
            Color.argb(200, 200, 200, 200)
        )

        ta.recycle()

        paint.setShadowLayer(mShadowRadius.toFloat(), 0f, 1f, mShadowColor)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawBackground(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        val path = Path()
        paint.color = Color.WHITE

        path.moveTo(points[0].x.toFloat(), points[0].y.toFloat())
        for (i in 1..7) {
            if (i % 2 == 1) {
                if (mCornerType == TRIANGLE) {
                    path.lineTo(points[i].x.toFloat(), points[i].y.toFloat())
                } else {
                    val rectF: RectF = getRectFByPosition(points[i], i / 2)
                    val startAngle: Float = (1 + i / 2).toInt() * 90f
                    val sweepAngle: Float = -90f

                    path.arcTo(
                        rectF, startAngle, sweepAngle
                    )
                }
            } else {
                path.lineTo(points[i].x.toFloat(), points[i].y.toFloat())
            }
        }

        canvas.drawPath(path, paint)
    }

    private fun getRectFByPosition(point: Point, pos: Int): RectF {
        return when (pos) {
            RECT_RIGHT -> RectF(
                (point.x - 2 * mInnerRadius).toFloat(),
                (point.y - mInnerRadius).toFloat(),
                point.x.toFloat(),
                (point.y + mInnerRadius).toFloat()
            )
            RECT_BOT -> RectF(
                (point.x - mInnerRadius).toFloat(),
                (point.y - 2 * mInnerRadius).toFloat(),
                (point.x + mInnerRadius).toFloat(),
                (point.y).toFloat()
            )
            RECT_LEFT -> RectF(
                (point.x).toFloat(),
                (point.y - mInnerRadius).toFloat(),
                (point.x + 2 * mInnerRadius).toFloat(),
                (point.y + mInnerRadius).toFloat()
            )
            else -> RectF(
                (point.x - mInnerRadius).toFloat(),
                (point.y).toFloat(),
                (point.x + mInnerRadius).toFloat(),
                (point.y + 2 * mInnerRadius).toFloat()
            )
        }
    }

    companion object {
        private const val ROUND = 0
        private const val TRIANGLE = 1

        private const val RECT_RIGHT = 0
        private const val RECT_BOT = 1
        private const val RECT_LEFT = 2
        private const val RECT_TOP = 3
    }
}