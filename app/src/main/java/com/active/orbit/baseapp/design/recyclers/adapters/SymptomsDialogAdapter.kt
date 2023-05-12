package com.active.orbit.baseapp.design.recyclers.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.database.models.DBSymptom
import com.active.orbit.baseapp.core.database.tables.TableSymptoms
import com.active.orbit.baseapp.core.preferences.engine.Preferences
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.dialogs.listeners.SelectSeverityDialogListener
import com.active.orbit.baseapp.design.dialogs.listeners.SelectSymptomDialogListener
import com.active.orbit.baseapp.design.recyclers.adapters.holders.SeverityDialogViewHolder
import com.active.orbit.baseapp.design.recyclers.adapters.holders.SymptomDialogViewHolder
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerAdapter
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerCell

class SymptomsDialogAdapter(private var context: Context) : RecyclerView.Adapter<SymptomDialogViewHolder>() {

    var symptoms = ArrayList<String>()
    var listener: SelectSymptomDialogListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SymptomDialogViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item_selection_dialog, parent, false)
        return SymptomDialogViewHolder(context, inflatedView)
    }

    override fun onBindViewHolder(holder: SymptomDialogViewHolder, position: Int) {
        holder.listener = listener
        holder.bind(symptoms[position])
    }

    override fun getItemCount(): Int {
        return symptoms.size
    }
}
