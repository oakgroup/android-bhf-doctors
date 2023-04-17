package com.active.orbit.baseapp.design.recyclers.engine

import android.content.Context
import android.util.AttributeSet
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.recyclerview.widget.RecyclerView
import com.active.orbit.baseapp.core.utils.Logger

/**
 * Custom recyclerview that should be extended from all the recyclerviews
 *
 * @author omar.brugna
 */
class BaseRecyclerView : RecyclerView {

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
        itemAnimator?.changeDuration = 0
        itemAnimator = null
        setHasFixedSize(true)

        addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                onScroll()
            }
        })
    }

    private fun onScroll() {

    }

    fun scrollToTop(animated: Boolean) {
        post {
            if (animated) smoothScrollToPosition(0)
            else scrollToPosition(0)
        }
    }

    fun scrollToBottom(animated: Boolean) {
        post {
            if (animated) smoothScrollToPosition(layoutManager!!.itemCount - 1)
            else scrollToPosition(layoutManager!!.itemCount - 1)
        }
    }

    override fun onInitializeAccessibilityEvent(event: AccessibilityEvent?) {
        try {
            // fixed launch crashes on some devices
            super.onInitializeAccessibilityEvent(event)
        } catch (e: Exception) {
            Logger.e("Exception in onInitializeAccessibilityEvent")
        }
    }

    override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo?) {
        try {
            // fixed launch crashes on some devices
            super.onInitializeAccessibilityNodeInfo(info)
        } catch (e: Exception) {
            Logger.e("Exception in onInitializeAccessibilityNodeInfo")
        }
    }
}