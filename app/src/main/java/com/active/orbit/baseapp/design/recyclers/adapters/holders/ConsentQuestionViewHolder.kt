package com.active.orbit.baseapp.design.recyclers.adapters.holders

import android.view.View
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.database.models.DBConsentQuestion
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerCell
import com.active.orbit.baseapp.design.recyclers.listeners.ConsentQuestionListener
import com.active.orbit.baseapp.design.widgets.BaseCheckBox

class ConsentQuestionViewHolder(itemView: View, var listener: ConsentQuestionListener? = null) : BaseRecyclerCell<DBConsentQuestion>(itemView) {

    private var question: BaseCheckBox

    init {
        question = itemView.findViewById(R.id.question)
    }

    override fun bind(model: DBConsentQuestion) {

        question.text = model.questionText

        question.setOnClickListener {
            listener?.isAccepted(question.isChecked)

        }


    }
}