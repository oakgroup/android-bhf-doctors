package com.active.orbit.baseapp.core.database.models

import android.text.TextUtils
import androidx.room.Entity
import com.active.orbit.baseapp.core.generics.BaseModel
import com.active.orbit.baseapp.core.utils.Constants

@Entity(
    tableName = "severities",
    primaryKeys = ["idSymptom", "idSeverity"]
)
data class DBSeverity(var idSymptom: String = Constants.EMPTY, var idSeverity: String = Constants.EMPTY) : BaseModel {

    var position: Int? = null
    var symptomResponse: String? = null
    var symptomValue: String? = null

    override fun identifier(): String {
        return idSeverity
    }

    fun description(): String {
        return "[$idSeverity - $position - $symptomResponse - $symptomValue]"
    }

    override fun isValid(): Boolean {
        return !TextUtils.isEmpty(idSeverity) && position != null && !TextUtils.isEmpty(symptomResponse) && !TextUtils.isEmpty(symptomValue)
    }
}