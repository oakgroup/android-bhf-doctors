package com.active.orbit.baseapp.design.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ScrollView

/**
 * * Custom scrollView that should be extended from all the recyclerviews
 *
 * @author omar.brugna
 */
class BaseScrollView : ScrollView {

    private val mContext: Context

    constructor(context: Context) : super(context) {
        mContext = context
        customize()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mContext = context
        customize()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mContext = context
        customize()
    }

    private fun customize() {
        overScrollMode = View.OVER_SCROLL_IF_CONTENT_SCROLLS
        isVerticalScrollBarEnabled = false
        isHorizontalScrollBarEnabled = false
    }
}