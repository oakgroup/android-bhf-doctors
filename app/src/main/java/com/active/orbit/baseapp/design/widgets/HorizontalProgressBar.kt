package com.active.orbit.baseapp.design.widgets

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.design.utils.UiUtils
import java.lang.Double
import kotlin.Boolean
import kotlin.Float
import kotlin.Int

open class HorizontalProgressBar(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : View(context, attrs, defStyleAttr) {

    private var mProgress = 0f
    private var mMaxProgress = 100f

    private var mRoundedCorners = true

    private var mProgressIconResource = R.drawable.ic_progress
    private var progressIcon = BaseImageView(context)

    private var mLineWidth = UiUtils.dp2px(resources, 35f)
    private var mBackgroundLineColorOne = ContextCompat.getColor(context, R.color.colorPrimaryLight)
    private var mBackgroundLineColorTwo = ContextCompat.getColor(context, R.color.colorPrimaryLight)
    private var mProgressLineColorOne = ContextCompat.getColor(context, R.color.colorAccent)
    private var mProgressLineColorTwo = ContextCompat.getColor(context, R.color.colorAccent)

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var animator: ValueAnimator? = null

    private var lastProgressRatio = Constants.INVALID.toFloat()

    private var hideProgressIcon = false

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!hideProgressIcon) (parent as ViewGroup).removeView(progressIcon)
        drawBackground(canvas)
        if (mProgress > 0f) drawProgress(canvas)
        if (!hideProgressIcon) addProgressIcon(mProgress > 0f)

    }

    private fun drawBackground(canvas: Canvas) {
        val marginStart = paddingStart.toFloat()
        val totalWidth = width.toFloat() - marginStart
        mPaint.shader = LinearGradient(marginStart, mLineWidth, totalWidth, mLineWidth / 2, mBackgroundLineColorOne, mBackgroundLineColorTwo, Shader.TileMode.MIRROR)
        mPaint.strokeWidth = mLineWidth
        mPaint.isAntiAlias = true
        mPaint.strokeCap = if (mRoundedCorners) Paint.Cap.ROUND else Paint.Cap.BUTT
        mPaint.style = Paint.Style.STROKE
        canvas.drawLine(marginStart, mLineWidth / 2, totalWidth, mLineWidth / 2, mPaint)
    }

    private fun drawProgress(canvas: Canvas) {
        val marginStart = paddingStart.toFloat()
        val totalWidth = getProgress() - marginStart
        mPaint.shader = LinearGradient(marginStart, mLineWidth, totalWidth, mLineWidth / 2, mProgressLineColorOne, mProgressLineColorTwo, Shader.TileMode.MIRROR)
        mPaint.strokeWidth = mLineWidth
        mPaint.isAntiAlias = true
        mPaint.strokeCap = if (mRoundedCorners) Paint.Cap.ROUND else Paint.Cap.BUTT
        mPaint.style = Paint.Style.STROKE
        canvas.drawLine(marginStart, mLineWidth / 2, totalWidth, mLineWidth / 2, mPaint)
    }

    private fun addProgressIcon(isVisible: Boolean) {
        val marginStart = paddingStart.toFloat()
        val totalWidth = getProgress() - marginStart
        val coordinateX = totalWidth - UiUtils.dp2px(resources, 3f)
        val coordinateY = mLineWidth / 2
        progressIcon.setImageResource(mProgressIconResource)
        progressIcon.measure(0, 0)
        progressIcon.visibility = if (isVisible) VISIBLE else INVISIBLE
        val size = (mLineWidth - UiUtils.dp2px(resources, 6f)).toInt()
        val params = RelativeLayout.LayoutParams(size, size)
        params.setMargins(coordinateX.toInt() - (size / 2), coordinateY.toInt() + paddingTop - (size / 2), 0, 0)
        (parent as ViewGroup).addView(progressIcon, params)
    }

    /**
     * Set the progress icon resource for the progress bar.
     * @param resourceId the resource identifier
     */
    fun setProgressIconResource(resourceId: Int, forceInvalidate: Boolean = false) {
        post {
            mProgressIconResource = resourceId
            if (forceInvalidate) {
                invalidate()
            }
        }
    }

    fun hideProgressIcon(forceInvalidate: Boolean = false) {
        hideProgressIcon = true
        post {
            if (forceInvalidate) {
                invalidate()
            }
        }
    }

    private fun getProgress(): Float {
        return width / mMaxProgress * mProgress
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
                animator?.end()
                animateProgress(progress)
            }
        }
    }

    private fun animateProgress(progress: Float) {
        animator = ValueAnimator.ofFloat(0f, progress)
        animator?.interpolator = DecelerateInterpolator()
        animator?.duration = Double.max(Constants.PROGRESS_ANIMATION_DURATION, progress * Constants.PROGRESS_ANIMATION_INCREMENT).toLong()
        animator?.addUpdateListener { valueAnimator ->
            mProgress = valueAnimator.animatedValue as Float
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

    fun setMaxProgress(maxProgress: Float, forceInvalidate: Boolean = false) {
        mMaxProgress = maxProgress
        if (forceInvalidate) {
            invalidate()
        }
    }

    fun setBackgroundLineColorOne(color: Int, forceInvalidate: Boolean = false) {
        mBackgroundLineColorOne = color
        if (forceInvalidate) {
            invalidate()
        }
    }

    fun setBackgroundLineColorTwo(color: Int, forceInvalidate: Boolean = false) {
        mBackgroundLineColorTwo = color
        if (forceInvalidate) {
            invalidate()
        }
    }

    fun setProgressLineColorOne(color: Int, forceInvalidate: Boolean = false) {
        mProgressLineColorOne = color
        if (forceInvalidate) {
            invalidate()
        }
    }

    fun setProgressLineColorTwo(color: Int, forceInvalidate: Boolean = false) {
        mProgressLineColorTwo = color
        if (forceInvalidate) {
            invalidate()
        }
    }

    fun setLineWidth(width: Float, forceInvalidate: Boolean = false) {
        mLineWidth = UiUtils.dp2px(resources, width)
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