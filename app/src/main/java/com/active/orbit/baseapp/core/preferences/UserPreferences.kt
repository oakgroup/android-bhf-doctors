package com.active.orbit.baseapp.core.preferences

import android.text.TextUtils
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.preferences.engine.BasePreferences
import com.active.orbit.baseapp.core.utils.Constants
import com.google.firebase.crashlytics.FirebaseCrashlytics

class UserPreferences : BasePreferences() {

    internal fun register(idUser: String, idProgram: String) {
        this.idUser = idUser
        this.idProgram = idProgram
    }

    internal fun isUserRegistered(): Boolean {
        return !TextUtils.isEmpty(idUser) && !TextUtils.isEmpty(idProgram)
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

    var idProgram: String?
        get() = prefs.getString(res.getString(R.string.preference_user_id_program_key), Constants.EMPTY)
        set(value) {
            val editor = prefs.edit()
            editor.putString(res.getString(R.string.preference_user_id_program_key), value)
            editor.apply()

            if (!TextUtils.isEmpty(value)) FirebaseCrashlytics.getInstance().setCustomKey("id_user", value!!)
        }

    var programStarted: Boolean
        get() = prefs.getBoolean(res.getString(R.string.preference_user_program_started_key), false)
        set(value) {
            val editor = prefs.edit()
            editor.putBoolean(res.getString(R.string.preference_user_program_started_key), value)
            editor.apply()
        }

    var dateProgramStarted: Long?
        get() = prefs.getLong(res.getString(R.string.preference_user_date_program_started_key), Constants.INVALID.toLong())
        set(value) {
            val editor = prefs.edit()
            if (value != null) editor.putLong(res.getString(R.string.preference_user_date_program_started_key), value)
            else editor.remove(res.getString(R.string.preference_user_date_program_started_key))
            editor.apply()
        }

    var watchSynchronized: Boolean
        get() = prefs.getBoolean(res.getString(R.string.preference_user_watch_synchronized_key), false)
        set(value) {
            val editor = prefs.edit()
            editor.putBoolean(res.getString(R.string.preference_user_watch_synchronized_key), value)
            editor.apply()
        }

    var userSex: String?
        get() = prefs.getString(res.getString(R.string.preference_user_sex_key), Constants.EMPTY)
        set(value) {
            val editor = prefs.edit()
            editor.putString(res.getString(R.string.preference_user_sex_key), value)
            editor.apply()
        }

    var userAge: String?
        get() = prefs.getString(res.getString(R.string.preference_user_age_key), Constants.EMPTY)
        set(value) {
            val editor = prefs.edit()
            if (value != null) editor.putString(res.getString(R.string.preference_user_age_key), value)
            else editor.remove(res.getString(R.string.preference_user_age_key))
            editor.apply()
        }

    var userWeight: String?
        get() = prefs.getString(res.getString(R.string.preference_user_weight_key), Constants.EMPTY)
        set(value) {
            val editor = prefs.edit()
            if (value != null) editor.putString(res.getString(R.string.preference_user_weight_key), value)
            else editor.remove(res.getString(R.string.preference_user_weight_key))
            editor.apply()
        }

    var userHeight: String?
        get() = prefs.getString(res.getString(R.string.preference_user_height_key), Constants.EMPTY)
        set(value) {
            val editor = prefs.edit()
            if (value != null) editor.putString(res.getString(R.string.preference_user_height_key), value)
            else editor.remove(res.getString(R.string.preference_user_height_key))
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

    var userDateOfConsent: Long?
        get() = prefs.getLong(res.getString(R.string.preference_user_date_of_birth_key), Constants.INVALID.toLong())
        set(value) {
            val editor = prefs.edit()
            if (value != null) editor.putLong(res.getString(R.string.preference_user_date_of_birth_key), value)
            else editor.remove(res.getString(R.string.preference_user_date_of_birth_key))
            editor.apply()
        }

    fun userFullName(): String {
        return "$userFirstName $userLastName"
    }



    override fun logout() {
        idUser = null
        userNhsNumber = null
        idProgram = null
        programStarted = false
        dateProgramStarted = null
        watchSynchronized = false
        userSex = null
        userHeight = null
        userWeight = null
        userFirstName = null
        userLastName = null
        userDateOfBirth = null

    }
}