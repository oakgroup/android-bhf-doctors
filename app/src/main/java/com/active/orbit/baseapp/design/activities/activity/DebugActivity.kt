package com.active.orbit.baseapp.design.activities.activity

import android.annotation.SuppressLint
import android.os.Bundle
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.databinding.ActivityDebugBinding
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import uk.ac.shef.tracker.core.computation.MobilityComputation
import uk.ac.shef.tracker.core.computation.data.MobilityData
import uk.ac.shef.tracker.core.database.models.TrackerDBActivity
import uk.ac.shef.tracker.core.database.models.TrackerDBLocation
import uk.ac.shef.tracker.core.database.models.TrackerDBStep
import uk.ac.shef.tracker.core.observers.TrackerObserverType
import uk.ac.shef.tracker.core.utils.Constants
import uk.ac.shef.tracker.core.utils.TimeUtils

class DebugActivity : BaseActivity() {

    private lateinit var binding: ActivityDebugBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDebugBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showBackButton()
        setToolbarTitle(R.string.debug_view)
        setToolbarTitleColor(R.color.textColorWhite)

        computeResults()
    }

    @Suppress("UNCHECKED_CAST")
    override fun onTrackerUpdate(type: TrackerObserverType, data: Any) {
        when (type) {
            TrackerObserverType.ACTIVITIES -> refreshActivitiesData(data as List<TrackerDBActivity>)
            TrackerObserverType.LOCATIONS -> refreshLocationsData(data as List<TrackerDBLocation>)
            TrackerObserverType.STEPS -> refreshStepsData(data as List<TrackerDBStep>)
            TrackerObserverType.MOBILITY -> {
                val mobilityChart = data as MobilityComputation
                refreshMobilityChart(mobilityChart.chart)
            }
            else -> super.onTrackerUpdate(type, data)
        }
    }

    /**
     * Called by the observer of viewModel.mobilityChart
     * @param mobilityChart the  mobility chart received
     */
    @SuppressLint("SetTextI18n")
    private fun refreshMobilityChart(mobilityChart: List<MobilityData>?) {
        val textField = binding.mobilityChart
        if (!mobilityChart.isNullOrEmpty()) {
            var concatenatedString = ""
            for (mobilityElement in mobilityChart) {
                concatenatedString += "\n" + mobilityElement.toString()
            }
            Logger.i(concatenatedString)
            textField.text = "chart: $concatenatedString \n"
        }
    }

    /**
     * Called by the observer of viewModel.stepsDataList
     * @param stepsList teh step list received
     */
    @SuppressLint("SetTextI18n")
    private fun refreshStepsData(stepsList: List<TrackerDBStep>) {
        val textField = binding.steps
        var numberOfSteps = 0
        var concatenatedString = ""
        var previoussteps = 0
        if (stepsList.isNotEmpty())
            previoussteps = stepsList[0].steps
        for (stepData in stepsList) {
            stepData.let {
                numberOfSteps += it.steps - previoussteps
                concatenatedString += "\n" + TimeUtils.formatMillis(it.timeInMillis, Constants.DATE_FORMAT_HOUR_MINUTE_SECONDS) + " " + (it.steps - previoussteps)
                previoussteps = it.steps
            }
        }
        textField.text = "Number of Steps: $numberOfSteps $concatenatedString"
    }

    /**
     * Called by the observer of viewModel.activitiesDataList
     * @param activityList teh list of activities received
     */
    @SuppressLint("SetTextI18n")
    private fun refreshActivitiesData(activityList: List<TrackerDBActivity>) {
        val textField = binding.activities
        var activitiesString = ""
        for (activityData in activityList)
            activitiesString += "\n   $activityData"
        textField.text = "Activities: " + activityList.size + activitiesString
    }


    /**
     * Called by the observer of viewModel.localtionsList
     * @param locationsList the list of locations
     */
    @SuppressLint("SetTextI18n")
    private fun refreshLocationsData(locationsList: List<TrackerDBLocation?>) {
        val textField = binding.locations
        var locationsString = ""
        for (location in locationsList)
            locationsString += "\n   $location"
        textField.text = "Locations: " + locationsList.size + locationsString
    }


    /**
     * This refreshes the content of the interface     *
     */
    private fun resetInterface() {
        refreshActivitiesData(ArrayList())
        refreshLocationsData(ArrayList())
        refreshStepsData(ArrayList())
        refreshMobilityChart(ArrayList())
    }

    fun clearInterface() {
        binding.locations.text = ""
        binding.steps.text = ""
        binding.activities.text = ""
        binding.mobilityChart.text = ""
    }
}