package com.active.orbit.baseapp.core.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.active.orbit.baseapp.core.generics.BaseModel


@Entity(tableName = "health")
class DBHealth : BaseModel {

    @PrimaryKey
    var healthID: Long = 0L
    var healthMobility: Int? = null
    var healthSelfCare: Int? = null
    var healthActivities: Int? = null
    var healthAnxiety: Int? = null
    var healthPain: Int? = null
    var healthScore: Int? = null
    var healthTimestamp: Long = 0L

    var uploaded = false


    override fun identifier(): String {
        return healthID.toString()
    }

    fun description(): String {
        return "[$healthID - $healthMobility - $healthSelfCare -$healthActivities -$healthAnxiety -$healthPain -$healthScore -$healthTimestamp]"
    }

    override fun isValid(): Boolean {
        return healthMobility != null
                && healthSelfCare != null
                && healthActivities != null
                && healthAnxiety != null
                && healthPain != null
                && healthScore != null
                && healthTimestamp > 0L
    }

}
