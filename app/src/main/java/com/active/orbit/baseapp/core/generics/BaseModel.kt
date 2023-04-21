package com.active.orbit.baseapp.core.generics

import android.text.TextUtils
import com.active.orbit.baseapp.core.database.models.DBProgram
import com.active.orbit.baseapp.core.database.models.DBSeverity
import com.active.orbit.baseapp.core.database.models.DBSymptom
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.design.recyclers.models.TripModel

/**
 * Base protocol that should be implemented by all the models
 *
 * @author omar.brugna
 */
interface BaseModel : Comparable<BaseModel?> {

    fun isValid(): Boolean

    fun identifier(): String {
        Logger.e("Called base identifier method, this should never happen")
        return Constants.EMPTY
    }

    fun sameOf(other: BaseModel?): Boolean {
        if (!TextUtils.isEmpty(identifier()) && !TextUtils.isEmpty(other?.identifier()))
            return identifier() == other?.identifier()
        return false
    }

    fun sameContentOf(other: BaseModel?): Boolean {
        // override to customize
        return false
    }

    override fun compareTo(other: BaseModel?): Int {
        when {
            this is DBProgram && other is DBProgram -> {
                val value = position ?: 0
                val otherValue = other.position ?: 0
                return value.compareTo(otherValue)
            }
            this is DBSymptom && other is DBSymptom -> {
                val value = position ?: 0
                val otherValue = other.position ?: 0
                return value.compareTo(otherValue)
            }
            this is DBSeverity && other is DBSeverity -> {
                val value = position ?: 0
                val otherValue = other.position ?: 0
                return value.compareTo(otherValue)
            }
            this is TripModel && other is TripModel -> {
                val value = startTime
                val otherValue = other.startTime
                return value.compareTo(otherValue)
            }
        }
        return Constants.PRIORITY_ZERO
    }
}