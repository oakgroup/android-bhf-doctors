package com.active.orbit.baseapp.design.recyclers.models

import com.active.orbit.baseapp.core.generics.BaseModel
import com.active.orbit.baseapp.core.utils.Constants

class SexModel(var sex: String) : BaseModel {

    override fun identifier(): String {
        return sex
    }

    override fun isValid(): Boolean {
        return sex != Constants.EMPTY
    }
}