package com.active.orbit.baseapp.design.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class BaseViewPager : ViewPager {

    var measureHeight = false
    private var isPagingEnabled = true

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (measureHeight) {
            var measuredHeight = 0
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                child.measure(
                    widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                )
                val childHeight = child.measuredHeight
                if (childHeight > measuredHeight) measuredHeight = childHeight
            }

            if (measuredHeight != 0) measuredHeight =
                MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY)
            super.onMeasure(widthMeasureSpec, measuredHeight)

        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (isPagingEnabled) {
            performClick()
            return super.onTouchEvent(event)
        }
        return false
    }

    override fun performClick(): Boolean {
        if (isPagingEnabled) return super.performClick()
        return false
    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        return isPagingEnabled && super.onInterceptTouchEvent(event)
    }

    fun setPagingEnabled(b: Boolean) {
        isPagingEnabled = b
    }
}