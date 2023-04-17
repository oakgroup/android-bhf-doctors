package com.active.orbit.baseapp.design.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatSpinner

class BaseSpinner : AppCompatSpinner {

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
}