package com.active.orbit.baseapp.design.components

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.databinding.ComponentMobilityPanellBinding

class MobilityPanelComponent : FrameLayout {

    private lateinit var binding: ComponentMobilityPanellBinding

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
        binding = ComponentMobilityPanellBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)


    }


    fun setProgress(vehicleTrips: Int, vehicleDistance: Int, vehicleMinutes: Long) {

        binding.motorVehicleTrips.text = vehicleTrips.toString()
        binding.motorVehicleDistance.text = vehicleDistance.toString()
        binding.motorVehicleMinutes.text = vehicleMinutes.toString()

        if (vehicleMinutes == 1L) binding.motorVehicleMinutesLabel.text = context.getString(R.string.minute)
        else binding.motorVehicleMinutesLabel.text = context.getString(R.string.minutes)


    }

}