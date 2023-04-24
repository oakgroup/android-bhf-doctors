package com.active.orbit.baseapp.design.recyclers.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.database.models.DBSymptom
import com.active.orbit.baseapp.core.database.tables.TableSymptoms
import com.active.orbit.baseapp.core.preferences.engine.Preferences
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.dialogs.listeners.SelectSymptomDialogListener
import com.active.orbit.baseapp.design.recyclers.adapters.holders.SymptomViewHolder
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerAdapter
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerCell

class SymptomsAdapter(private var activity: BaseActivity, var listener: SelectSymptomDialogListener? = null) : BaseRecyclerAdapter<DBSymptom>() {

    override fun dataSource(context: Context): List<DBSymptom> {
        val idProgram = Preferences.user(activity).idProgram ?: Constants.EMPTY
        return TableSymptoms.getAll(activity, idProgram)
    }

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerCell<DBSymptom> {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_selection_dialog, parent, false)
        return SymptomViewHolder(view, listener)
    }
}