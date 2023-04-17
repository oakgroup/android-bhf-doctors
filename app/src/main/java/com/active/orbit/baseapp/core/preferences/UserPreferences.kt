package com.active.orbit.baseapp.core.preferences

import com.active.orbit.baseapp.core.preferences.engine.BasePreferences
import com.active.orbit.baseapp.core.preferences.engine.PreferencesKeys
import com.active.orbit.baseapp.core.utils.Constants

class UserPreferences : BasePreferences() {

    var id: String?
        get() = prefs.getString(PreferencesKeys.preference_user_id, Constants.EMPTY)
        set(value) {
            val editor = prefs.edit()
            editor.putString(PreferencesKeys.preference_user_id, value)
            editor.apply()
        }

    var name: String?
        get() = prefs.getString(PreferencesKeys.preference_user_name, Constants.EMPTY)
        set(value) {
            val editor = prefs.edit()
            editor.putString(PreferencesKeys.preference_user_name, value)
            editor.apply()
        }

    override fun logout() {
        id = null
        name = null
    }
}