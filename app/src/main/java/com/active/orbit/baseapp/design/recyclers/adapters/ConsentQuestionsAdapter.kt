package com.active.orbit.baseapp.design.recyclers.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.recyclers.adapters.holders.ConsentQuestionViewHolder
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerAdapter
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerCell
import com.active.orbit.baseapp.design.recyclers.listeners.ConsentQuestionListener
import com.active.orbit.baseapp.design.recyclers.models.ConsentQuestionModel

class ConsentQuestionsAdapter(private var activity: BaseActivity, var listener: ConsentQuestionListener? = null) : BaseRecyclerAdapter<ConsentQuestionModel>() {

    var numberOfQuestions = 0

    override fun dataSource(context: Context): List<ConsentQuestionModel> {
        //TODO remove and get the questions from server
        val questionOne = ConsentQuestionModel(1,"I am over 18 years old")
        val questionTwo = ConsentQuestionModel(2,"I live in England, Wales, or Scotland")
        val questionThree = ConsentQuestionModel(3,"I understand that I can withdraw at any time, but you may still use data obtained until the date of my withdrawal")
        val questionFour = ConsentQuestionModel(4,"I give my consent to take part in this study")
        val questionFive = ConsentQuestionModel(5, "I give consent to obtain GPS data as well as activity and questionnaire data")

        val questions = listOf(questionOne, questionTwo, questionThree, questionFour, questionFive)
        numberOfQuestions = questions.size

        return questions

    }

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerCell<ConsentQuestionModel> {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_consent_question, parent, false)
        return ConsentQuestionViewHolder(view, listener)
    }
}