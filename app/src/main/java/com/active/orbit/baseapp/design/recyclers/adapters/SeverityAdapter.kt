package com.active.orbit.baseapp.design.recyclers.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.database.models.DBSeverity
import com.active.orbit.baseapp.core.database.tables.TableSeverities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.dialogs.listeners.SelectSeverityDialogListener
import com.active.orbit.baseapp.design.recyclers.adapters.holders.SeverityViewHolder
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerAdapter
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerCell

class SeverityAdapter(private var activity: BaseActivity, var idSymptom: String, var listener: SelectSeverityDialogListener? = null) : BaseRecyclerAdapter<DBSeverity>() {

    override fun dataSource(context: Context): List<DBSeverity> {
        return TableSeverities.getAll(activity, idSymptom)
    }

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerCell<DBSeverity> {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_selection_dialog, parent, false)
        return SeverityViewHolder(view, listener)
    }
}
