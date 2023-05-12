package com.active.orbit.baseapp.design.activities.activity

import android.annotation.SuppressLint
import android.os.Bundle
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.databinding.ActivityDebugBinding
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.tracker.core.computation.data.MobilityData
import com.active.orbit.tracker.core.database.models.DBActivity
import com.active.orbit.tracker.core.database.models.DBLocation
import com.active.orbit.tracker.core.database.models.DBStep
import com.active.orbit.tracker.core.utils.TimeUtils

class DebugActivity : BaseActivity() {

    private lateinit var binding: ActivityDebugBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDebugBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showBackButton()
        setToolbarTitle(R.string.debug_view)
        setToolbarTitleColor(R.color.textColorWhite)

        initObservers()

        // TODO wear synchronisation
        /*
        binding.wearSync.setOnClickListener {
            Logger.i("Requesting wear sync")
            val intent = Intent(this, WearSync::class.java)
            startActivity(intent)
        }
        */
    }

    /**
     * This initialises the view model and the methods used to retrieve the live data for the interface
     */
    private fun initObservers() {
        // TODO tracker rework
        /*
        // changes to the UI when the data changes
        viewModel.stepsDataList.observe(this) { stepsList ->
            Logger.i("Inserting steps")
            refreshStepsData(stepsList)
        }
        viewModel.activitiesDataList.observe(this) { activityList ->
            Logger.i("Inserting activities")
            refreshActivitiesData(activityList)
        }
        viewModel.locationsDataList.observe(this) { locationsList ->
            Logger.i("Inserting locations")
            refreshLocationsData(locationsList)
        }
        viewModel.mobilityChart?.observe(this) { mobilityChart ->
            Logger.i("Inserting chart")
            refreshMobilityChart(mobilityChart.chart)
        }
        */
    }

    /**
     * Called by the observer of viewModel.mobilityChart
     * @param mobilityChart the  mobility chart received
     */
    @SuppressLint("SetTextI18n")
    private fun refreshMobilityChart(mobilityChart: List<MobilityData>?) {
        val textField = binding.mobilityChart
        if (mobilityChart != null && mobilityChart.isNotEmpty()) {
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
    private fun refreshStepsData(stepsList: List<DBStep>) {
        val textField = binding.steps
        var numberOfSteps = 0
        var concatenatedString = ""
        var previoussteps = 0
        if (stepsList.isNotEmpty())
            previoussteps = stepsList[0].steps
        for (stepData in stepsList) {
            stepData.let {
                numberOfSteps += it.steps - previoussteps
                concatenatedString += "\n" + TimeUtils.formatMillis(it.timeInMillis, "HH:mm:ss") + " " + (it.steps - previoussteps)
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
    private fun refreshActivitiesData(activityList: List<DBActivity>) {
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
    private fun refreshLocationsData(locationsList: List<DBLocation?>) {
        val textField = binding.locations
        var locationsString = ""
        for (location in locationsList)
            locationsString += "\n   $location"
        textField.text = "Locations: " + locationsList.size + locationsString
    }


    /**
     * This refreshes the content of the interface     *
     * @param binding the binding of the interface
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

    override fun onDestroy() {
        super.onDestroy()

        // TODO tracker rework
        /*
        viewModel.stepsDataList.removeObservers(this)
        viewModel.activitiesDataList.removeObservers(this)
        viewModel.locationsDataList.removeObservers(this)
        viewModel.mobilityChart?.removeObservers(this)
        */
    }
}