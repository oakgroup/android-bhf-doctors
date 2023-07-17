package com.active.orbit.baseapp.design.recyclers.models

import com.active.orbit.baseapp.core.generics.BaseModel
import com.active.orbit.baseapp.core.utils.Constants
import uk.ac.shef.tracker.core.computation.data.MobilityData
import uk.ac.shef.tracker.core.database.models.TrackerDBLocation
import uk.ac.shef.tracker.core.database.models.TrackerDBTrip
import uk.ac.shef.tracker.core.utils.TimeUtils

class TripModel : BaseModel {

    var id = Constants.INVALID
    var startTime = Constants.INVALID
    var endTime = Constants.INVALID
    var activityType = Constants.INVALID
    var chart: MutableList<MobilityData> = mutableListOf()
    var radiusInMeters: Int = 0
    var steps: Int = 0
    var reliable: Boolean = true
    var distanceInMeters: Int = 0
    var speedInMetersPerSeconds: Long = 0
    var locations: MutableList<TrackerDBLocation> = mutableListOf()
    var subTrips: MutableList<TrackerDBTrip> = mutableListOf()
    var duration: Long = 0
    var uploaded: Boolean = false

    var activityTime: String?
    var position = Constants.INVALID

    @Suppress("ConvertSecondaryConstructorToPrimary")
    constructor(dbTrip: TrackerDBTrip) {
        this.id = dbTrip.idTrip
        this.startTime = dbTrip.startTime
        this.endTime = dbTrip.endTime
        this.activityType = dbTrip.activityType
        this.chart = dbTrip.chart
        this.radiusInMeters = dbTrip.radiusInMeters
        this.steps = dbTrip.steps
        this.reliable = dbTrip.reliable
        this.distanceInMeters = dbTrip.distanceInMeters
        this.speedInMetersPerSeconds = dbTrip.getSpeedInMPerSecs()
        this.locations = dbTrip.locations
        this.subTrips = dbTrip.subTrips
        this.duration = dbTrip.getDuration(chart)
        this.uploaded = dbTrip.uploaded

        this.activityTime = TimeUtils.formatMillis(dbTrip.getStartTime(chart), Constants.DATE_FORMAT_HOUR_MINUTE) + " - " + TimeUtils.formatMillis(dbTrip.getEndTime(chart), "HH:mm")
    }

    override fun identifier(): String {
        return startTime.toString() + endTime.toString()
    }

    override fun isValid(): Boolean {
        return id != Constants.INVALID
    }
}