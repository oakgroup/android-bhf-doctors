package com.active.orbit.baseapp.design.recyclers.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.recyclers.adapters.holders.ActivityViewHolder
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerAdapter
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerCell
import com.active.orbit.baseapp.design.recyclers.models.TripModel

class ActivitiesAdapter(private var activity: BaseActivity) : BaseRecyclerAdapter<TripModel>() {

    var tripModels = ArrayList<TripModel>()

    override fun dataSource(context: Context): List<TripModel> {
        return listOf()
    }

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerCell<TripModel> {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_activity, parent, false)
        return ActivityViewHolder(activity, view, tripModels)
    }

    override fun onBindViewHolder(holder: BaseRecyclerCell<TripModel>, position: Int) {
        tripModels[position].position = position
        super.onBindViewHolder(holder, position)
    }
}