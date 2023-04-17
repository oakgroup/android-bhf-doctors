package com.active.orbit.baseapp.core.preferences.engine

import android.content.Context
import android.content.SharedPreferences
import com.active.orbit.baseapp.core.utils.Logger

/**
 * Abstract preferences class that should be extended from all utility
 * preferences classes
 *
 * @author omar.brugna
 */
abstract class BasePreferences {

    protected lateinit var prefs: SharedPreferences

    companion object {

        private const val filename = PreferencesKeys.filename

        fun logout(context: Context) {
            Preferences.backend(context).logout()
            Preferences.lifecycle(context).logout()
            Preferences.user(context).logout()
        }

        fun printAll(context: Context) {
            val prefs = context.getSharedPreferences(filename, Context.MODE_PRIVATE)
            Logger.d("Stored Preferences")
            for ((key, value) in prefs.all)
                Logger.d("$key - $value")
        }
    }

    internal fun setupPreferences(context: Context) {
        prefs = context.getSharedPreferences(filename, Context.MODE_PRIVATE)
    }

    abstract fun logout()
}