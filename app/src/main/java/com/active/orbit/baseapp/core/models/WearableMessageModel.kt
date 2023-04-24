package com.active.orbit.baseapp.core.models

import android.text.TextUtils
import com.active.orbit.baseapp.core.generics.BaseModel
import com.active.orbit.baseapp.core.utils.Constants
import com.google.gson.annotations.SerializedName

class WearableMessageModel : BaseModel {

    @SerializedName("type")
    var type = Constants.EMPTY

    @SerializedName("id_user")
    var idUser = Constants.EMPTY

    @SerializedName("id_patient")
    var idPatient = Constants.EMPTY

    @SerializedName("data_uploaded")
    var dataUploaded = false

    @SerializedName("remaining_data_upload")
    var remainingDataUpload = Constants.EMPTY

    override fun identifier(): String {
        return idUser
    }

    override fun isValid(): Boolean {
        return !TextUtils.isEmpty(type)
    }
}