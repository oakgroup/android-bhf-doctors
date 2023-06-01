package com.active.orbit.baseapp.core.preferences

import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.preferences.engine.BasePreferences
import com.active.orbit.baseapp.core.utils.Constants

class LifecyclePreferences : BasePreferences() {

    var firstInstall: Long?
        get() = prefs.getLong(res.getString(R.string.preference_lifecycle_first_install_key), Constants.INVALID.toLong())
        set(value) {
            val editor = prefs.edit()
            if (value != null) editor.putLong(res.getString(R.string.preference_lifecycle_first_install_key), value)
            else editor.remove(res.getString(R.string.preference_lifecycle_first_install_key))
            editor.apply()
        }

    var welcomeShown: Boolean
        get() = prefs.getBoolean(res.getString(R.string.preference_lifecycle_welcome_shown_key), false)
        set(value) {
            val editor = prefs.edit()
            editor.putBoolean(res.getString(R.string.preference_lifecycle_welcome_shown_key), value)
            editor.apply()
        }

    var onboardingshown: Boolean
        get() = prefs.getBoolean(res.getString(R.string.preference_lifecycle_onboarding_shown_key), false)
        set(value) {
            val editor = prefs.edit()
            editor.putBoolean(res.getString(R.string.preference_lifecycle_onboarding_shown_key), value)
            editor.apply()
        }

    var isPrivacyPolicyAccepted: Boolean
        get() = prefs.getBoolean(res.getString(R.string.preference_lifecycle_privacy_policy_accepted_key), false)
        set(value) {
            val editor = prefs.edit()
            editor.putBoolean(res.getString(R.string.preference_lifecycle_privacy_policy_accepted_key), value)
            editor.apply()
        }

    var userDetailsUploaded: Boolean
        get() = prefs.getBoolean(res.getString(R.string.preference_lifecycle_user_details_uploaded_key), false)
        set(value) {
            val editor = prefs.edit()
            editor.putBoolean(res.getString(R.string.preference_lifecycle_user_details_uploaded_key), value)
            editor.apply()
        }

    var notificationScheduled: Int
        get() = prefs.getInt(res.getString(R.string.preference_lifecycle_notification_scheduled), Constants.INVALID)
        set(value) {
            val editor = prefs.edit()
            editor.putInt(res.getString(R.string.preference_lifecycle_notification_scheduled), value)
            editor.apply()
        }


    override fun logout() {
        firstInstall = null
        welcomeShown = false
        userDetailsUploaded = false
    }
}