/*
 * Copyright (c) Code Developed by Prof. Fabio Ciravegna
 * All rights Reserved
 */

package it.torino.wearostracker

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.wear.widget.drawer.WearableActionDrawerView
import com.active.orbit.baseapp.core.utils.Utils
import com.active.orbit.baseapp.design.activities.engine.BaseActivity

import uk.ac.shef.tracker.core.tracker.TrackerManager
import it.torino.wearostracker.databinding.ActivityMainBinding
import it.torino.wearostracker.retrieval.ComputeDayDataAsync
import it.torino.wearostracker.R
import uk.ac.shef.tracker.core.database.queries.TrackerHeartRates

class MainActivity : BaseActivity() {

    private var visible: CharSequence? = ""
    lateinit var binding: ActivityMainBinding
    private var wearableActionDrawer: WearableActionDrawerView? = null
    private var clicksCount =0

    companion object {
        lateinit var HEART_RATE_READINGS: String
        lateinit var STEPS_DATA: String
        lateinit var WEAR_SYNC: String
        lateinit var HEART_RATE_DATA: String
        lateinit var SEND_DATA: String

        val TAG = this::class.simpleName
        const val COMPUTE_STEPS = 0
        const val COMPUTE_LOCATIONS = 1
        const val COMPUTE_HEART_RATES = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initObservers()
        HEART_RATE_READINGS = getString(R.string.heart_rate_summary)
        STEPS_DATA = getString( R.string.steps)
        WEAR_SYNC = getString(R.string.wearsync)
        HEART_RATE_DATA = getString(R.string.heart_rate)
        SEND_DATA = getString(R.string.send_data)

        // initialising the interface
        initDrawers()

        binding.heartRateView.setOnClickListener {
            when (clicksCount){
                0-> {
                    binding.heartRateView.setBackgroundColor(Color.RED)
                    clicksCount++
                }
                1 -> {

                }
            }
        }
        //@todo set the tracker preferences
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        super.onPause()
    }


    private fun initObservers() {
        // changes to the UI when the data changes
        viewModel.heartRates.observe(this) { heartRatesList ->
            Log.i(TAG, "inserting heart rates")
            if (heartRatesList != null) {
                refreshData(heartRatesList)
            }
        }


    }

    private fun initDrawers() {
        // Top navigation drawer
        wearableActionDrawer = findViewById(R.id.bottom_action_drawer)
        // Peeks action drawer on the bottom.
        wearableActionDrawer!!.controller.peekDrawer()
        wearableActionDrawer!!.setOnMenuItemClickListener { item ->
            wearableActionDrawer!!.controller.closeDrawer()
            updateFragment(item.title)
            false
        }
    }

    /**
     * called when we want a specific fragment to become visibile
     *
     * @param title the name of the fragment to display (e.g. LAST_N_BEACON_DATA)
     */
    private fun updateFragment(title: CharSequence?) {
        visible = title
        val midnight = midnightinMsecs(TrackerManager.getInstance(this).currentDateTime)
        when (title) {
            HEART_RATE_READINGS -> {
                Log.i(TAG, "requesting the list of HR readings for the date")
                ComputeDayDataAsync(
                    this,
                    viewModel, midnight, midnight + Globals.MSECS_IN_A_DAY, COMPUTE_HEART_RATES
                )
            }
            STEPS_DATA -> {
                Log.i(TAG, "requesting the list of steps for the date")
                ComputeDayDataAsync(
                    this,
                    viewModel, midnight, midnight + Globals.MSECS_IN_A_DAY, COMPUTE_STEPS
                )
            }
            WEAR_SYNC -> {
                Log.i(TAG, "requesting wear sync")
                val intent = Intent(this, WearSync::class.java)
                startActivity(intent)
            }
            HEART_RATE_DATA -> {
                Log.i(TAG, "requesting the current HR readings ")
                refreshData(ArrayList<TrackerHeartRates>())
            }
            SEND_DATA -> {
                Log.i(TAG, "sending data ")
                val trackerRestarter = TrackerRestarter()
                trackerRestarter.startDataUploader(this, false)
            }
        }
    }

    /**
     * it writes the selected data on the screen and on the Log
     *
     * @param elementList a list of elements to be displayed
     */
    private fun refreshData(elementList: List<*>) {
        binding.outputText.text = ""
        Log.i(TAG, "requesting $visible")
        when (visible) {
            HEART_RATE_DATA -> {
                binding.outputText.visibility = View.GONE
                binding.fancyView.visibility = View.GONE
                binding.heartRateBpm.visibility = View.VISIBLE
                binding.heartRateTime.visibility = View.VISIBLE
                binding.progressBar.visibility = View.VISIBLE
            }
            HEART_RATE_READINGS -> {
                Log.i(TAG, "showing ${elementList.size} hrs")
                for (trackedData in elementList) {
                    val str = "${trackedData}\n".trimIndent()
                    binding.outputText.append(str)
                }
                if (elementList.isEmpty())
                    binding.fancyView.text = getString(R.string.none)
                binding.outputText.visibility = View.VISIBLE
                binding.fancyView.visibility = View.GONE
                binding.heartRateBpm.visibility = View.GONE
                binding.heartRateTime.visibility = View.GONE
            }
            STEPS_DATA -> {
                Log.i(TAG, "showing ${elementList.size} steps")
                for (trackedData in elementList) {
                    val str = "${trackedData}\n".trimIndent()
                    binding.outputText.append(str)
                }
                if (elementList.isEmpty())
                    binding.fancyView.text = getString(R.string.none)
                binding.outputText.visibility = View.VISIBLE
                binding.fancyView.visibility = View.GONE
                binding.heartRateBpm.visibility = View.GONE
                binding.heartRateTime.visibility = View.GONE
            }
            WEAR_SYNC -> {

            }
        }
    }
}