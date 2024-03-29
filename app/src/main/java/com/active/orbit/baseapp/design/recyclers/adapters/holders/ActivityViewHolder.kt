package com.active.orbit.baseapp.design.recyclers.adapters.holders

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.design.activities.engine.Activities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.main.PatientActivity
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerCell
import com.active.orbit.baseapp.design.recyclers.models.TripModel
import com.active.orbit.baseapp.design.utils.ActivityUtils
import com.active.orbit.baseapp.design.widgets.BaseImageView
import com.active.orbit.baseapp.design.widgets.BaseTextView
import com.google.android.gms.location.DetectedActivity

class ActivityViewHolder(var activity: BaseActivity, itemView: View, private var tripModels: ArrayList<TripModel>) : BaseRecyclerCell<TripModel>(itemView) {

    private var activityPanel: ViewGroup
    private var activityIcon: BaseImageView
    private var activityName: BaseTextView
    private var activityTime: BaseTextView
    private var activityDetails: BaseTextView
    private var rightIconFrame: FrameLayout
    private var connectorOne: View
    private var connectorTwo: View

    init {
        activityPanel = itemView.findViewById(R.id.panel)
        activityIcon = itemView.findViewById(R.id.activityIcon)
        activityName = itemView.findViewById(R.id.activityName)
        activityTime = itemView.findViewById(R.id.activityTime)
        activityDetails = itemView.findViewById(R.id.activityDetails)
        rightIconFrame = itemView.findViewById(R.id.rightIconFrame)
        connectorOne = itemView.findViewById(R.id.connectorOne)
        connectorTwo = itemView.findViewById(R.id.connectorTwo)
    }

    @SuppressLint("SetTextI18n")
    override fun bind(model: TripModel) {

        rightIconFrame.isClickable = false

        activityIcon.setImageDrawable(ActivityUtils.getIcon(activity, model.activityType))
        activityName.text = ActivityUtils.getName(activity, model.activityType).replaceFirstChar { it.uppercase() }
        activityTime.text = model.activityTime

        if (model.activityType in listOf(DetectedActivity.WALKING, DetectedActivity.RUNNING, DetectedActivity.ON_FOOT)) {
            activityDetails.visibility = View.VISIBLE
            activityDetails.text = activity.getString(R.string.activity_details, model.steps.toString(), model.speedInMetersPerSeconds.toString())
        } else {
            activityDetails.visibility = View.GONE
        }

        if (model.position == tripModels.size - 1) {
            connectorOne.visibility = View.GONE
            connectorTwo.visibility = View.GONE
        } else {
            connectorOne.visibility = View.VISIBLE
            connectorTwo.visibility = View.VISIBLE
        }

        activityPanel.isClickable = false
        activityPanel.setOnTouchListener { v, event ->
            itemView.performClick()
            if (event != null) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        when (v) {
                            itemView -> {
                                val rightIconFrame = itemView.findViewById<FrameLayout>(R.id.rightIconFrame)
                                rightIconFrame.isPressed = true
                            }
                        }
                    }
                }
            }
            false
        }
        activityPanel.setOnClickListener {
            onActivityClicked(model)
        }
    }

    private fun onActivityClicked(model: TripModel) {
        PatientActivity.currentTrip = model
        PatientActivity.currentTripPosition = model.position
        PatientActivity.displayedTripsList = tripModels
        Router.getInstance().startBaseActivity(activity, Activities.MAP)
    }
}