package com.active.orbit.baseapp.design.recyclers.adapters.holders

import android.view.View
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.design.dialogs.listeners.SelectSexDialogListener
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerCell
import com.active.orbit.baseapp.design.recyclers.models.SexModel
import com.active.orbit.baseapp.design.widgets.BaseTextView

class SexDialogViewHolder(itemView: View, var listener: SelectSexDialogListener? = null) : BaseRecyclerCell<SexModel>(itemView) {

    private var sex: BaseTextView

    init {
        sex = itemView.findViewById(R.id.text)
    }

    override fun bind(model: SexModel) {

        sex.text = model.sex
        itemView.setOnClickListener {
            listener?.onSexSelected(model)
        }


    }
}