package com.active.orbit.baseapp.design.recyclers.models

import com.active.orbit.baseapp.core.generics.BaseProtocol
import com.active.orbit.baseapp.core.utils.Constants

class FaqModel : BaseProtocol {

    var position = Constants.INVALID
    var category = Constants.EMPTY
    var subCategories = ArrayList<FaqModel>()
    var response: FaqModel? = null
    var question = Constants.EMPTY
    var answer = Constants.EMPTY

    constructor(question: String, answer: String) {
        this.question = question
        this.answer = answer
    }

    constructor(category: String) {
        this.category = category
    }

    override fun isValid(): Boolean {
        return position != Constants.INVALID && (hasSubCategories() || hasResponse())
    }

    override fun identifier(): String {
        return position.toString()
    }

    fun hasSubCategories(): Boolean {
        return subCategories.isNotEmpty()
    }

    fun hasResponse(): Boolean {
        return response != null
    }
}