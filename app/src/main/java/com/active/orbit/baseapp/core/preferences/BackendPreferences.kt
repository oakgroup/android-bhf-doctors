package com.active.orbit.baseapp.core.preferences

import com.active.orbit.baseapp.core.preferences.engine.BasePreferences
import com.active.orbit.baseapp.core.preferences.engine.PreferencesKeys
import com.active.orbit.baseapp.core.utils.Constants

class BackendPreferences : BasePreferences() {

    var baseUrl: String
        get() = prefs.getString(PreferencesKeys.preference_backend_base_url, Constants.EMPTY) ?: Constants.EMPTY
        set(value) {
            val editor = prefs.edit()
            editor.putString(PreferencesKeys.preference_backend_base_url, value)
            editor.apply()
        }

    override fun logout() {
        // baseUrl = Constants.EMPTY
    }
}