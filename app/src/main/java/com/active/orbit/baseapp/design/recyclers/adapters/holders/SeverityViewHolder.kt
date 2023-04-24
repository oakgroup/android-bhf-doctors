package com.active.orbit.baseapp.design.recyclers.adapters.holders

import android.view.View
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.database.models.DBSeverity
import com.active.orbit.baseapp.design.dialogs.listeners.SelectSeverityDialogListener
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerCell
import com.active.orbit.baseapp.design.widgets.BaseTextView

class SeverityViewHolder(itemView: View, var listener: SelectSeverityDialogListener? = null) : BaseRecyclerCell<DBSeverity>(itemView) {

    private var severity: BaseTextView

    init {
        severity = itemView.findViewById(R.id.text)
    }

    override fun bind(model: DBSeverity) {
        severity.text = model.symptomResponse
        itemView.setOnClickListener {
            listener?.onSeveritySelected(model)
        }
    }
}