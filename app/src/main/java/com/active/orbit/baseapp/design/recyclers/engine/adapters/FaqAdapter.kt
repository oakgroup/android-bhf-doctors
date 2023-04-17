package com.active.orbit.baseapp.design.recyclers.engine.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerAdapter
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerCell
import com.active.orbit.baseapp.design.recyclers.engine.adapters.holders.FaqViewHolder
import com.active.orbit.baseapp.design.recyclers.engine.models.FaqModel


class FaqAdapter(private var context: Context, private val categories: Array<FaqModel>) : BaseRecyclerAdapter<FaqModel>() {

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerCell<FaqModel> {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_faq, parent, false)
        return FaqViewHolder(context, view)
    }

    override fun dataSource(context: Context): List<FaqModel> {
        return listOf()
    }

    override fun onBindViewHolder(holder: BaseRecyclerCell<FaqModel>, position: Int) {
        val viewHolder = holder as FaqViewHolder
        viewHolder.bind(categories[position])
    }
}