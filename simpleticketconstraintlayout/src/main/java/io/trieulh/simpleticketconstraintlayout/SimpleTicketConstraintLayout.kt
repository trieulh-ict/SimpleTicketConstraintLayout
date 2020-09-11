package io.trieulh.simpleticketconstraintlayout

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import io.trieulh.simpleticketconstraintlayout.constants.SimpleCornerType.Companion.OUTER_ROUND
import io.trieulh.simpleticketconstraintlayout.constants.SimpleCornerType.Companion.ROUND
import io.trieulh.simpleticketconstraintlayout.constants.SimpleCornerType.Companion.TRIANGLE
import io.trieulh.simpleticketconstraintlayout.constants.SimpleOrientation
import io.trieulh.simpleticketconstraintlayout.constants.SimpleOrientation.Companion.HORIZONTAL
import io.trieulh.simpleticketconstraintlayout.constants.SimpleOrientation.Companion.VERTICAL
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Created by Trieulh on 16,September,2019
 */
class SimpleTicketConstraintLayout(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {

    private var path = Path()

    //Attributes
    private var mCornerType: Int = ROUND
    private var mInnerRadius: Int = 0
    private var mShadowRadius: Int = 0
    private var mShadowColor: Int = Color.argb(200, 200, 200, 200)
    private var mOrientation: Int = HORIZONTAL
    private var mDividerRatio: Float = 0.0f
    private var mDividerDistance: Int = 0
    private var mShouldDisplayDivider: Boolean = false
    private var mShouldDisplayStroke: Boolean = false
    private var mStrokeWidth: Int = 0
    private var mStrokeColor: Int = Color.BLACK

    // Paint object for coloring and styling
    private val fillPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
        isDither = true
    }

    private val strokePaint = Paint().apply {
        style = Paint.Style.STROKE
        isAntiAlias = true
        isDither = true
    }

    private var points: List<Point> = mutableListOf()

    init {
        setWillNotDraw(false)
        setLayerType(View.LAYER_TYPE_SOFTWARE, fillPaint)
        setLayerType(View.LAYER_TYPE_SOFTWARE, strokePaint)
        initAttrs(attrs)
    }

    private fun initAttrs(attrs: AttributeSet) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.SimpleTicketConstraintLayout)
        mInnerRadius = ta.getDimensionPixelSize(
            R.styleable.SimpleTicketConstraintLayout_ds_inner_corner_radius,
            0
        )
        mCornerType = ta.getInt(R.styleable.SimpleTicketConstraintLayout_ds_corner_type, ROUND)
        mShadowRadius = ta.getDimensionPixelSize(R.styleable.SimpleTicketConstraintLayout_ds_shadow_radius, 0)
        mShadowColor = ta.getColor(
            R.styleable.SimpleTicketConstraintLayout_ds_shadow_color,
            Color.argb(200, 200, 200, 200)
        )
        mOrientation = ta.getInt(R.styleable.SimpleTicketConstraintLayout_ds_orientation, HORIZONTAL)
        mDividerRatio = ta.getFloat(R.styleable.SimpleTicketConstraintLayout_ds_divider_ratio, 0.0f)
        mDividerDistance = ta.getDimensionPixelSize(
            R.styleable.SimpleTicketConstraintLayout_ds_divider_distance,
            0
        )
        mShouldDisplayDivider = ta.getBoolean(R.styleable.SimpleTicketConstraintLayout_ds_should_display_divider, false)
        mShouldDisplayStroke = ta.getBoolean(R.styleable.SimpleTicketConstraintLayout_ds_should_display_stroke, false)
        mStrokeWidth = ta.getDimensionPixelSize(R.styleable.SimpleTicketConstraintLayout_ds_stroke_width, 0)
        mStrokeColor = ta.getColor(R.styleable.SimpleTicketConstraintLayout_ds_stroke_color, Color.BLACK)
        ta.recycle()

        fillPaint.setShadowLayer(mShadowRadius.toFloat(), 0f, 1f, mShadowColor)
        strokePaint.apply {
            strokeWidth = mStrokeWidth.toFloat()
            color = mStrokeColor
        }
    }

    private fun getUpdatedPoints(): List<Point> = mutableListOf(
        Point(0 + getExtraPadding(), mInnerRadius + getExtraPadding()),
        Point(mInnerRadius + getExtraPadding(), 0 + getExtraPadding()),
        Point(measuredWidth - mInnerRadius - getExtraPadding(), 0 + getExtraPadding()),
        Point(measuredWidth - getExtraPadding(), mInnerRadius + getExtraPadding()),
        Point(measuredWidth - getExtraPadding(), measuredHeight - mInnerRadius - getExtraPadding()),
        Point(measuredWidth - mInnerRadius - getExtraPadding(), measuredHeight - getExtraPadding()),
        Point(mInnerRadius + getExtraPadding(), measuredHeight - getExtraPadding()),
        Point(0 + getExtraPadding(), measuredHeight - mInnerRadius - getExtraPadding())
    )

    override fun dispatchDraw(canvas: Canvas) {
        val save = canvas.save()
        canvas.clipPath(path)
        super.dispatchDraw(canvas)
        canvas.restoreToCount(save)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawBackground(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        if (points.isEmpty())
            points = getUpdatedPoints()

        fillPaint.color = Color.WHITE
        path.moveTo(points[0].x.toFloat(), points[0].y.toFloat())
        for (i in 1..8) {
            if (i % 2 == 1) {
                if (mCornerType == TRIANGLE) {
                    path.lineTo(points[i].x.toFloat(), points[i].y.toFloat())
                } else {
                    val rectF: RectF = getRectFByPosition(points[i], i / 2)
                    val angleConvertValue = if (mCornerType == OUTER_ROUND) 90f else 0f
                    val angleConvertRevert = if (mCornerType == OUTER_ROUND) 1f else -1f
                    val startAngle: Float = (1 + i / 2) * 90f + angleConvertValue
                    val sweepAngle: Float = angleConvertRevert * 90f

                    path.arcTo(
                        rectF, startAngle, sweepAngle
                    )
                }
            } else {
                val nextIndex = if (i == 8) 0 else i
                if (mShouldDisplayDivider) {
                    drawArcIfNeed(path, points[i - 1], points[nextIndex])
                }
                path.lineTo(points[nextIndex].x.toFloat(), points[nextIndex].y.toFloat())
            }
        }

        canvas.drawPath(path, fillPaint)
        if (mShouldDisplayStroke)
            canvas.drawPath(path, strokePaint)
    }

    private fun drawArcIfNeed(path: Path, currentPoint: Point, nextPoint: Point) {
        val (direction, point1, point2) = find2Points(currentPoint, nextPoint)
        if (point1 == null || point2 == null) return
        val (rectF, startAngle, sweepAngle) = getCornRectFByPosition(direction, point1, point2)

        path.lineTo(point1.x.toFloat(), point1.y.toFloat())
        path.arcTo(rectF, startAngle, sweepAngle)
    }

    private fun find2Points(currentPoint: Point, nextPoint: Point): Triple<Int, Point?, Point?> {
        val distanceX =
            if (mDividerDistance > 0 && mOrientation == HORIZONTAL) mDividerDistance.toFloat() else abs((currentPoint.x - nextPoint.x)) * mDividerRatio
        val distanceY =
            if (mDividerDistance > 0 && mOrientation == VERTICAL) mDividerDistance.toFloat() else abs((currentPoint.y - nextPoint.y)) * mDividerRatio

        val centerX: Int = (distanceX + min(currentPoint.x, nextPoint.x)).roundToInt()
        val centerY: Int = (distanceY + min(currentPoint.y, nextPoint.y)).roundToInt()

        return when {
            mDividerDistance >= (if (mOrientation == HORIZONTAL) abs((currentPoint.x - nextPoint.x)) else abs((currentPoint.y - nextPoint.y))) ->
                Triple(
                    NONE,
                    null,
                    null
                )

            isHorizontalLineDirection(currentPoint, nextPoint) && nextPoint.x > currentPoint.x ->
                Triple(
                    TOP,
                    Point(centerX - mInnerRadius, centerY),
                    Point(centerX + mInnerRadius, centerY)
                )

            isHorizontalLineDirection(currentPoint, nextPoint) && nextPoint.x < currentPoint.x ->
                Triple(
                    BOTTOM,
                    Point(centerX + mInnerRadius, centerY),
                    Point(centerX - mInnerRadius, centerY)
                )

            isVerticalLineDirection(currentPoint, nextPoint) && nextPoint.y > currentPoint.y ->
                Triple(
                    RIGHT,
                    Point(centerX, centerY - mInnerRadius),
                    Point(centerX, centerY + mInnerRadius)
                )

            isVerticalLineDirection(currentPoint, nextPoint) && nextPoint.y < currentPoint.y ->
                Triple(
                    LEFT,
                    Point(centerX, centerY + mInnerRadius),
                    Point(centerX, centerY - mInnerRadius)
                )

            else -> Triple(NONE, null, null)
        }
    }

    private fun isVerticalLineDirection(currentPoint: Point, nextPoint: Point): Boolean {
        return currentPoint.x.toFloat() == nextPoint.x.toFloat() && mOrientation == VERTICAL
    }

    private fun isHorizontalLineDirection(currentPoint: Point, nextPoint: Point): Boolean {
        return currentPoint.y.toFloat() == nextPoint.y.toFloat() && mOrientation == HORIZONTAL
    }

    private fun getCornRectFByPosition(
        direction: Int,
        point1: Point,
        point2: Point
    ): Triple<RectF, Float, Float> {
        return when (direction) {
            TOP -> Triple(
                RectF(
                    point1.x.toFloat(),
                    (point1.y - mInnerRadius).toFloat(),
                    point2.x.toFloat(),
                    (point2.y + mInnerRadius).toFloat()
                ),
                180f,
                -180f
            )
            RIGHT -> Triple(
                RectF(
                    (point1.x - mInnerRadius).toFloat(),
                    (point1.y).toFloat(),
                    (point2.x + mInnerRadius).toFloat(),
                    point2.y.toFloat()
                ),
                270f,
                -180f
            )
            BOTTOM -> Triple(
                RectF(
                    point2.x.toFloat(),
                    (point2.y - mInnerRadius).toFloat(),
                    point1.x.toFloat(),
                    (point1.y + mInnerRadius).toFloat()
                ),
                0f,
                -180f
            )
            // LEFT
            else -> Triple(
                RectF(
                    (point2.x - mInnerRadius).toFloat(),
                    (point2.y).toFloat(),
                    (point1.x + mInnerRadius).toFloat(),
                    point1.y.toFloat()
                ),
                90f,
                -180f
            )
        }
    }

    private fun getRectFByPosition(point: Point, pos: Int): RectF {
        val centerAxisConvertValue = if (mCornerType == OUTER_ROUND) mInnerRadius else 0
        return when (pos) {
            // TOP LEFT - DESTINATION RIGHT
            RECT_RIGHT -> RectF(
                (point.x - 2 * mInnerRadius).toFloat() + centerAxisConvertValue,
                (point.y - mInnerRadius).toFloat() + centerAxisConvertValue,
                point.x.toFloat() + centerAxisConvertValue,
                (point.y + mInnerRadius).toFloat() + centerAxisConvertValue
            )
            // TOP RIGHT - DESTINATION BOT
            RECT_BOT -> RectF(
                (point.x - mInnerRadius).toFloat() - centerAxisConvertValue,
                (point.y - 2 * mInnerRadius).toFloat() + centerAxisConvertValue,
                (point.x + mInnerRadius).toFloat() - centerAxisConvertValue,
                (point.y).toFloat() + centerAxisConvertValue
            )
            // BOT RIGHT - DESTINATION LEFT
            RECT_LEFT -> RectF(
                (point.x).toFloat() - centerAxisConvertValue,
                (point.y - mInnerRadius).toFloat() - centerAxisConvertValue,
                (point.x + 2 * mInnerRadius).toFloat() - centerAxisConvertValue,
                (point.y + mInnerRadius).toFloat() - centerAxisConvertValue
            )
            // BOT LEFT - DESTINATION TOP
            else -> RectF(
                (point.x - mInnerRadius).toFloat() + centerAxisConvertValue,
                (point.y).toFloat() - centerAxisConvertValue,
                (point.x + mInnerRadius).toFloat() + centerAxisConvertValue,
                (point.y + 2 * mInnerRadius).toFloat() - centerAxisConvertValue
            )
        }
    }

    private fun getExtraPadding() = mShadowRadius + mStrokeWidth

    fun setCornerType(@SimpleOrientation type: Int) {
        mCornerType = type
        invalidate()
    }

    fun setDividerDistanceByPixel(distance: Int) {
        mDividerDistance = distance
        invalidate()
    }

    fun setDividerDistanceByRatio(ratio: Float) {
        mDividerRatio = ratio
        invalidate()
    }

    fun setCornerRadiusByPixel(radius: Int) {
        mInnerRadius = radius
        invalidate()
    }

    fun setOrientation(@SimpleOrientation orientation: Int) {
        mOrientation = orientation
        invalidate()
    }

    fun setShadowRadiusByPixel(radius: Int) {
        mShadowRadius = radius
        invalidate()
    }

    fun shouldDisplayDivider(shouldDisplay: Boolean) {
        mShouldDisplayDivider = shouldDisplay
        invalidate()
    }

    fun shouldDisplayStroke(shouldDisplay: Boolean) {
        mShouldDisplayStroke = shouldDisplay
        invalidate()
    }

    fun setStrokeColor(@ColorInt color: Int) {
        mStrokeColor = color
        invalidate()
    }

    fun setStrokeWidthByPixel(width: Int) {
        mStrokeWidth = width
        invalidate()
    }

    companion object {
        // Rect of corners
        private const val RECT_RIGHT = 0
        private const val RECT_BOT = 1
        private const val RECT_LEFT = 2
        private const val RECT_TOP = 3

        // Side
        private const val NONE = -1
        private const val TOP = 0
        private const val RIGHT = 1
        private const val BOTTOM = 2
        private const val LEFT = 3
    }
}