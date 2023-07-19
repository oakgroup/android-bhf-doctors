package com.active.orbit.baseapp.design.activities.questionnaire

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.enums.BottomNavItemType
import com.active.orbit.baseapp.core.managers.HealthManager
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.databinding.ActivityHealthBinding
import com.active.orbit.baseapp.design.activities.engine.Activities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation
import com.active.orbit.baseapp.design.recyclers.adapters.HealthAdapter
import com.active.orbit.baseapp.design.recyclers.listeners.RefreshListener
import com.active.orbit.baseapp.design.utils.UiUtils

class HealthActivity : BaseActivity(), View.OnClickListener {

    companion object {
        const val HEALTH_REQUEST_CODE = 312
        private const val HEALTH_RESPONSES_MAX = 20
    }

    private lateinit var binding: ActivityHealthBinding
    private var adapter: HealthAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHealthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showMenuComponent()
        setToolbarTitle(getString(R.string.app_name))
        binding.bottomNav.setSelected(BottomNavItemType.HEALTH)

        prepare()

        HealthManager.checkForNotUploaded(this)
    }

    override fun onResume() {
        super.onResume()

        showResponses()
    }

    private fun prepare() {

        binding.btnFillQuestionnaire.setOnClickListener(this)

        val linearLayoutManager = LinearLayoutManager(this)
        binding.healthRecyclerView.layoutManager = linearLayoutManager

        adapter = HealthAdapter(this)
        binding.healthRecyclerView.adapter = adapter

        binding.noHealth.text = getString(R.string.health_empty_patient)
        binding.btnFillQuestionnaire.visibility = View.VISIBLE
    }

    private fun showResponses() {
        adapter?.refresh(this, object : RefreshListener {
            override fun onRefreshed(itemCount: Int) {
                if (itemCount == 0) {
                    binding.noHealth.visibility = View.VISIBLE
                } else {
                    binding.noHealth.visibility = View.GONE
                }
            }
        })
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnFillQuestionnaire -> {
                if ((adapter?.itemCount ?: 0) < HEALTH_RESPONSES_MAX) {
                    Router.getInstance().activityAnimation(ActivityAnimation.LEFT_RIGHT).startBaseActivityForResult(this, Activities.HEALTH_MOBILITY, Bundle(), HEALTH_REQUEST_CODE)
                } else {
                    UiUtils.showLongToast(this, getString(R.string.full_health_list_message))
                }
            }
        }
    }
}