package com.active.orbit.baseapp.core.database.models

import android.text.TextUtils
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.active.orbit.baseapp.core.generics.BaseModel


@Entity(tableName = "health")
class DBHealth : BaseModel {

    @PrimaryKey
    var healthID: Long = 0L
    var healthMobility: String? = null
    var healthSelfCare: String? = null
    var healthActivities: String? = null
    var healthAnxiety: String? = null
    var healthPain: String? = null
    var healthScore: Int? = null


    override fun identifier(): String {
        return healthID.toString()
    }

    fun description(): String {
        return "[$healthID - $healthMobility - $healthSelfCare -$healthActivities -$healthAnxiety -$healthPain -$healthScore]"
    }

    override fun isValid(): Boolean {
        return !TextUtils.isEmpty(healthMobility)
                && !TextUtils.isEmpty(healthSelfCare)
                && !TextUtils.isEmpty(healthActivities)
                && !TextUtils.isEmpty(healthAnxiety)
                && !TextUtils.isEmpty(healthPain)
                && healthScore != null

    }

}
