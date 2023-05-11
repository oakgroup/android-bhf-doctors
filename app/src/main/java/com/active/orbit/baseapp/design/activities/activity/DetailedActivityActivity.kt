package com.active.orbit.baseapp.design.activities.activity

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.active.orbit.baseapp.databinding.ActivityDetailedActivityBinding
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.recyclers.adapters.ActivitiesAdapter
import com.active.orbit.baseapp.design.recyclers.models.TripModel
import com.active.orbit.tracker.core.database.models.DBTrip
import com.google.android.gms.location.DetectedActivity

class DetailedActivityActivity : BaseActivity() {

    private lateinit var binding: ActivityDetailedActivityBinding
    private var adapter: ActivitiesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailedActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showBackButton()
        showLogoButton()

        prepare()
    }

    private fun prepare() {
        val linearLayoutManager = LinearLayoutManager(this)
        binding.activitiesRecyclerView.layoutManager = linearLayoutManager

        adapter = ActivitiesAdapter(this)
        binding.activitiesRecyclerView.adapter = adapter

        initObservers()
    }

    private fun initObservers() {
        // TODO tracker rework
        /*
        viewModel.tripsList?.observe(this) { tripsList ->
            showActivities(tripsList)
        }
        */
    }

    private fun showActivities(tripsList: List<DBTrip>) {
        var tripModels = tripsList.map { TripModel(it) }
        tripModels = tripModels.filter { it.activityType != DetectedActivity.STILL }
        if (tripsList.isEmpty()) {
            binding.noActivities.visibility = View.VISIBLE
        } else {
            binding.noActivities.visibility = View.GONE
        }
        adapter?.tripModels = ArrayList(tripModels)
        adapter?.replaceAll(ArrayList(tripModels))
    }

    override fun onDestroy() {
        super.onDestroy()

        // TODO tracker rework
        // viewModel.tripsList?.removeObservers(this)
    }
}
