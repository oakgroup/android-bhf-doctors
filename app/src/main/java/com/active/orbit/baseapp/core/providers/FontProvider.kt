package com.active.orbit.baseapp.core.providers

import android.content.Context
import android.graphics.Typeface
import com.active.orbit.baseapp.core.utils.Constants

/**
 * Utility class that should be used to access every font
 *
 * @author omar.brugna
 */
object FontProvider {

    fun getFromIndex(context: Context, index: Int): Typeface {
        return when (index) {
            Constants.TYPEFACE_BOLD_INDEX -> getBold(context)
            Constants.TYPEFACE_BOLD_ITALIC_INDEX -> getBoldItalic(context)
            else -> getRegular(context)
        }
    }

    fun getRegular(context: Context): Typeface {
        return Typeface.createFromAsset(context.assets, Constants.TYPEFACE_REGULAR)
    }

    fun getBold(context: Context): Typeface {
        return Typeface.createFromAsset(context.assets, Constants.TYPEFACE_BOLD)
    }

    fun getBoldItalic(context: Context): Typeface {
        return Typeface.createFromAsset(context.assets, Constants.TYPEFACE_BOLD_ITALIC)
    }
}