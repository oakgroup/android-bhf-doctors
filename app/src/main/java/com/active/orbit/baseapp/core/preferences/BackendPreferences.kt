package com.active.orbit.baseapp.core.preferences

import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.preferences.engine.BasePreferences
import com.active.orbit.baseapp.core.utils.Constants

class BackendPreferences : BasePreferences() {

    var baseUrl: String
        get() = prefs.getString(res.getString(R.string.preference_backend_base_url_key), Constants.EMPTY) ?: Constants.EMPTY
        set(value) {
            val editor = prefs.edit()
            editor.putString(res.getString(R.string.preference_backend_base_url_key), value)
            editor.apply()
        }

    override fun logout() {
        // baseUrl = Constants.EMPTY
    }
}