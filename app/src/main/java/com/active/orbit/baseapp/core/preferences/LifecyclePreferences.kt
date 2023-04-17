package com.active.orbit.baseapp.core.preferences

import com.active.orbit.baseapp.core.preferences.engine.BasePreferences
import com.active.orbit.baseapp.core.preferences.engine.PreferencesKeys
import com.active.orbit.baseapp.core.utils.Constants

class LifecyclePreferences : BasePreferences() {

    var firstInstall: Long?
        get() = prefs.getLong(PreferencesKeys.preference_lifecycle_first_install, Constants.INVALID.toLong())
        set(value) {
            val editor = prefs.edit()
            if (value != null) editor.putLong(PreferencesKeys.preference_lifecycle_first_install, value)
            else editor.remove(PreferencesKeys.preference_lifecycle_first_install)
            editor.apply()
        }

    override fun logout() {
        firstInstall = null
    }
}