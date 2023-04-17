package com.active.orbit.baseapp.demo.design.recyclers.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.demo.core.deserialization.DemoImageMap
import com.active.orbit.baseapp.demo.design.recyclers.cells.DemoImageCell
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerAdapter
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerCell

class DemoImageRecyclerAdapter(private val activity: BaseActivity) : BaseRecyclerAdapter<DemoImageMap.DemoImageMapItem>() {

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerCell<DemoImageMap.DemoImageMapItem> {
        val inflater = LayoutInflater.from(activity)
        val itemView = inflater.inflate(R.layout.cell_demo_image, parent, false)
        return DemoImageCell(activity, itemView)
    }

    override fun dataSource(context: Context): List<DemoImageMap.DemoImageMapItem> {
        return listOf()
    }
}