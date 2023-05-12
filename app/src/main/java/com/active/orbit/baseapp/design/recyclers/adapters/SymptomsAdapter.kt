package com.active.orbit.baseapp.design.recyclers.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.database.models.DBReportSymptom
import com.active.orbit.baseapp.core.database.models.DBSymptom
import com.active.orbit.baseapp.core.database.tables.TableReportedSymptoms
import com.active.orbit.baseapp.core.database.tables.TableSymptoms
import com.active.orbit.baseapp.core.preferences.engine.Preferences
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.recyclers.adapters.holders.SymptomViewHolder
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerAdapter
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerCell

class SymptomsAdapter(private var activity: BaseActivity) : BaseRecyclerAdapter<DBReportSymptom>() {

    override fun dataSource(context: Context): List<DBReportSymptom> {
        return TableReportedSymptoms.getAll(activity)
    }

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerCell<DBReportSymptom> {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_symptom, parent, false)
        return SymptomViewHolder(activity, view)
    }
}