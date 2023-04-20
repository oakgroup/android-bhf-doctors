package com.active.orbit.baseapp.design.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.active.orbit.baseapp.R
import com.google.android.gms.location.DetectedActivity

object ActivityUtils {

    fun getIcon(context: Context, activityType: Int): Drawable? {
        return when (activityType) {
            // TODO George review icons
            DetectedActivity.STILL -> ContextCompat.getDrawable(context, R.drawable.ic_walking)
            DetectedActivity.WALKING, DetectedActivity.ON_FOOT -> ContextCompat.getDrawable(context, R.drawable.ic_walking)
            DetectedActivity.RUNNING -> ContextCompat.getDrawable(context, R.drawable.ic_walking)
            DetectedActivity.ON_BICYCLE -> ContextCompat.getDrawable(context, R.drawable.ic_cycling)
            DetectedActivity.IN_VEHICLE -> ContextCompat.getDrawable(context, R.drawable.ic_driving)
            else -> ContextCompat.getDrawable(context, R.drawable.ic_walking)
        }
    }

    fun getName(context: Context, activityType: Int): String {
        return when (activityType) {
            DetectedActivity.STILL -> context.getString(R.string.still)
            DetectedActivity.WALKING, DetectedActivity.ON_FOOT -> context.getString(R.string.walking)
            DetectedActivity.RUNNING -> context.getString(R.string.running)
            DetectedActivity.ON_BICYCLE -> context.getString(R.string.cycling)
            DetectedActivity.IN_VEHICLE -> context.getString(R.string.vehicle)
            else -> context.getString(R.string.walking)
        }
    }
}