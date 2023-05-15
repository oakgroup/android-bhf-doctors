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
    var symptomId: Long = 0L
    var symptomName: String? = null
    var symptomSeverity: String? = null
    var symptomDateTime = 0L
    var symptomTimestamp= 0L
    var symptomDetails: String? = null


    override fun identifier(): String {
        return symptomId.toString()
    }

    fun description(): String {
        return "[$symptomId - $symptomName - $symptomSeverity -$symptomTimestamp]"
    }

    override fun isValid(): Boolean {
        return !TextUtils.isEmpty(symptomName)
                && !TextUtils.isEmpty(symptomSeverity)
                && symptomDateTime > 0L
                && symptomTimestamp > 0L
    }

    fun severitySelected(): Boolean {
        return symptomSeverity != null
    }

}
