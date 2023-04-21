package com.active.orbit.baseapp.core.serialization

import android.text.TextUtils
import com.active.orbit.baseapp.core.generics.BaseModel
import com.active.orbit.baseapp.core.utils.Constants
import com.google.gson.annotations.SerializedName

class UploadSymptomsRequest : BaseModel {

    @SerializedName("user_id")
    var userId: String? = null

    @SerializedName("symptoms")
    var symptoms = ArrayList<UploadSymptomRequest>()

    override fun identifier(): String {
        return Constants.EMPTY
    }

    override fun isValid(): Boolean {
        return !TextUtils.isEmpty(userId) && symptoms.isNotEmpty()
    }

    class UploadSymptomRequest : BaseModel {

        @SerializedName("timeInMsecs")
        var timeInMsecs: Long = Constants.INVALID.toLong()

        @SerializedName("symptomId")
        var symptomId: String? = null

        @SerializedName("symptomValue")
        var symptomValue: Int = Constants.INVALID

        @SerializedName("symptomResponse")
        var symptomResponse: String? = null

        @SerializedName("description")
        var description: String? = null

        override fun identifier(): String {
            return Constants.EMPTY
        }

        override fun isValid(): Boolean {
            return timeInMsecs != Constants.INVALID.toLong() &&
                    !TextUtils.isEmpty(symptomId) &&
                    symptomValue != Constants.INVALID &&
                    !TextUtils.isEmpty(symptomResponse)
        }
    }
}