package com.active.orbit.baseapp.design.recyclers.adapters.holders

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.database.models.DBSymptom
import com.active.orbit.baseapp.design.dialogs.listeners.SelectSeverityDialogListener
import com.active.orbit.baseapp.design.dialogs.listeners.SelectSymptomDialogListener
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerCell
import com.active.orbit.baseapp.design.widgets.BaseTextView

class SymptomDialogViewHolder(var context: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {

    var listener: SelectSymptomDialogListener? = null

    private var symptom: BaseTextView

    init {
        symptom = itemView.findViewById(R.id.text)
    }

    fun bind(symptomName: String) {
        symptom.text = symptomName
        itemView.setOnClickListener {
            listener?.onSymptomSelect(symptomName)
        }
    }
}