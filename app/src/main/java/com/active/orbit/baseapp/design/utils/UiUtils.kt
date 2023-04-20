package com.active.orbit.baseapp.design.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextUtils
import android.util.DisplayMetrics
import android.widget.Toast
import com.active.orbit.baseapp.core.utils.Constants
import java.util.*

object UiUtils {
    /**
     * Simple Android short Toast
     *
     * @param context an instance of [Context]
     * @param resourceId text to show
     */
    fun showShortToast(context: Context, resourceId: Int) {
        showToast(context, context.resources.getString(resourceId), Toast.LENGTH_SHORT)
    }

    /**
     * Simple Android short Toast
     *
     * @param context an instance of [Context]
     * @param text text to show
     */
    fun showShortToast(context: Context, text: String) {
        showToast(context, text, Toast.LENGTH_SHORT)
    }

    /**
     * Simple Android long Toast
     *
     * @param context an instance of [Context]
     * @param resourceId text to show
     */
    fun showLongToast(context: Context, resourceId: Int) {
        showToast(context, context.resources.getString(resourceId), Toast.LENGTH_LONG)
    }

    /**
     * Simple Android short Toast
     *
     * @param context an instance of [Context]
     * @param text text to show
     */
    fun showLongToast(context: Context, text: String) {
        showToast(context, text, Toast.LENGTH_LONG)
    }

    /**
     * Simple Android Toast
     *
     * @param context an instance of [Context]
     * @param text text to show
     */
    fun showToast(context: Context, text: String, length: Int) {
        Toast.makeText(context, text, length).show()
    }

    fun dp2px(resources: Resources, dp: Float): Float {
        val scale = resources.displayMetrics.density
        return dp * scale + 0.5f
    }

    fun px2dp(resources: Resources, px: Float): Float {
        return px / (resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun sp2px(resources: Resources, sp: Float): Float {
        val scale = resources.displayMetrics.scaledDensity
        return sp * scale
    }

    fun getScreenWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }

    fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    /**
     * https://stackoverflow.com/questions/33671196/floatingactionbutton-with-text-instead-of-image
     *
     * @param text button text
     * @param textSize button text size
     * @param textColor button text color
     * @return the bitmap button
     */
    fun textAsBitmap(text: String, textSize: Float, textColor: Int): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.textSize = textSize
        paint.color = textColor
        paint.textAlign = Paint.Align.LEFT
        val baseline = -paint.ascent() // ascent() is negative
        val width = (paint.measureText(text) + 0.0f).toInt() // round
        val height = (baseline + paint.descent() + 0.0f).toInt()
        val image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(image)
        canvas.drawText(text, 0f, baseline, paint)
        return image
    }

    fun capitalize(str: String?): String {
        if (!TextUtils.isEmpty(str)) {
            return str!!.substring(0, 1).uppercase(Locale.getDefault()) + str.substring(1)
        }
        return Constants.EMPTY
    }

}