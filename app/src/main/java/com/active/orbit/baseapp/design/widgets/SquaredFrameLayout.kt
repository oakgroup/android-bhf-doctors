package com.active.orbit.baseapp.design.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * Custom FrameLayout forced to be squared
 *
 * @author omar.brugna
 */
class SquaredFrameLayout : FrameLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measuredWidth
        setMeasuredDimension(width, width)
    }
}