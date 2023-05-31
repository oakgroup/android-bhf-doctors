package com.active.orbit.baseapp.design.recyclers.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.database.models.DBProgram
import com.active.orbit.baseapp.core.database.tables.TablePrograms
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.dialogs.listeners.SelectProgrammeDialogListener
import com.active.orbit.baseapp.design.recyclers.adapters.holders.ProgrammeDialogViewHolder
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerAdapter
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerCell

class ProgrammesDialogAdapter(private var activity: BaseActivity, var listener: SelectProgrammeDialogListener? = null) : BaseRecyclerAdapter<DBProgram>() {

    override fun dataSource(context: Context): List<DBProgram> {
        return TablePrograms.getAll(activity).filter { it.name == "Newcastle" }
//TODO use this
//        return TablePrograms.getAll(activity).filter { it.name == "Moving Health" }
    }

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerCell<DBProgram> {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_selection_dialog, parent, false)
        return ProgrammeDialogViewHolder(view, listener)
    }
}
