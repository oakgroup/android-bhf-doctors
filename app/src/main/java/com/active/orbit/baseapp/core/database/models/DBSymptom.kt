package com.active.orbit.baseapp.core.database.models

import android.text.TextUtils
import androidx.room.Entity
import androidx.room.Ignore
import com.active.orbit.baseapp.core.generics.BaseModel
import com.active.orbit.baseapp.core.utils.Constants

@Entity(
    tableName = "symptoms",
    primaryKeys = ["idProgram", "idSymptom"]
)
data class DBSymptom(var idProgram: String = Constants.EMPTY, var idSymptom: String = Constants.EMPTY) : BaseModel {

    var position: Int? = null
    var question: String? = null

    @Ignore
    var severity: DBSeverity? = null

    @Ignore
    var symptomDetails: String? = null

    @Ignore
    var symptomDateTime = 0L

    override fun identifier(): String {
        return idSymptom
    }

    fun description(): String {
        return "[$idSymptom - $position - $question]"
    }

    override fun isValid(): Boolean {
        return !TextUtils.isEmpty(idSymptom) && position != null && !TextUtils.isEmpty(question)
    }

    fun severitySelected(): Boolean {
        return severity != null
    }
}