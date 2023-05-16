package com.active.orbit.baseapp.design.recyclers.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.database.models.DBHealth
import com.active.orbit.baseapp.core.database.tables.TableHealth
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.recyclers.adapters.holders.HealthViewHolder
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerAdapter
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerCell

class HealthAdapter(private var activity: BaseActivity) : BaseRecyclerAdapter<DBHealth>() {

    override fun dataSource(context: Context): List<DBHealth> {
        return TableHealth.getAll(activity)
    }

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerCell<DBHealth> {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_health, parent, false)
        return HealthViewHolder(activity, view)
    }
}