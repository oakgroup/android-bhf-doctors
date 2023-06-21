package com.active.orbit.baseapp.core.database.models

import android.text.TextUtils
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.active.orbit.baseapp.core.generics.BaseModel
import com.active.orbit.baseapp.core.utils.Constants


@Entity(tableName = "consent_questions")
class DBConsentQuestion : BaseModel {

    @PrimaryKey
    var questionID = Constants.EMPTY
    var questionText: String? = null


    override fun identifier(): String {
        return questionID
    }

    fun description(): String {
        return "[$questionID - $questionText]"
    }

    override fun isValid(): Boolean {
        return !TextUtils.isEmpty(questionID)
                && questionText != null
    }

}
