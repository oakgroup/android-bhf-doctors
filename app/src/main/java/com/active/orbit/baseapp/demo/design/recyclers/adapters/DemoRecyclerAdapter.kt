package com.active.orbit.baseapp.demo.design.recyclers.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.database.engine.Database
import com.active.orbit.baseapp.demo.core.database.models.DBDemo
import com.active.orbit.baseapp.demo.design.recyclers.cells.DemoCell
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerAdapter
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerCell

class DemoRecyclerAdapter(private val activity: BaseActivity) : BaseRecyclerAdapter<DBDemo>() {

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerCell<DBDemo> {
        val inflater = LayoutInflater.from(activity)
        val itemView = inflater.inflate(R.layout.cell_demo, parent, false)
        return DemoCell(activity, itemView)
    }

    override fun dataSource(context: Context): List<DBDemo> {
        return Database.getInstance(context).getDemo().getAllVoted()
    }
}