package com.active.orbit.baseapp.core.deserialization

import android.text.TextUtils
import com.active.orbit.baseapp.core.database.models.DBConsentQuestion
import com.active.orbit.baseapp.core.generics.BaseModel
import com.active.orbit.baseapp.core.utils.Constants
import com.google.gson.annotations.SerializedName

class RetrieveConsentFormMap : BaseModel {

    @SerializedName("consentText")
    var consentText: String? = null

    @SerializedName("version")
    var version: String? = null

    @SerializedName("questions")
    var questions = ArrayList<QuestionsDataItem>()

    override fun identifier(): String {
        return Constants.EMPTY
    }

    override fun isValid(): Boolean {
        return !TextUtils.isEmpty(consentText) &&
                !TextUtils.isEmpty(version) &&
                questions.isNotEmpty()
    }

    class QuestionsDataItem() : BaseModel {

        @SerializedName("id")
        var id: Int = Constants.INVALID

        @SerializedName("question")
        var question: String? = null


        override fun isValid(): Boolean {
            return id != Constants.INVALID &&
                    !TextUtils.isEmpty(question)
        }


    }

    fun dbQuestions(): ArrayList<DBConsentQuestion> {

        val questionsList = ArrayList<DBConsentQuestion>()
        if (questions.isNotEmpty()) {
            for (question in questions) {
                if (question.isValid()) {
                    val model = DBConsentQuestion()
                    model.questionID = question.id.toString()
                    model.questionText = question.question
                    questionsList.add(model)
                }
            }
        }

        return questionsList
    }


}

