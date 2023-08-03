package com.active.orbit.baseapp.core.preferences

import android.text.TextUtils
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.preferences.engine.BasePreferences
import com.active.orbit.baseapp.core.utils.Constants
import com.google.firebase.crashlytics.FirebaseCrashlytics

class UserPreferences : BasePreferences() {

    internal fun register(idUser: String) {
        this.idUser = idUser
    }

    internal fun isUserRegistered(): Boolean {
        return !TextUtils.isEmpty(idUser)
    }

    var idUser: String?
        get() = prefs.getString(res.getString(R.string.preference_user_id_user_key), Constants.EMPTY)
        set(value) {
            val editor = prefs.edit()
            editor.putString(res.getString(R.string.preference_user_id_user_key), value)
            editor.apply()

            if (!TextUtils.isEmpty(value)) FirebaseCrashlytics.getInstance().setCustomKey("id_user", value!!)
        }

    var userNhsNumber: String?
        get() = prefs.getString(res.getString(R.string.preference_user_id_patient_key), Constants.EMPTY)
        set(value) {
            val editor = prefs.edit()
            editor.putString(res.getString(R.string.preference_user_id_patient_key), value)
            editor.apply()

            if (!TextUtils.isEmpty(value)) FirebaseCrashlytics.getInstance().setCustomKey("id_user", value!!)
        }

    var userSex: String?
        get() = prefs.getString(res.getString(R.string.preference_user_sex_key), Constants.EMPTY)
        set(value) {
            val editor = prefs.edit()
            editor.putString(res.getString(R.string.preference_user_sex_key), value)
            editor.apply()
        }

    var userFirstName: String?
        get() = prefs.getString(res.getString(R.string.preference_user_first_name_key), Constants.EMPTY)
        set(value) {
            val editor = prefs.edit()
            if (value != null) editor.putString(res.getString(R.string.preference_user_first_name_key), value)
            else editor.remove(res.getString(R.string.preference_user_first_name_key))
            editor.apply()
        }

    var userLastName: String?
        get() = prefs.getString(res.getString(R.string.preference_user_last_name_key), Constants.EMPTY)
        set(value) {
            val editor = prefs.edit()
            if (value != null) editor.putString(res.getString(R.string.preference_user_last_name_key), value)
            else editor.remove(res.getString(R.string.preference_user_last_name_key))
            editor.apply()
        }

    var userDateOfBirth: Long?
        get() = prefs.getLong(res.getString(R.string.preference_user_date_of_birth_key), Constants.INVALID.toLong())
        set(value) {
            val editor = prefs.edit()
            if (value != null) editor.putLong(res.getString(R.string.preference_user_date_of_birth_key), value)
            else editor.remove(res.getString(R.string.preference_user_date_of_birth_key))
            editor.apply()
        }

    var userPostcode: String?
        get() = prefs.getString(res.getString(R.string.preference_user_postcode_key), Constants.EMPTY)
        set(value) {
            val editor = prefs.edit()
            if (value != null) editor.putString(res.getString(R.string.preference_user_postcode_key), value)
            else editor.remove(res.getString(R.string.preference_user_postcode_key))
            editor.apply()
        }

    var userEmail: String?
        get() = prefs.getString(res.getString(R.string.preference_user_email_key), Constants.EMPTY)
        set(value) {
            val editor = prefs.edit()
            if (value != null) editor.putString(res.getString(R.string.preference_user_email_key), value)
            else editor.remove(res.getString(R.string.preference_user_email_key))
            editor.apply()
        }

    var userPhone: String?
        get() = prefs.getString(res.getString(R.string.preference_user_phone_key), Constants.EMPTY)
        set(value) {
            val editor = prefs.edit()
            if (value != null) editor.putString(res.getString(R.string.preference_user_phone_key), value)
            else editor.remove(res.getString(R.string.preference_user_phone_key))
            editor.apply()
        }

    var userConsentDate: Long?
        get() = prefs.getLong(res.getString(R.string.preference_user_consent_date_key), Constants.INVALID.toLong())
        set(value) {
            val editor = prefs.edit()
            if (value != null) editor.putLong(res.getString(R.string.preference_user_consent_date_key), value)
            else editor.remove(res.getString(R.string.preference_user_consent_date_key))
            editor.apply()
        }

    var userConsentName: String?
        get() = prefs.getString(res.getString(R.string.preference_user_consent_name_key), Constants.EMPTY)
        set(value) {
            val editor = prefs.edit()
            if (value != null) editor.putString(res.getString(R.string.preference_user_consent_name_key), value)
            else editor.remove(res.getString(R.string.preference_user_consent_name_key))
            editor.apply()
        }

    var consentFormText: String
        get() = prefs.getString(res.getString(R.string.preference_user_consent_form_text_key), Constants.EMPTY) ?: Constants.EMPTY
        set(value) {
            val editor = prefs.edit()
            editor.putString(res.getString(R.string.preference_user_consent_form_text_key), value)
            editor.apply()
        }

    var consentVersion: String
        get() = prefs.getString(res.getString(R.string.preference_user_consent_form_version_key), Constants.EMPTY) ?: Constants.EMPTY
        set(value) {
            val editor = prefs.edit()
            editor.putString(res.getString(R.string.preference_user_consent_form_version_key), value)
            editor.apply()
        }

    fun userFullName(): String {
        return "$userFirstName $userLastName"
    }


    override fun logout() {
        idUser = null
        userNhsNumber = null
        userSex = null
        userFirstName = null
        userLastName = null
        userDateOfBirth = null
        userPostcode = null
        userConsentDate = null
        userConsentName = null

    }
}