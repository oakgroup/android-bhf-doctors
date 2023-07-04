package com.active.orbit.baseapp.design.recyclers.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.database.models.DBConsentQuestion
import com.active.orbit.baseapp.core.database.tables.TableConsentQuestions
import com.active.orbit.baseapp.core.database.tables.TableReportedSymptoms
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.recyclers.adapters.holders.ConsentQuestionViewHolder
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerAdapter
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerCell
import com.active.orbit.baseapp.design.recyclers.listeners.ConsentQuestionListener

class ConsentQuestionsAdapter(private var activity: BaseActivity, var listener: ConsentQuestionListener? = null) : BaseRecyclerAdapter<DBConsentQuestion>() {

    var numberOfQuestions = 0

    override fun dataSource(context: Context): List<DBConsentQuestion> {

        val questions = TableConsentQuestions.getAll(activity)
        numberOfQuestions = questions.size
        return questions

    }

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerCell<DBConsentQuestion> {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_consent_question, parent, false)
        return ConsentQuestionViewHolder(view, listener)
    }
}