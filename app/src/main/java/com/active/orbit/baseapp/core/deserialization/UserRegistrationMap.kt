package com.active.orbit.baseapp.core.deserialization

import android.text.TextUtils
import com.active.orbit.baseapp.core.generics.BaseModel
import com.active.orbit.baseapp.core.serialization.UploadHealthRequest
import com.active.orbit.baseapp.core.utils.Constants
import com.google.gson.annotations.SerializedName

class UserRegistrationMap : BaseModel {


    @SerializedName("Item")
    var dataItem = DataItem()

    class DataItem() : BaseModel {

        @SerializedName("userId")
        var userId = UserId()

        @SerializedName("participantIdCounter")
        var participantIdCounter = ParticipantIdCounter()

        class UserId() : BaseModel {

            @SerializedName("S")
            var id: String = Constants.EMPTY
            override fun isValid(): Boolean {
                return  !TextUtils.isEmpty(id)
            }

        }

        class ParticipantIdCounter() : BaseModel {

            @SerializedName("N")
            var counter: Int = Constants.INVALID
            override fun isValid(): Boolean {
                return  counter != Constants.INVALID
            }

        }

        override fun isValid(): Boolean {
            return userId.isValid() && participantIdCounter.isValid()
        }

    }


    override fun identifier(): String {
        return Constants.EMPTY
    }


    override fun isValid(): Boolean {
        return dataItem.isValid()
    }
}