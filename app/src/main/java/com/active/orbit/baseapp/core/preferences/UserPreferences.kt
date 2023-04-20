package com.active.orbit.baseapp.core.preferences

import android.text.TextUtils
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.preferences.engine.BasePreferences
import com.active.orbit.baseapp.core.preferences.engine.PreferencesKeys
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

    var idPatient: String?
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
        get() = prefs.getLong(res.getString(R.string.preference_user_date_program_started), Constants.INVALID.toLong())
        set(value) {
            val editor = prefs.edit()
            if (value != null) editor.putLong(res.getString(R.string.preference_user_date_program_started), value)
            else editor.remove(res.getString(R.string.preference_user_date_program_started))
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
        get() = prefs.getString(res.getString(R.string.preference_user_sex), Constants.EMPTY)
        set(value) {
            val editor = prefs.edit()
            editor.putString(res.getString(R.string.preference_user_sex), value)
            editor.apply()
        }

    var userAge: String?
        get() = prefs.getString(res.getString(R.string.preference_user_age), Constants.EMPTY)
        set(value) {
            val editor = prefs.edit()
            if (value != null) editor.putString(res.getString(R.string.preference_user_age), value)
            else editor.remove(res.getString(R.string.preference_user_age))
            editor.apply()
        }

    var userWeight: String?
        get() = prefs.getString(res.getString(R.string.preference_user_weight), Constants.EMPTY)
        set(value) {
            val editor = prefs.edit()
            if (value != null) editor.putString(res.getString(R.string.preference_user_weight), value)
            else editor.remove(res.getString(R.string.preference_user_weight))
            editor.apply()
        }

    var userHeight: String?
        get() = prefs.getString(res.getString(R.string.preference_user_height), Constants.EMPTY)
        set(value) {
            val editor = prefs.edit()
            if (value != null) editor.putString(res.getString(R.string.preference_user_height), value)
            else editor.remove(res.getString(R.string.preference_user_height))
            editor.apply()
        }


    override fun logout() {
        idUser = null
        idPatient = null
        idProgram = null
        programStarted = false
        dateProgramStarted = null
        watchSynchronized = false
        userSex = null
        userHeight = null
        userWeight = null
    }
}