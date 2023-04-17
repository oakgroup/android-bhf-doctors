package com.active.orbit.baseapp.design.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.core.utils.ImageUtils
import com.bumptech.glide.request.RequestOptions

class BaseImageView : AppCompatImageView {

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
        if (isClickable) {
            alpha = if (pressed) Constants.ALPHA_PRESSED
            else Constants.ALPHA_ENABLED
        }
    }

    fun setImageUrl(context: Context, url: String, @DrawableRes fallbackDrawable: Int) {
        val options = RequestOptions()
            .fallback(fallbackDrawable)
            .error(fallbackDrawable)
            .diskCacheStrategy(Constants.IMAGE_DISK_STRATEGY_AUTOMATIC)

        ImageUtils.getGlide(context)
            ?.load(url)
            ?.apply(options)
            ?.into(this)
    }
}