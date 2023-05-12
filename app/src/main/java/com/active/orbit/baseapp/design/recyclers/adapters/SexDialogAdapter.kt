package com.active.orbit.baseapp.design.recyclers.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.dialogs.listeners.SelectSexDialogListener
import com.active.orbit.baseapp.design.recyclers.adapters.holders.SexDialogViewHolder
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerAdapter
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerCell
import com.active.orbit.baseapp.design.recyclers.models.SexModel

class SexDialogAdapter(private var activity: BaseActivity, var listener: SelectSexDialogListener? = null) : BaseRecyclerAdapter<SexModel>() {

    override fun dataSource(context: Context): List<SexModel> {
        val male = SexModel("Male")
        val female = SexModel("Female")

        return listOf(male, female)

    }

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerCell<SexModel> {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_selection_dialog, parent, false)
        return SexDialogViewHolder(view, listener)
    }
}
