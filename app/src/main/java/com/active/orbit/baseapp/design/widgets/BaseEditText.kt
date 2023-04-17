package com.active.orbit.baseapp.design.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.providers.FontProvider
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.core.utils.Logger

class BaseEditText : AppCompatEditText {

    val textTrim: String
        get() = text?.toString()?.trim() ?: Constants.EMPTY

    constructor(context: Context) : super(context) {
        customize(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        customize(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        customize(attrs)
    }

    @SuppressLint("CustomViewStyleable")
    private fun customize(attrs: AttributeSet?) {
        var typefaceIndex = Constants.TYPEFACE_REGULAR_INDEX
        val ta = context.obtainStyledAttributes(attrs, R.styleable.BaseText, 0, 0)
        try {
            typefaceIndex = ta.getInt(R.styleable.BaseText_typeface, Constants.TYPEFACE_REGULAR_INDEX)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            ta.recycle()
        }

        try {
            typeface = FontProvider.getFromIndex(context, typefaceIndex)
        } catch (e: Exception) {
            Logger.e("Error loading font ${e.localizedMessage}")
        }
    }

    fun clear() {
        setText(Constants.EMPTY)
    }
}