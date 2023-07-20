package com.active.orbit.baseapp.core.deserialization

import android.text.TextUtils
import com.active.orbit.baseapp.core.generics.BaseModel
import com.active.orbit.baseapp.core.utils.Constants
import com.google.gson.annotations.SerializedName

class UploadHealthMap : BaseModel {

    @SerializedName("result")
    var result: String? = null

    override fun identifier(): String {
        return Constants.EMPTY
    }

    override fun isValid(): Boolean {
        return !TextUtils.isEmpty(result)
    }
}