package com.active.orbit.baseapp.design.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatCheckBox
import com.active.orbit.baseapp.core.providers.FontProvider
import com.active.orbit.baseapp.core.utils.Logger

class BaseCheckBox : AppCompatCheckBox {

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
        try {
            typeface = FontProvider.getRegular(context)
        } catch (e: Exception) {
            Logger.e("Error loading font ${e.localizedMessage}")
        }
    }
}