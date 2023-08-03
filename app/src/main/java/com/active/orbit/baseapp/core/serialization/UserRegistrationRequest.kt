package com.active.orbit.baseapp.core.serialization

import android.text.TextUtils
import com.active.orbit.baseapp.core.generics.BaseModel
import com.active.orbit.baseapp.core.utils.Constants
import com.google.gson.annotations.SerializedName
import java.math.BigInteger

class UserRegistrationRequest : BaseModel {

    @SerializedName("phoneModel")
    var phoneModel: String? = null

    @SerializedName("appVersion")
    var appVersion: String? = null

    @SerializedName("androidVersion")
    var androidVersion: String? = null

    @SerializedName("userNHSNumber")
    var userNhsNumber: BigInteger? = null

    @SerializedName("userFirstName")
    var userFirstName: String? = null

    @SerializedName("userLastName")
    var userLastName: String? = null

    @SerializedName("userDateOfBirth")
    var userDob: Long? = null

    @SerializedName("userPostcode")
    var userPostcode: String? = null

    @SerializedName("userSex")
    var userSex: String? = null

    @SerializedName("timeInMsecs")
    var registrationTimestamp: Long? = null

    @SerializedName("userIPAddress")
    var userIPAddress: String? = null

    @SerializedName("userConsentDate")
    var userConsentDate: Long? = null

    @SerializedName("userConsentName")
    var userConsentName: String? = null

    @SerializedName("userEmail")
    var userEmail: String? = null

    @SerializedName("userPhoneNumber")
    var userPhoneNumber: String? = null


    override fun identifier(): String {
        return Constants.EMPTY
    }

    override fun isValid(): Boolean {
        return !TextUtils.isEmpty(phoneModel) &&
                !TextUtils.isEmpty(appVersion) &&
                !TextUtils.isEmpty(androidVersion) &&
                userNhsNumber != null &&
                !TextUtils.isEmpty(userSex) &&
                !TextUtils.isEmpty(userFirstName) &&
                !TextUtils.isEmpty(userLastName) &&
                !TextUtils.isEmpty(userConsentName) &&
                !TextUtils.isEmpty(userIPAddress) &&
                !TextUtils.isEmpty(userPostcode) &&
                (!TextUtils.isEmpty(userEmail) ||
                !TextUtils.isEmpty(userPhoneNumber)) &&
                userDob != null &&
                userConsentDate != null &&
                registrationTimestamp != null
    }
}