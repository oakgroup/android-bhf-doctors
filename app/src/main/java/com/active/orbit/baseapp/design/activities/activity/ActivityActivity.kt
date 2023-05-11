package com.active.orbit.baseapp.design.activities.activity

import android.os.Bundle
import androidx.core.content.ContextCompat
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.core.utils.TimeUtils
import com.active.orbit.baseapp.core.utils.Utils
import com.active.orbit.baseapp.databinding.ActivityActivityBinding
import com.active.orbit.baseapp.design.activities.engine.Activities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation

class ActivityActivity : BaseActivity() {

    private lateinit var binding: ActivityActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showBackButton()
        showLogoButton()

        prepare()

        Utils.delay(TimeUtils.ONE_SECOND_MILLIS.toLong() / 3) {
            initObservers()
        }
    }

    private fun prepare() {

        binding.btnDetailedActivity.setOnClickListener {
            Router.getInstance()
                .activityAnimation(ActivityAnimation.LEFT_RIGHT)
                .startBaseActivity(this, Activities.DETAILED_ACTIVITY)
        }

        binding.walkingActivityProgress.setBackgroundLineColor(ContextCompat.getColor(this, R.color.colorPrimaryLight))
        binding.walkingActivityProgress.setProgressLineColor(ContextCompat.getColor(this, R.color.colorAccent))
        binding.walkingActivityProgress.setProgressIconResource(R.drawable.ic_activity)
        binding.walkingActivityProgress.setLineWidth(40f)
        binding.walkingActivityProgress.setMaxProgress(200.toFloat())

        binding.heartActivityProgress.setBackgroundLineColorOne(ContextCompat.getColor(this, R.color.colorPrimaryLight))
        binding.heartActivityProgress.setBackgroundLineColorTwo(ContextCompat.getColor(this, R.color.colorPrimaryLight))
        binding.heartActivityProgress.setProgressLineColorOne(ContextCompat.getColor(this, R.color.colorAccent))
        binding.heartActivityProgress.setProgressLineColorTwo(ContextCompat.getColor(this, R.color.colorAccent))
        binding.heartActivityProgress.setProgressIconResource(R.drawable.ic_activity)
        binding.heartActivityProgress.setLineWidth(40f)
        binding.heartActivityProgress.setMaxProgress(Constants.NHS_WEEK_HEART_TARGET.toFloat())

        binding.pullToRefresh.setOnRefreshListener {
            computeResults()
        }
    }

    /**
     * This initialises the view model and the methods used to retrieve the live data for the interface
     */
    private fun initObservers() {

        // TODO tracker rework
        /*
        viewModel.mobilityChart?.observe(this) { mobilityChart ->
            var minutesWalking = 0L
            var distanceWalking = 0
            var minutesHeart = 0L
            var distanceHeart = 0
            var minutesCycling = 0L
            var distanceCycling = 0
            var minutesVehicle = 0L
            var distanceVehicle = 0
            var steps = 0
            if (mobilityChart.chart.isNotEmpty()) {
                val summary = mobilityChart.summaryData
                minutesWalking = summary.walkingMsecs / TimeUtils.ONE_MINUTE_MILLIS
                distanceWalking = summary.walkingDistance.roundToInt()
                minutesHeart = summary.runningMsecs / TimeUtils.ONE_MINUTE_MILLIS
                distanceHeart = summary.runningDistance.roundToInt()
                minutesCycling = summary.cyclingMsecs / TimeUtils.ONE_MINUTE_MILLIS
                distanceCycling = summary.cyclingDistance.roundToInt()
                minutesVehicle = summary.vehicleMsecs / TimeUtils.ONE_MINUTE_MILLIS
                distanceVehicle = summary.vehicleDistance.roundToInt()
                steps = summary.steps
            }

            binding.heartActivityProgress.setProgress(minutesHeart.toFloat())
            binding.walkingActivityProgress.setProgress(minutesWalking.toFloat())

            if (minutesWalking == 1L) binding.walkingProgressText.text = getString(R.string.activity_distance_active_minute)
            else binding.walkingProgressText.text = getString(R.string.activity_distance_active_minutes, minutesWalking.toString(), distanceWalking.toString())

            if (minutesHeart == 1L) binding.heartProgressText.text = getString(R.string.activity_distance_heart_minute)
            else binding.heartProgressText.text = getString(R.string.activity_distance_heart_minutes, minutesHeart.toString(), distanceHeart.toString())

            if (minutesCycling == 1L) binding.bicycleProgressText.text = getString(R.string.activity_distance_cycling_minute)
            else binding.bicycleProgressText.text = getString(R.string.activity_distance_cycling_minutes, minutesCycling.toString(), distanceCycling.toString())

            if (minutesVehicle == 1L) binding.vehicleProgressText.text = getString(R.string.activity_distance_vehicle_minute)
            else binding.vehicleProgressText.text = getString(R.string.activity_distance_vehicle_minutes, minutesVehicle.toString(), distanceVehicle.toString())

            if (steps == 1) binding.stepsProgressText.text = getString(R.string.activity_step)
            else binding.stepsProgressText.text = getString(R.string.activity_steps, steps.toString())

            binding.pullToRefresh.isRefreshing = false
        }
        */
    }

    override fun onDestroy() {
        super.onDestroy()

        // TODO tracker rework
        // viewModel.mobilityChart?.removeObservers(this)
    }
}