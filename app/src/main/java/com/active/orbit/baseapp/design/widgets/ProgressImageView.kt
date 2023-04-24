package com.active.orbit.baseapp.design.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.active.orbit.baseapp.design.utils.UiUtils

class ProgressImageView : AppCompatImageView {

    companion object {
        const val SIZE_IN_DP = 30f
    }

    constructor(context: Context) : super(context) {
        customize()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        customize()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        customize()
    }

    private fun customize() {

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = UiUtils.dp2px(context.resources, SIZE_IN_DP).toInt()
        super.onMeasure(size, size)
    }
}