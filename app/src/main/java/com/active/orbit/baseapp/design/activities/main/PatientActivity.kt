package com.active.orbit.baseapp.design.activities.main

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.enums.BottomNavItemType
import com.active.orbit.baseapp.core.enums.SecondaryPanelType
import com.active.orbit.baseapp.core.listeners.ResultListener
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.databinding.ActivityPatientBinding
import com.active.orbit.baseapp.design.activities.engine.Activities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation
import com.active.orbit.baseapp.design.models.PanelModel
import com.active.orbit.baseapp.design.recyclers.models.TripModel
import com.active.orbit.tracker.core.computation.MobilityComputation
import com.active.orbit.tracker.core.observers.TrackerObserverType
import com.active.orbit.tracker.core.tracker.TrackerConfig
import com.active.orbit.tracker.core.tracker.TrackerManager
import com.active.orbit.tracker.core.utils.TimeUtils
import kotlin.math.roundToInt

class PatientActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityPatientBinding
    private var lastTimeClosed: Long = 0

    companion object {
        var displayedTripsList: List<TripModel> = ArrayList()
        var currentTripPosition: Int = 0
        var currentTrip: TripModel? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showMenuComponent()
        showLogo()

        prepare()

        val config = TrackerConfig()
        config.useStepCounter = true
        config.useActivityRecognition = true
        config.useLocationTracking = true
        config.useHeartRateMonitor = true
        config.useMobilityModelling = true
        config.useBatteryMonitor = true
        config.useStayPoints = true
        config.compactLocations = true
        config.uploadData = true

        TrackerManager.getInstance(this).askForPermissionAndStartTracker(config)

        computeResults()
    }

    override fun onResume() {
        super.onResume()

        binding.bottomNav.setSelected(BottomNavItemType.MAIN)

        onboarded(object : ResultListener {
            override fun onResult(success: Boolean) {
                if (!success) {
                    // we cannot be here if the app has not been correctly onboarded
                    Router.getInstance()
                        .newTask(true)
                        .startBaseActivity(this@PatientActivity, Activities.SPLASH)
                    finish()
                } else {
                    TrackerManager.getInstance(this@PatientActivity).onResume()

                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastTimeClosed > 180000)
                        TrackerManager.getInstance(this@PatientActivity).currentDateTime = TimeUtils.midnightInMsecs(currentTime)
                    Logger.i("Computing results")
                    computeResults()
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()
        TrackerManager.getInstance(this).onPause()
        lastTimeClosed = System.currentTimeMillis()
    }

    override fun onTrackerUpdate(type: TrackerObserverType, data: Any) {
        when (type) {
            TrackerObserverType.MOBILITY -> {
                val mobilityChart = data as MobilityComputation

                var minutesWalking = 0L
                var distanceWalking = 0
                var minutesHeart = 0L
                if (mobilityChart.chart.isNotEmpty()) {
                    val summary = mobilityChart.summaryData
                    minutesWalking = summary.walkingMsecs / TimeUtils.ONE_MINUTE_MILLIS
                    distanceWalking = summary.walkingDistance.roundToInt()
                    minutesHeart = summary.runningMsecs / TimeUtils.ONE_MINUTE_MILLIS
                }

                val modelActivity = PanelModel()
                modelActivity.value1 = distanceWalking
                modelActivity.value2 = minutesWalking.toInt()
                modelActivity.value3 = minutesHeart.toInt()
                binding.activityPanel.updatePanel(SecondaryPanelType.ACTIVITY, modelActivity)

                val modelMobility = PanelModel()
                modelMobility.value1 = 0 // TODO tracker data
                modelMobility.value2 = 0 // TODO tracker data
                binding.mobilityPanel.updatePanel(SecondaryPanelType.MOBILITY, modelMobility)

                val modelPhysiology = PanelModel()
                modelPhysiology.value1 = 0 // TODO tracker data
                modelPhysiology.value2 = 0 // TODO tracker data
                binding.physiologyPanel.updatePanel(SecondaryPanelType.PHYSIOLOGY, modelPhysiology)
            }
            else -> super.onTrackerUpdate(type, data)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        TrackerManager.getInstance(this).onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun prepare() {
        binding.activityPanel.setPanel(SecondaryPanelType.ACTIVITY)
        binding.mobilityPanel.setPanel(SecondaryPanelType.MOBILITY)
        binding.physiologyPanel.setPanel(SecondaryPanelType.PHYSIOLOGY)

        binding.activityPanel.disableClick()
        binding.mobilityPanel.disableClick()
        binding.physiologyPanel.disableClick()

        binding.activityPanel.setOnClickListener(this)
        binding.mobilityPanel.setOnClickListener(this)
        binding.physiologyPanel.setOnClickListener(this)

        // TODO remove this used for debugging purposes
        binding.activityPanel.setOnLongClickListener {
            Router.getInstance()
                .activityAnimation(ActivityAnimation.LEFT_RIGHT)
                .startBaseActivity(this, Activities.DEBUG)
            true
        }
    }

    override fun onClick(v: View?) {

        when (v) {
            binding.activityPanel -> {
                Router.getInstance()
                    .activityAnimation(ActivityAnimation.LEFT_RIGHT)
                    .startBaseActivity(this, Activities.ACTIVITY)
            }

            binding.mobilityPanel -> {
                Router.getInstance()
                    .activityAnimation(ActivityAnimation.LEFT_RIGHT)
                    .startBaseActivity(this, Activities.ACTIVITY)
            }

            binding.physiologyPanel -> {
                Router.getInstance()
                    .activityAnimation(ActivityAnimation.LEFT_RIGHT)
                    .startBaseActivity(this, Activities.ACTIVITY)
            }
        }
    }
}