package com.active.orbit.baseapp.design.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageButton
import com.active.orbit.baseapp.core.utils.Constants

class BaseImageButton : AppCompatImageButton {

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

    override fun setPressed(pressed: Boolean) {
        super.setPressed(pressed)
        alpha = if (pressed) Constants.ALPHA_PRESSED
        else Constants.ALPHA_ENABLED
    }
}