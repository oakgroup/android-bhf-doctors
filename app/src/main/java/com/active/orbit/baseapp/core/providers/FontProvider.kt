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
            Constants.TYPEFACE_SEMIBOLD_INDEX -> getSemibold(context)
            Constants.TYPEFACE_ITALIC_INDEX -> getItalic(context)
            Constants.TYPEFACE_BLACK_INDEX -> getBlack(context)
            Constants.TYPEFACE_LIGHT_INDEX -> getLight(context)
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

    fun getSemibold(context: Context): Typeface {
        return Typeface.createFromAsset(context.assets, Constants.TYPEFACE_SEMIBOLD)
    }

    fun getItalic(context: Context): Typeface {
        return Typeface.createFromAsset(context.assets, Constants.TYPEFACE_ITALIC)
    }

    fun getBlack(context: Context): Typeface {
        return Typeface.createFromAsset(context.assets, Constants.TYPEFACE_BLACK)
    }

    fun getLight(context: Context): Typeface {
        return Typeface.createFromAsset(context.assets, Constants.TYPEFACE_LIGHT)
    }

    fun getBoldItalic(context: Context): Typeface {
        return Typeface.createFromAsset(context.assets, Constants.TYPEFACE_BOLD_ITALIC)
    }
}