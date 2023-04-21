package com.active.orbit.baseapp.core.database.models

import android.text.TextUtils
import androidx.room.Entity
import com.active.orbit.baseapp.core.generics.BaseModel
import com.active.orbit.baseapp.core.utils.Constants

@Entity(
    tableName = "programs",
    primaryKeys = ["idProgram"]
)
data class DBProgram(var idProgram: String = Constants.EMPTY) : BaseModel {

    var position: Int? = null
    var name: String? = null

    override fun identifier(): String {
        return idProgram
    }

    fun description(): String {
        return "[$idProgram - $position - $name]"
    }

    override fun isValid(): Boolean {
        return !TextUtils.isEmpty(idProgram) && position != null && !TextUtils.isEmpty(name)
    }
}