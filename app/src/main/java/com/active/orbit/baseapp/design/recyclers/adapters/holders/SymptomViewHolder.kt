package com.active.orbit.baseapp.design.recyclers.adapters.holders

import android.view.View
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.database.models.DBSymptom
import com.active.orbit.baseapp.design.dialogs.listeners.SelectSymptomDialogListener
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerCell
import com.active.orbit.baseapp.design.widgets.BaseTextView

class SymptomViewHolder(itemView: View, var listener: SelectSymptomDialogListener? = null) : BaseRecyclerCell<DBSymptom>(itemView) {

    private var symptom: BaseTextView

    init {
        symptom = itemView.findViewById(R.id.text)
    }

    override fun bind(model: DBSymptom) {
        symptom.text = model.question
        itemView.setOnClickListener {
            listener?.onSymptomSelect(model)
        }
    }
}