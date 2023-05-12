package com.active.orbit.baseapp.core.database.models

import android.text.TextUtils
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.active.orbit.baseapp.core.generics.BaseModel
import com.active.orbit.baseapp.core.utils.Constants



@Entity(tableName = "reported_symptoms")
class DBReportSymptom : BaseModel {

    @PrimaryKey
    var symptomId: Long = 0
    var symptomName: String? = null
    var symptomSeverity: String? = null
    var symptomDateTime = 0L

    @Ignore
    var symptomDetails: String = Constants.EMPTY


    override fun identifier(): String {
        return symptomId.toString()
    }

    fun description(): String {
        return "[$symptomId - $symptomName - $symptomSeverity]"
    }

    override fun isValid(): Boolean {
        return !TextUtils.isEmpty(symptomName)
                && !TextUtils.isEmpty(symptomSeverity)
                && symptomDateTime > 0L
    }

    fun severitySelected(): Boolean {
        return symptomSeverity != null
    }

}
