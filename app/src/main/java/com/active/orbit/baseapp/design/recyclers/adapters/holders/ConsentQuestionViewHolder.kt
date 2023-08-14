package com.active.orbit.baseapp.design.recyclers.adapters.holders

import android.view.View
import android.view.ViewGroup
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.database.models.DBConsentQuestion
import com.active.orbit.baseapp.design.listeners.ItemListener
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerCell
import com.active.orbit.baseapp.design.recyclers.listeners.ConsentQuestionListener
import com.active.orbit.baseapp.design.widgets.BaseCheckBox

class ConsentQuestionViewHolder(itemView: View, var listener: ConsentQuestionListener? = null, var itemListener: ItemListener? = null, var allAccepted: Boolean = false) : BaseRecyclerCell<DBConsentQuestion>(itemView) {

    private var consentRoot: ViewGroup
    private var question: BaseCheckBox

    init {
        consentRoot = itemView.findViewById(R.id.consentRoot)
        question = itemView.findViewById(R.id.question)
    }

    override fun bind(model: DBConsentQuestion) {

        question.text = model.questionText

        if (allAccepted) {
            question.isChecked = true
            question.setOnClickListener {
                question.isChecked = true
            }
        } else {
            question.setOnClickListener {
                listener?.isAccepted(question.isChecked)
            }
        }

        consentRoot.post {
            itemListener?.onItemHeight(consentRoot.height)
        }
    }
}