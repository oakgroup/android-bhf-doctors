/*
 * Copyright (c) Code Developed by Prof. Fabio Ciravegna
 * All rights Reserved
 */


package it.torino.wearostracker.retrieval

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.viewModelScope
import it.torino.tracker.MyViewModel
import it.torino.tracker.utils.Globals
import it.torino.tracker.utils.PreferencesStore
import it.torino.tracker.tracker.TrackerService
import it.torino.tracker.tracker.sensors.heart_rate_monitor.HeartRateData
import it.torino.tracker.tracker.sensors.location_recognition.LocationData
import it.torino.tracker.tracker.sensors.step_counting.StepsData
import it.torino.wearostracker.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ComputeDayDataAsync(
    private val context: Context,
    private val viewModel: MyViewModel,
    private val startTime: Long,
    private val endTime: Long,
    private val whatToCompute: Int
) {
    private var locations = mutableListOf<LocationData>()
    private var steps: MutableList<StepsData> = mutableListOf()
    private var heartRates: MutableList<HeartRateData> = mutableListOf()
    private val TAG: String = javaClass.simpleName
    private val repositoryInstance: it.torino.tracker.Repository? =
        it.torino.tracker.Repository.getInstance(context)

    init {
        viewModel.viewModelScope.launch {
            computeResults(whatToCompute, TrackerService.currentTracker)
            setLiveData(viewModel)
        }
    }

    private suspend fun computeResults(whatToCompute: Int, currentTracker: TrackerService?) {
        withContext(Dispatchers.IO) {
            when (whatToCompute) {
                    MainActivity.COMPUTE_STEPS -> steps =
                        collectStepsFromDatabase(currentTracker)
                    MainActivity.COMPUTE_LOCATIONS -> locations =
                        collectLocationsFromDatabase(currentTracker)
                    MainActivity.COMPUTE_HEART_RATES -> heartRates =
                        collectHeartRatesFromDatabase(currentTracker)
                }
                Result.success(true)
        }
    }


    /**
     * it gets the locations from the database, it gets the steps from the temp list and it combines them
     * then it flushes any remaining locations
     * @param currentTracker the tracker so to get the location tracker module
     * @return a list of locations

     */
    private fun collectLocationsFromDatabase(currentTracker: TrackerService?): MutableList<LocationData> {
        var locations: MutableList<LocationData> =
            repositoryInstance?.dBLocationDao?.getLocationsBetween(startTime, endTime)!!
        if (currentTracker != null) {
            locations.addAll(currentTracker.locationTracker?.locationsDataList!!)
            currentTracker.locationTracker?.flushLocations(context)
        }
        val locUtils = it.torino.tracker.tracker.sensors.location_recognition.LocationUtilities()
        val userPreferences = PreferencesStore()

        val useStayPoints = userPreferences.getBooleanPreference(
            currentTracker,
            Globals.STAY_POINTS, false
        )
        if (useStayPoints!!)
            locations = locUtils.identifyStayPoint(locations)
        val useGlobalSED = userPreferences.getBooleanPreference(
            currentTracker,
            Globals.COMPACTING_LOCATIONS_GENERAL,
            false
        )
        if (useGlobalSED!!)
            locations = locUtils.simplifyLocationsListUsingSED(locations)
        return locations
    }

    /**
     * it gets the hr from the database, it gets the hr from the temp list and it combines them
     * then it flushes any remaining locations
     * @param currentTracker the tracker so to get the location tracker module
     * @return a list of heart rates

     */
    private fun collectHeartRatesFromDatabase(currentTracker: TrackerService?): MutableList<HeartRateData> {
        val heartRates: MutableList<HeartRateData> =
            repositoryInstance?.dBHeartRateDao?.getHeartRateBetween(startTime, endTime)!!
        if (currentTracker != null) {
            heartRates.addAll(currentTracker.heartMonitor?.heartRateReadingStack!!)
            currentTracker.heartMonitor?.flush()
        }
        return heartRates
    }

    /**
     * it gets the steps from the database, it gets the steps from the temp list and it combines them
     * then it flushes any remaining locations
     * @param currentTracker the tracker so to get the step counter module
     * @return a list of steps
     */
    private fun collectStepsFromDatabase(currentTracker: TrackerService?): MutableList<StepsData> {
        val steps = repositoryInstance?.dBStepsDao?.getStepsBetween(startTime, endTime)!!
        if (currentTracker != null) {
            val newStepsList = currentTracker.stepCounter?.stepsDataList
            steps.addAll(newStepsList!!)
            TrackerService.currentTracker?.stepCounter?.flush()
        }
        computeCadenceForSteps(steps)
        return steps
    }

    /**
     * it assigns the cadence based on the sequence of steps
     * @param steps the list of steps for a day
     */
    private fun computeCadenceForSteps(steps: MutableList<StepsData>) {
        var prevStepsData: StepsData? = null
        for (stepData in steps) {
            if (prevStepsData != null) {
                stepData.cadence = stepData.computeCadence(prevStepsData)
            }
            prevStepsData = stepData
        }
    }

    /**
     * it provides the results to the live data for the interface to display if necessary
     */
    private suspend fun setLiveData(viewModel: MyViewModel) {
        withContext(Dispatchers.Main) {
            // running on ui thread
            if (Looper.getMainLooper() != null) {
                Handler(Looper.getMainLooper()).post {
                    try {
                        if (steps.isNotEmpty()) {
                            viewModel.setStepsDataList(steps)
                        }
                    } catch (error: Exception) {
                        Log.i(TAG, error.message!!)
                    }
                    try {
                        if (locations.isNotEmpty()) {
                            viewModel.setLocationsDataList(locations)
                        }
                    } catch (error: Exception) {
                        Log.i(TAG, error.message!!)
                    }
                    try {
                        if (heartRates.isNotEmpty()) {
                            viewModel.setHeartRates(heartRates)
                            Result.success(true)
                        }
                    } catch (error: Exception) {
                        Log.i(TAG, error.message!!)
                        Result.failure<java.lang.Exception>(error)
                    }

                }
            }
        }
    }
}