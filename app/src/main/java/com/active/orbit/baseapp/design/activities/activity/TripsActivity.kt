package com.active.orbit.baseapp.design.activities.activity

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.enums.BottomNavItemType
import com.active.orbit.baseapp.databinding.ActivityTripsBinding
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.recyclers.adapters.ActivitiesAdapter
import com.active.orbit.baseapp.design.recyclers.models.TripModel
import com.google.android.gms.location.DetectedActivity
import uk.ac.shef.tracker.core.computation.MobilityComputation
import uk.ac.shef.tracker.core.computation.data.MobilityData
import uk.ac.shef.tracker.core.database.models.TrackerDBTrip
import uk.ac.shef.tracker.core.observers.TrackerObserverType

class TripsActivity : BaseActivity() {

    private lateinit var binding: ActivityTripsBinding
    private var adapter: ActivitiesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showMenuComponent()
        setToolbarTitle(getString(R.string.app_name))
        binding.bottomNav.setSelected(BottomNavItemType.TRIPS)


        prepare()
    }

    private fun prepare() {
        val linearLayoutManager = LinearLayoutManager(this)
        binding.activitiesRecyclerView.layoutManager = linearLayoutManager

        adapter = ActivitiesAdapter(this)
        binding.activitiesRecyclerView.adapter = adapter

        computeResults()
    }

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
