package com.active.orbit.baseapp.design.activities.main

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.enums.BottomNavItemType
import com.active.orbit.baseapp.core.managers.ConsentFormManager
import com.active.orbit.baseapp.core.notifications.NotificationType
import com.active.orbit.baseapp.core.notifications.NotificationsManager
import com.active.orbit.baseapp.core.preferences.engine.Preferences
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.databinding.ActivityPatientBinding
import com.active.orbit.baseapp.design.activities.engine.Activities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.recyclers.models.TripModel
import com.google.android.gms.location.DetectedActivity
import uk.ac.shef.tracker.core.computation.MobilityComputation
import uk.ac.shef.tracker.core.observers.TrackerObserverType
import uk.ac.shef.tracker.core.tracker.TrackerConfig
import uk.ac.shef.tracker.core.tracker.TrackerManager
import uk.ac.shef.tracker.core.utils.TimeUtils
import uk.ac.shef.tracker.core.utils.background
import kotlin.math.roundToInt

class PatientActivity : BaseActivity() {

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
        setToolbarTitle(getString(R.string.app_name))

        prepare()

        val config = TrackerConfig()
        config.baseUrl = Preferences.backend(this).baseUrl
        config.useStepCounter = true
        config.useActivityRecognition = true
        config.useLocationTracking = true
        config.useHeartRateMonitor = false
        config.useMobilityModelling = true
        config.useBatteryMonitor = true
        config.useStayPoints = false
        config.compactLocations = false
        config.uploadData = true

        TrackerManager.getInstance(this).askForPermissionAndStartTracker(config)

        ConsentFormManager.retrieveConsentForm(thiss)

        computeResults()
        scheduleNotification()
    }

    override fun onResume() {
        super.onResume()

        binding.bottomNav.setSelected(BottomNavItemType.MAIN)

        if (!onboarded()) {
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
                var distanceHeart = 0
                var minutesCycling = 0L
                var distanceCycling = 0
                var minutesVehicle = 0L
                var distanceVehicle = 0
                var vehicleTrips = 0
                var steps = 0
                if (mobilityChart.chart.isNotEmpty()) {
                    val summary = mobilityChart.summaryData
                    minutesWalking = summary.walkingMsecs / com.active.orbit.baseapp.core.utils.TimeUtils.ONE_MINUTE_MILLIS
                    distanceWalking = summary.walkingDistance.roundToInt()
                    minutesHeart = summary.runningMsecs / com.active.orbit.baseapp.core.utils.TimeUtils.ONE_MINUTE_MILLIS
                    distanceHeart = summary.runningDistance.roundToInt()
                    minutesCycling = summary.cyclingMsecs / com.active.orbit.baseapp.core.utils.TimeUtils.ONE_MINUTE_MILLIS
                    distanceCycling = summary.cyclingDistance.roundToInt()
                    minutesVehicle = summary.vehicleMsecs / com.active.orbit.baseapp.core.utils.TimeUtils.ONE_MINUTE_MILLIS
                    distanceVehicle = summary.vehicleDistance.roundToInt()
                    vehicleTrips = mobilityChart.trips.filter { it.activityType == DetectedActivity.IN_VEHICLE }.count()
                    steps = summary.steps
                }

                binding.activityPanel.setProgress(minutesWalking, minutesHeart, minutesCycling, distanceWalking, distanceHeart, distanceCycling, steps)
                binding.mobilityPanel.setProgress(vehicleTrips, distanceVehicle, minutesVehicle)
            }

            else -> super.onTrackerUpdate(type, data)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        TrackerManager.getInstance(this).onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun prepare() {

    }


    private fun scheduleNotification() {
        //schedule notification only if it has not been scheduled before
        background {
            if (Preferences.lifecycle(this@PatientActivity).notificationScheduled == Constants.INVALID) {
                val notificationToSchedule = NotificationType.HEALTH
                Preferences.lifecycle(this@PatientActivity).notificationScheduled = notificationToSchedule.id
                NotificationsManager.scheduleNotification(this@PatientActivity, (com.active.orbit.baseapp.core.utils.TimeUtils.ONE_DAY_MILLIS * 30), notificationToSchedule)
            }
        }
    }
}