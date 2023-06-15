package com.active.orbit.baseapp.design.recyclers.models

import com.active.orbit.baseapp.core.generics.BaseModel
import com.active.orbit.baseapp.core.utils.Constants

//TODO do not use this after Prassana finishes the API, use the DBConsentQuestion
class ConsentQuestionModel(var id: Int, var question: String) : BaseModel {

    override fun identifier(): String {
        return question
    }

    override fun isValid(): Boolean {
        return question != Constants.EMPTY
    }
}