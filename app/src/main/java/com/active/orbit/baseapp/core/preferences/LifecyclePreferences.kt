package com.active.orbit.baseapp.core.preferences

import com.active.orbit.baseapp.R
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

    var welcomeShown: Boolean
        get() = prefs.getBoolean(res.getString(R.string.preference_lifecycle_welcome_shown), false)
        set(value) {
            val editor = prefs.edit()
            editor.putBoolean(res.getString(R.string.preference_lifecycle_welcome_shown), value)
            editor.apply()
        }

    var onboardingshown: Boolean
        get() = prefs.getBoolean(res.getString(R.string.preference_lifecycle_onboarding_shown), false)
        set(value) {
            val editor = prefs.edit()
            editor.putBoolean(res.getString(R.string.preference_lifecycle_onboarding_shown), value)
            editor.apply()
        }

    var isPrivacyPolicyAccepted: Boolean
        get() = prefs.getBoolean(res.getString(R.string.preference_lifecycle_privacy_policy_accepted), false)
        set(value) {
            val editor = prefs.edit()
            editor.putBoolean(res.getString(R.string.preference_lifecycle_privacy_policy_accepted), value)
            editor.apply()
        }

    var userDetailsUploaded: Boolean
        get() = prefs.getBoolean(res.getString(R.string.preference_lifecycle_user_details_uploaded), false)
        set(value) {
            val editor = prefs.edit()
            editor.putBoolean(res.getString(R.string.preference_lifecycle_user_details_uploaded), value)
            editor.apply()
        }



    override fun logout() {
        firstInstall = null
        welcomeShown = false
        userDetailsUploaded = false
    }
}