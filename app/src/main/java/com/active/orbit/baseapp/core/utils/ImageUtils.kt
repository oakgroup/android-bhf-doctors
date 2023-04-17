package com.active.orbit.baseapp.core.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager

/**
 * Utility class that provides some useful methods to manage images
 *
 * @author omar.brugna
 */
object ImageUtils {

    fun getGlide(context: Context?): RequestManager? {
        if (context != null) {
            if (context !is AppCompatActivity || !context.isDestroyed) {
                // initialize glide only when context is not null and
                // if context is not an instance of a destroyed activity
                return Glide.with(context)
            }
        }
        return null
    }
}