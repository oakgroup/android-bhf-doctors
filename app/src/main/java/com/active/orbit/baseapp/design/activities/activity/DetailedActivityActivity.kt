package com.active.orbit.baseapp.design.activities.activity

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.active.orbit.baseapp.databinding.ActivityDetailedActivityBinding
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.recyclers.adapters.ActivitiesAdapter
import com.active.orbit.baseapp.design.recyclers.models.TripModel
import com.active.orbit.tracker.core.computation.MobilityComputation
import com.active.orbit.tracker.core.computation.data.MobilityData
import com.active.orbit.tracker.core.database.models.TrackerDBTrip
import com.active.orbit.tracker.core.observers.TrackerObserverType
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

        computeResults()
    }

    @Suppress("UNCHECKED_CAST")
    override fun onTrackerUpdate(type: TrackerObserverType, data: Any) {
        when (type) {
            TrackerObserverType.MOBILITY -> {
                val mobilityChart = data as MobilityComputation
                showActivities(mobilityChart.trips)
            }
            else -> super.onTrackerUpdate(type, data)
        }
    }

    private fun showActivities(tripsList: List<TrackerDBTrip>) {
        var tripModels = tripsList.map { TripModel(it) }
        tripModels = tripModels.filter { it.activityType != DetectedActivity.STILL && it.activityType != DetectedActivity.UNKNOWN && it.activityType != MobilityData.INVALID_VALUE }
        if (tripModels.isEmpty()) {
            binding.noActivities.visibility = View.VISIBLE
        } else {
            binding.noActivities.visibility = View.GONE
        }
        adapter?.tripModels = ArrayList(tripModels)
        adapter?.replaceAll(ArrayList(tripModels))
    }
}
