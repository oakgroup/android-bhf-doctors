package com.active.orbit.baseapp.design.widgets

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.utils.BaseException
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.design.utils.UiUtils
import java.lang.Double
import kotlin.Boolean
import kotlin.Float
import kotlin.Int
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

open class CircularProgressBar(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : View(context, attrs, defStyleAttr) {

    private var mAngleStart = ANGLE_START
    private var mAngleEnd = ANGLE_END
    private var mAngleProgress = ANGLE_PROGRESS

    companion object {
        const val ANGLE_START = -90f
        const val ANGLE_END = 360f
        const val ANGLE_PROGRESS = 0f
        const val FULL_CIRCLE = 360f
    }

    private var lastProgressRatio = Constants.INVALID.toFloat()

    private var mProgress = 0f
    private var mMaxProgress = 100f

    private var mDrawText = false
    private var mRoundedCorners = false

    private var mProgressIconResource = R.drawable.ic_progress
    private var progressIcon = BaseImageView(context)
    private var mDrawIcon = false

    private var mLineWidth = UiUtils.dp2px(resources, 20f)
    private var mTextColor = ContextCompat.getColor(context, R.color.textColorPrimaryDark)
    private var mBackgroundLineColor = ContextCompat.getColor(context, R.color.gray)
    private var mProgressLineColor = ContextCompat.getColor(context, R.color.colorPrimary)

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var animator: ValueAnimator? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        (parent as ViewGroup).removeView(progressIcon)
        drawBackground(canvas)
        drawProgress(canvas)
        if (mDrawText) drawText(canvas)
        if (mDrawIcon) addProgressIcon()
    }

    private fun reset() {
        mAngleStart = ANGLE_START
        mAngleEnd = ANGLE_END
        mAngleProgress = ANGLE_PROGRESS
    }

    private fun drawBackground(canvas: Canvas) {
        val diameter = min(width, height) - mLineWidth / 2
        val rect = RectF(mLineWidth / 2.toFloat(), mLineWidth / 2.toFloat(), diameter, diameter)
        mPaint.color = mBackgroundLineColor
        mPaint.strokeWidth = mLineWidth
        mPaint.isAntiAlias = true
        mPaint.strokeCap = if (mRoundedCorners) Paint.Cap.ROUND else Paint.Cap.BUTT
        mPaint.style = Paint.Style.STROKE
        canvas.drawArc(rect, mAngleStart, mAngleEnd, false, mPaint)
    }

    private fun drawProgress(canvas: Canvas) {
        val diameter = min(width, height) - mLineWidth / 2
        val rect = RectF(mLineWidth / 2.toFloat(), mLineWidth / 2.toFloat(), diameter, diameter)
        mPaint.color = mProgressLineColor
        mPaint.strokeWidth = mLineWidth
        mPaint.isAntiAlias = true
        mPaint.strokeCap = if (mRoundedCorners) Paint.Cap.ROUND else Paint.Cap.BUTT
        mPaint.style = Paint.Style.STROKE
        canvas.drawArc(rect, mAngleStart, mAngleProgress, false, mPaint)
    }

    private fun drawText(canvas: Canvas) {
        mPaint.textSize = min(width, height) / 5f
        mPaint.textAlign = Paint.Align.CENTER
        mPaint.strokeWidth = 0F
        mPaint.color = mTextColor

        // center text
        val xPos: Int = canvas.width / 2
        val yPos = (canvas.height / 2 - (mPaint.descent() + mPaint.ascent()) / 2).toInt()
        canvas.drawText("$mProgress%", xPos.toFloat(), yPos.toFloat(), mPaint)
    }

    private fun getAngleFromProgress(): Float {
        return mAngleEnd / mMaxProgress * mProgress
    }

    private fun addProgressIcon() {
        val size = UiUtils.dp2px(context.resources, ProgressImageView.SIZE_IN_DP).toInt()
        val radius = (min(width, height) - mLineWidth) / 2
        val centerX = width / 2
        val centerY = height / 2
        val progressAngle = FULL_CIRCLE / mMaxProgress * mProgress + ANGLE_START
        val progressAngleRadians = Math.toRadians(progressAngle.toDouble())
        val coordinateX = cos(progressAngleRadians) * radius + centerX
        val coordinateY = sin(progressAngleRadians) * radius + centerY
        progressIcon.setImageResource(mProgressIconResource)
        progressIcon.measure(size, size)
        val params = RelativeLayout.LayoutParams(size, size)
        params.setMargins(coordinateX.toInt() - (size / 2), coordinateY.toInt() - (size / 2), 0, 0)
        (parent as ViewGroup).addView(progressIcon, params)
    }

    /**
     * Set the progress icon resource for the progress bar.
     * @param resourceId the resource identifier
     */
    fun setProgressIconResource(resourceId: Int) {
        post {
            mProgressIconResource = resourceId
            invalidate()
        }
    }

    fun showProgressIcon(show: Boolean) {
        post {
            mDrawIcon = show
            invalidate()
        }
    }

    /**
     * Set progress of the circular progress bar.
     * @param progress progress between 0 and 100.
     */
    fun setProgress(progress: Float) {
        val newProgressRatio = progress / mMaxProgress
        if (lastProgressRatio != newProgressRatio) {
            lastProgressRatio = newProgressRatio
            post {
                animator?.removeAllUpdateListeners()
                animator?.end()
                animateProgress(progress)
            }
        }
    }

    open fun animateProgress(progress: Float) {
        animator = ValueAnimator.ofFloat(0f, progress)
        animator?.interpolator = DecelerateInterpolator()
        animator?.duration = Double.max(Constants.PROGRESS_ANIMATION_DURATION, progress * Constants.PROGRESS_ANIMATION_INCREMENT).toLong()
        animator?.addUpdateListener { valueAnimator ->
            mProgress = valueAnimator.animatedValue as Float
            mAngleProgress = getAngleFromProgress()
            invalidate()
        }
        animator?.start()
    }

    fun progress(): Float {
        return mProgress
    }

    fun maxProgress(): Float {
        return mMaxProgress
    }

    fun setMaxProgress(progressMax: Float) {
        if (mMaxProgress <= 0) throw BaseException("The max progress must be greater than zero")
        if (mMaxProgress != progressMax) {
            mMaxProgress = progressMax
            invalidate()
        }
    }

    fun setBackgroundLineColor(color: Int, forceInvalidate: Boolean = false) {
        mBackgroundLineColor = color
        if (forceInvalidate) {
            invalidate()
        }
    }

    fun setProgressLineColor(color: Int, forceInvalidate: Boolean = false) {
        mProgressLineColor = color
        if (forceInvalidate) {
            invalidate()
        }
    }

    fun setLineWidth(lineWidth: Float, forceInvalidate: Boolean = false) {
        mLineWidth = UiUtils.dp2px(resources, lineWidth)
        if (forceInvalidate) {
            invalidate()
        }
    }

    fun setTextColor(color: Int, forceInvalidate: Boolean = false) {
        mTextColor = color
        if (forceInvalidate) {
            invalidate()
        }
    }

    fun showProgressText(show: Boolean, forceInvalidate: Boolean = false) {
        mDrawText = show
        if (forceInvalidate) {
            invalidate()
        }
    }

    /**
     * Toggle this if you don't want rounded corners on progress bar.
     * Default is true.
     * @param roundedCorners true if you want rounded corners of false otherwise.
     */
    fun useRoundedCorners(roundedCorners: Boolean, forceInvalidate: Boolean = false) {
        mRoundedCorners = roundedCorners
        if (forceInvalidate) {
            invalidate()
        }
    }

}