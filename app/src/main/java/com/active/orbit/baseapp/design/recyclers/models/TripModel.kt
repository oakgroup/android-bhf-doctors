package com.active.orbit.baseapp.design.recyclers.models

import com.active.orbit.baseapp.core.generics.BaseProtocol
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.tracker.retrieval.data.MobilityElementData
import com.active.orbit.tracker.retrieval.data.TripData
import com.active.orbit.tracker.tracker.sensors.location_recognition.LocationData
import com.active.orbit.tracker.utils.Utils

class TripModel : BaseProtocol {

    var id = Constants.INVALID
    var startTime = Constants.INVALID
    var endTime = Constants.INVALID
    var activityType = Constants.INVALID
    var chart: MutableList<MobilityElementData> = mutableListOf()
    var radiusInMeters: Int = 0
    var steps: Int = 0
    var reliable: Boolean = true
    var distanceInMeters: Int = 0
    var locations: MutableList<LocationData> = mutableListOf()
    var subTrips: MutableList<TripData> = mutableListOf()
    var duration: Long = 0
    var uploaded: Boolean = false

    var activityTime: String?
    var position = Constants.INVALID

    @Suppress("ConvertSecondaryConstructorToPrimary")
    constructor(tripData: TripData) {
        this.id = tripData.id
        this.startTime = tripData.startTime
        this.endTime = tripData.endTime
        this.activityType = tripData.activityType
        this.chart = tripData.chart
        this.radiusInMeters = tripData.radiusInMeters
        this.steps = tripData.steps
        this.reliable = tripData.reliable
        this.distanceInMeters = tripData.distanceInMeters
        this.locations = tripData.locations
        this.subTrips = tripData.subTrips
        this.duration = tripData.getDuration(chart)
        this.uploaded = tripData.uploaded

        this.activityTime = Utils.millisecondsToString(tripData.getStartTime(chart), "HH:mm") + " - " + Utils.millisecondsToString(tripData.getEndTime(chart), "HH:mm")
    }

    override fun identifier(): String {
        return startTime.toString() + endTime.toString()
    }

    override fun isValid(): Boolean {
        return id != Constants.INVALID
    }
}