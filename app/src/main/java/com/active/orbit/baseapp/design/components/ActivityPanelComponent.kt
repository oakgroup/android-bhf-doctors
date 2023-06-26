package com.active.orbit.baseapp.design.components

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.databinding.ComponentActivityPanelBinding

class ActivityPanelComponent : FrameLayout {

    private lateinit var binding: ComponentActivityPanelBinding

    constructor(context: Context) : super(context) {
        prepare()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        prepare()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        prepare()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun prepare() {
        binding = ComponentActivityPanelBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)

        binding.walkingActivityProgress.setBackgroundLineColor(ContextCompat.getColor(context, R.color.colorPrimaryLight))
        binding.walkingActivityProgress.setProgressLineColor(ContextCompat.getColor(context, R.color.colorAccent))
        binding.walkingActivityProgress.setProgressIconResource(R.drawable.ic_activity)
        binding.walkingActivityProgress.setLineWidth(40f)
        binding.walkingActivityProgress.setMaxProgress(200.toFloat())

        binding.heartActivityProgress.setBackgroundLineColorOne(ContextCompat.getColor(context, R.color.colorPrimaryLight))
        binding.heartActivityProgress.setBackgroundLineColorTwo(ContextCompat.getColor(context, R.color.colorPrimaryLight))
        binding.heartActivityProgress.setProgressLineColorOne(ContextCompat.getColor(context, R.color.colorAccent))
        binding.heartActivityProgress.setProgressLineColorTwo(ContextCompat.getColor(context, R.color.colorAccent))
        binding.heartActivityProgress.setProgressIconResource(R.drawable.ic_activity)
        binding.heartActivityProgress.setLineWidth(40f)
        binding.heartActivityProgress.setMaxProgress(Constants.NHS_WEEK_HEART_TARGET.toFloat())

    }


    fun setProgress(minutesWalking: Long, minutesHeart: Long, minutesCycling: Long, distanceWalking: Int, distanceHeart: Int, distanceCycling: Int, steps: Int) {

        binding.heartActivityProgress.setProgress(minutesHeart.toFloat())
        binding.walkingActivityProgress.setProgress(minutesWalking.toFloat())

        if (minutesWalking == 1L) binding.walkingProgressText.text = context.getString(R.string.activity_distance_active_minute)
        else binding.walkingProgressText.text = context.getString(R.string.activity_distance_active_minutes, minutesWalking.toString(), distanceWalking.toString())

        if (minutesHeart == 1L) binding.heartProgressText.text = context.getString(R.string.activity_distance_heart_minute)
        else binding.heartProgressText.text = context.getString(R.string.activity_distance_heart_minutes, minutesHeart.toString(), distanceHeart.toString())

        if (minutesCycling == 1L) binding.bicycleProgressText.text = context.getString(R.string.activity_distance_cycling_minute)
        else binding.bicycleProgressText.text = context.getString(R.string.activity_distance_cycling_minutes, minutesCycling.toString(), distanceCycling.toString())

        if (steps == 1) binding.stepsProgressText.text = context.getString(R.string.activity_step)
        else binding.stepsProgressText.text = context.getString(R.string.activity_steps, steps.toString())

    }

}