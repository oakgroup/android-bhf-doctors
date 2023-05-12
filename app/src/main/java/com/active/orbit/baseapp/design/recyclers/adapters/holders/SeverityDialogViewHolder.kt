package com.active.orbit.baseapp.design.recyclers.adapters.holders

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.database.models.DBSeverity
import com.active.orbit.baseapp.design.dialogs.listeners.SelectSeverityDialogListener
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerCell
import com.active.orbit.baseapp.design.widgets.BaseTextView

class SeverityDialogViewHolder(var context: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {

    var listener: SelectSeverityDialogListener? = null

    private var severity: BaseTextView

    init {
        severity = itemView.findViewById(R.id.text)
    }

    fun bind(severityName: String) {
        severity.text = severityName
        itemView.setOnClickListener {
            listener?.onSeveritySelected(severityName)
        }
    }
}