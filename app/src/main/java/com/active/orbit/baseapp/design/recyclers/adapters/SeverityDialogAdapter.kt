package com.active.orbit.baseapp.design.recyclers.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.design.dialogs.listeners.SelectSeverityDialogListener
import com.active.orbit.baseapp.design.recyclers.adapters.holders.SeverityDialogViewHolder

class SeverityDialogAdapter(private var context: Context) : RecyclerView.Adapter<SeverityDialogViewHolder>() {

    var severities = ArrayList<String>()
    var listener: SelectSeverityDialogListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeverityDialogViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item_selection_dialog, parent, false)
        return SeverityDialogViewHolder(context, inflatedView)
    }

    override fun onBindViewHolder(holder: SeverityDialogViewHolder, position: Int) {
        holder.listener = listener
        holder.bind(severities[position])
    }

    override fun getItemCount(): Int {
        return severities.size
    }
}
