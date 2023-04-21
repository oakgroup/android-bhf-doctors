package com.active.orbit.baseapp.core.deserialization

import com.active.orbit.baseapp.core.generics.BaseModel
import com.active.orbit.baseapp.core.utils.Constants
import com.google.gson.annotations.SerializedName

class UploadSymptomsMap : BaseModel {

    @SerializedName("inserted")
    var inserted: Int? = null

    override fun identifier(): String {
        return Constants.EMPTY
    }

    override fun isValid(): Boolean {
        return inserted != null
    }
}