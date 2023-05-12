package com.active.orbit.baseapp.design.recyclers.adapters.holders

import android.view.View
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.database.models.DBProgram
import com.active.orbit.baseapp.design.dialogs.listeners.SelectProgrammeDialogListener
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerCell
import com.active.orbit.baseapp.design.widgets.BaseTextView

class ProgrammeDialogViewHolder(itemView: View, var listener: SelectProgrammeDialogListener? = null) : BaseRecyclerCell<DBProgram>(itemView) {

    private var programme: BaseTextView

    init {
        programme = itemView.findViewById(R.id.text)
    }

    override fun bind(model: DBProgram) {
        programme.text = model.name
        itemView.setOnClickListener {
            listener?.onProgrammeSelect(model)
        }
    }
}