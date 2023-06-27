package com.active.orbit.baseapp.design.activities.symptoms

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.enums.BottomNavItemType
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.databinding.ActivitySymptomsBinding
import com.active.orbit.baseapp.design.activities.engine.Activities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation
import com.active.orbit.baseapp.design.recyclers.adapters.SymptomsAdapter
import com.active.orbit.baseapp.design.recyclers.listeners.RefreshListener
import com.active.orbit.baseapp.design.utils.UiUtils

class SymptomsActivity : BaseActivity() {

    companion object {
        const val SYMPTOM_REQUEST_CODE = 912
        private const val SYMPTOMS_MAX = 20
    }

    private lateinit var binding: ActivitySymptomsBinding
    private var adapter: SymptomsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySymptomsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showMenuComponent()
        setToolbarTitle(getString(R.string.app_name))

        binding.bottomNav.setSelected(BottomNavItemType.TRIPS)

        prepare()
    }

    override fun onResume() {
        super.onResume()

        showSymptoms()
    }

    private fun prepare() {

        val linearLayoutManager = LinearLayoutManager(this)
        binding.symptomsRecyclerView.layoutManager = linearLayoutManager

        adapter = SymptomsAdapter(this)
        binding.symptomsRecyclerView.adapter = adapter

        binding.symptomsRecyclerView.isVerticalScrollBarEnabled = false
        binding.symptomsRecyclerView.isNestedScrollingEnabled = false

        binding.noSymptoms.text = getString(R.string.symptoms_empty_patient)
        binding.btnAddSymptom.visibility = View.VISIBLE
        binding.btnAddSymptom.setOnClickListener {
            if ((adapter?.itemCount ?: 0) < SYMPTOMS_MAX) {
                Router.getInstance()
                    .activityAnimation(ActivityAnimation.LEFT_RIGHT)
                    .startBaseActivityForResult(this, Activities.REPORT_SYMPTOM_DETAILS, Bundle(), SYMPTOM_REQUEST_CODE)
            } else {
                UiUtils.showLongToast(this, getString(R.string.full_symptoms_list_message))
            }
        }
    }

    private fun showSymptoms() {
        adapter?.refresh(this, object : RefreshListener {
            override fun onRefreshed(itemCount: Int) {
                if (itemCount == 0) {
                    binding.noSymptoms.visibility = View.VISIBLE
                } else {
                    binding.noSymptoms.visibility = View.GONE
                }
            }
        })
    }
}