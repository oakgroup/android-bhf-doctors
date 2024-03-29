package com.active.orbit.baseapp.core.utils

import android.util.Log
import com.active.orbit.baseapp.BuildConfig

/**
 * Utility class to log with a specific tag
 *
 * @author omar.brugna
 */
object Logger {

    private const val TAG = "MovingHealth"

    fun v(text: String) {
        v(TAG, text)
    }

    fun d(text: String) {
        d(TAG, text)
    }

    fun i(text: String) {
        i(TAG, text)
    }

    fun w(text: String) {
        w(TAG, text)
    }

    fun e(text: String) {
        e(TAG, text)
    }

    fun v(tag: String, text: String) {
        if (BuildConfig.DEBUG) Log.v(tag, text)
    }

    fun d(tag: String, text: String) {
        if (BuildConfig.DEBUG) Log.d(tag, text)
    }

    fun i(tag: String, text: String) {
        if (BuildConfig.DEBUG) Log.i(tag, text)
    }

    fun w(tag: String, text: String) {
        if (BuildConfig.DEBUG) Log.w(tag, text)
    }

    fun e(tag: String, text: String) {
        if (BuildConfig.DEBUG) Log.e(tag, text)
    }
}