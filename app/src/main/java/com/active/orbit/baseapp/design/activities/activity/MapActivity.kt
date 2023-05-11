package com.active.orbit.baseapp.design.activities.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.databinding.ActivityMapBinding
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.main.PatientActivity
import com.active.orbit.baseapp.design.recyclers.models.TripModel
import com.active.orbit.baseapp.design.utils.ActivityUtils
import com.active.orbit.baseapp.design.utils.CadenceGraphDisplay
import com.active.orbit.tracker.core.database.models.DBLocation
import com.google.android.gms.location.DetectedActivity
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*


class MapActivity : BaseActivity(), OnMapReadyCallback, View.OnClickListener {

    private lateinit var binding: ActivityMapBinding

    private var map: GoogleMap? = null
    private var currentTrip = PatientActivity.currentTrip
    private var tripPosition = PatientActivity.currentTripPosition
    private var displayedTripsList = PatientActivity.displayedTripsList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showBackButton()
        showLogoButton()

        prepare()
    }


    private fun prepare() {
        MapsInitializer.initialize(this)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        decideOnFabsVisibility()
        binding.floatingLeftArrow.setOnClickListener(this)
        binding.floatingRightArrow.setOnClickListener(this)

        binding.itemActivity.rightIconFrame.visibility = View.GONE
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.floatingLeftArrow -> {
                tripPosition--
                currentTrip = displayedTripsList[tripPosition]
                showTrip()
                showCadenceIfRelevant()
                drawMap(map!!)
                decideOnFabsVisibility()
            }

            binding.floatingRightArrow -> {
                tripPosition++
                currentTrip = displayedTripsList[tripPosition]
                showTrip()
                showCadenceIfRelevant()
                drawMap(map!!)
                decideOnFabsVisibility()
            }
        }
    }

    private fun showTrip() {
        if (currentTrip != null) {
            binding.itemActivity.activityIcon.setImageDrawable(ActivityUtils.getIcon(this, currentTrip!!.activityType))
            binding.itemActivity.activityName.text = ActivityUtils.getName(this, currentTrip!!.activityType).replaceFirstChar { it.uppercase() }
            binding.itemActivity.activityTime.text = currentTrip!!.activityTime
        } else {
            Logger.e("Current trip is null on ${javaClass.name}")
            binding.itemActivity.activityIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_walking))
            binding.itemActivity.activityName.clear()
            binding.itemActivity.activityTime.clear()
        }
    }

    /**
     * This shows the cadence graph for relevant activities
     *
     */
    private fun showCadenceIfRelevant() {
        val container: LinearLayout = findViewById(R.id.graphContainer)
        if (currentTrip!!.activityType in listOf(
                DetectedActivity.ON_BICYCLE, DetectedActivity.WALKING,
                DetectedActivity.RUNNING, DetectedActivity.ON_FOOT
            )
            && currentTrip!!.steps > 150
            // display only for movements of at least 3 minutes
            && currentTrip!!.duration > 180000
        ) {
            CadenceGraphDisplay(findViewById(R.id.chart_view), currentTrip!!)
            container.visibility = View.VISIBLE
        } else container.visibility = View.INVISIBLE
    }

    private fun decideOnFabsVisibility() {
        if (displayedTripsList.size <= 1) {
            binding.floatingLeftArrow.isClickable = false
            binding.floatingLeftArrow.visibility = View.INVISIBLE
            binding.floatingRightArrow.isClickable = false
            binding.floatingRightArrow.visibility = View.INVISIBLE
        } else {
            when (tripPosition) {
                0 -> {
                    binding.floatingLeftArrow.isClickable = false
                    binding.floatingLeftArrow.visibility = View.INVISIBLE
                    binding.floatingRightArrow.isClickable = true
                    binding.floatingRightArrow.visibility = View.VISIBLE
                }
                displayedTripsList.size - 1 -> {
                    binding.floatingLeftArrow.isClickable = true
                    binding.floatingLeftArrow.visibility = View.VISIBLE
                    binding.floatingRightArrow.isClickable = false
                    binding.floatingRightArrow.visibility = View.INVISIBLE
                }
                else -> {
                    binding.floatingLeftArrow.isClickable = true
                    binding.floatingLeftArrow.visibility = View.VISIBLE
                    binding.floatingRightArrow.isClickable = true
                    binding.floatingRightArrow.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        showTrip()
        showCadenceIfRelevant()
    }


    /**
     * This callback is triggered when the map is ready to be used.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        drawMap(googleMap)
    }

    private fun drawMap(googleMap: GoogleMap) {
        map = googleMap
        map!!.uiSettings.isZoomControlsEnabled = true
        map!!.clear()
        System.gc()
        displayTrip(currentTrip)
    }

    /**
     * This displays the trip onto the map
     * @param trip
     */

    private fun displayTrip(trip: TripModel?) {
        if (trip == null) return
        val locations = trip.locations
        var currMarker: LatLng
        val width = binding.mapContainer.measuredWidth
        val height = binding.mapContainer.measuredHeight
        // set the offset from edges of the map 10% of screen
        val padding = (width * 0.10).toInt()
        // TODO clean spurious locations here
        val pattern = listOf(Dash(15f), Gap(10f))
        var prevMarker: LatLng? = null
        val color = Color.BLUE

        for (location in locations) {
            currMarker = LatLng(location.latitude, location.longitude)
            val circleOptions = CircleOptions()
                .center(currMarker)
                .strokeColor(color)
                .fillColor(color)
                // the radius is in meters
                .radius(2.0)
            map!!.addCircle(circleOptions)
            if (prevMarker != null) {
                map!!.addPolyline(
                    PolylineOptions()
                        .add(prevMarker, currMarker)
                        .width(5f)
                        .pattern(pattern)
                        .color(color)
                )
            }
            prevMarker = currMarker
        } //if <= 1 it will crash so add the same location twice - otherwise it will not focus
        when (locations.size) {
            1 -> locations.add(locations[0])
            0 -> locations.add(DBLocation(trip.chart[trip.startTime].timeInMSecs, 0.0, 0.0, 0.0, 0.0))
            else -> addStartEndTags(locations)

        }
        val builder = LatLngBounds.Builder()
        for (location in locations) {
            builder.include(LatLng(location.latitude, location.longitude))
        }
        val latLngBounds = builder.build()
        // padding of the map around the trajectory in pixels
        try {
            map!!.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, width, height, padding.toInt()))
            val zoom = map!!.cameraPosition.zoom
            if (zoom > 18.0f) map!!.animateCamera(CameraUpdateFactory.zoomTo(17.5f))
        } catch (e: Exception) {
            e.message?.let { Logger.i(it) }
        }
    }

    /**
     * This add a green and red start to movements trips
     *
     * @param locations
     */
    private fun addStartEndTags(locations: MutableList<DBLocation>) {
        if (currentTrip?.activityType != DetectedActivity.STILL) {
            map!!.addMarker(createMarker(locations[0], BitmapDescriptorFactory.HUE_GREEN))
            map!!.addMarker(createMarker(locations[locations.size - 1], BitmapDescriptorFactory.HUE_ORANGE))
        }
    }

    /**
     * used to create the start and end circles for a moving trajectory
     *
     * @param DBLocation
     * @param color
     * @return
     */
    private fun createMarker(location: DBLocation, strokeColor: Float): MarkerOptions {
        val currMarker = LatLng(location.latitude, location.longitude)
        return MarkerOptions()
            .position(currMarker)
            .icon(BitmapDescriptorFactory.defaultMarker(strokeColor))
    }
}