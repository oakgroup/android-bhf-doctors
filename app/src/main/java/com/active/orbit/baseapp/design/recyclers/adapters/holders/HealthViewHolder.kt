package com.active.orbit.baseapp.design.recyclers.adapters.holders

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.database.models.DBHealth
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.routing.enums.Extra
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.core.utils.TimeUtils
import com.active.orbit.baseapp.design.activities.engine.Activities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation
import com.active.orbit.baseapp.design.recyclers.engine.BaseRecyclerCell
import com.active.orbit.baseapp.design.utils.UiUtils
import com.active.orbit.baseapp.design.widgets.BaseTextView
import com.active.orbit.baseapp.design.widgets.HorizontalProgressBar

class HealthViewHolder(var activity: BaseActivity, itemView: View) : BaseRecyclerCell<DBHealth>(itemView), View.OnTouchListener {

    private var healthScore: BaseTextView
    private var healthScoreProgress: HorizontalProgressBar
    private var healthTimestamp: BaseTextView
    private var rightIconFrame: FrameLayout

    init {
        healthScore = itemView.findViewById(R.id.healthScore)
        healthScoreProgress = itemView.findViewById(R.id.healthScoreProgress)
        healthTimestamp = itemView.findViewById(R.id.timestamp)
        rightIconFrame = itemView.findViewById(R.id.rightIconFrame)
    }

    override fun bind(model: DBHealth) {
        healthScore.text = activity.getString(R.string.health_score_label, model.healthScore.toString())
        healthTimestamp.text = TimeUtils.format(TimeUtils.getCurrent(model.healthID), Constants.DATE_FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)

        itemView.setOnClickListener {
            onItemClicked(model)
        }

        itemView.setOnTouchListener(this)
        rightIconFrame.isClickable = false


        healthScoreProgress.setBackgroundLineColorOne(ContextCompat.getColor(activity, R.color.colorSecondaryLight))
        healthScoreProgress.setBackgroundLineColorTwo(ContextCompat.getColor(activity, R.color.colorSecondaryLight))
        healthScoreProgress.setProgressLineColorOne(ContextCompat.getColor(activity, R.color.colorPrimary))
        healthScoreProgress.setProgressLineColorTwo(ContextCompat.getColor(activity, R.color.colorSecondary))
        healthScoreProgress.setLineWidth(20f)
        healthScoreProgress.hideProgressIcon()
        healthScoreProgress.setMaxProgress(Constants.HEALTH_MAX_PROGRESS.toFloat())
        healthScoreProgress.setProgress(model.healthScore!!.toFloat())

    }

    private fun onItemClicked(model: DBHealth) {
        val bundle = Bundle()
        bundle.putString(Extra.IDENTIFIER.key, model.healthID.toString())
        Router.getInstance()
            .activityAnimation(ActivityAnimation.LEFT_RIGHT)
            .startBaseActivity(activity, Activities.HEALTH_RESPONSE, bundle)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event != null) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    when (v) {
                        itemView -> {
                            rightIconFrame.isPressed = true
                        }

                    }
                }
            }
        }
        return false
    }
}