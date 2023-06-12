package com.active.orbit.baseapp.core.serialization

import android.text.TextUtils
import com.active.orbit.baseapp.core.generics.BaseModel
import com.active.orbit.baseapp.core.utils.Constants
import com.google.gson.annotations.SerializedName

class UploadHealthRequest : BaseModel {

    @SerializedName("userId")
    var userId: String? = null

    @SerializedName("healthResponse")
    var healthResponse = UploadHealthResponse()

    override fun identifier(): String {
        return Constants.EMPTY
    }

    override fun isValid(): Boolean {
        return !TextUtils.isEmpty(userId) && healthResponse.isValid()
    }

    class UploadHealthResponse : BaseModel {

        @SerializedName("timeInMsecs")
        var timestamp: Long = Constants.INVALID.toLong()

        @SerializedName("id")
        var healthID: Long = Constants.INVALID.toLong()

        @SerializedName("mobility")
        var mobility: Int? = null

        @SerializedName("selfCare")
        var selfCare: Int? = null

        @SerializedName("usualActivities")
        var usualActivities: Int? = null

        @SerializedName("pain")
        var pain: Int? = null

        @SerializedName("anxiety")
        var anxiety: Int? = null

        @SerializedName("score")
        var score: Int? = null


        override fun identifier(): String {
            return Constants.EMPTY
        }

        override fun isValid(): Boolean {
            return timestamp != Constants.INVALID.toLong() &&
                    mobility != null &&
                    selfCare != null &&
                    usualActivities != null &&
                    pain != null &&
                    anxiety != null &&
                    score != null
        }
    }
}