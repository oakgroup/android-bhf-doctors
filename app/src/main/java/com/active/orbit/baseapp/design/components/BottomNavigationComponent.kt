package com.active.orbit.baseapp.design.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.enums.BottomNavItemType
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.databinding.ComponentBottomNavigationBinding
import com.active.orbit.baseapp.design.activities.engine.Activities
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation

class BottomNavigationComponent : FrameLayout, View.OnClickListener {

    private var selectedType = BottomNavItemType.MAIN

    internal lateinit var binding: ComponentBottomNavigationBinding

    constructor(context: Context) : super(context) {
        prepare(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        prepare(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        prepare(attrs)
    }

    private fun prepare(attrs: AttributeSet?) {
        binding = ComponentBottomNavigationBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)

        binding.navTrips.setOnClickListener(this)
        binding.navHealth.setOnClickListener(this)
        binding.navMain.setOnClickListener(this)
    }

    fun setSelected(navType: BottomNavItemType) {
        selectedType = navType
        when (navType) {
            BottomNavItemType.MAIN -> {
                binding.navTrips.setImage(R.drawable.bottom_nav_trips_idle)
                binding.navHealth.setImage(R.drawable.bottom_nav_health_idle)
                binding.navMain.setImage(R.drawable.bottom_nav_main_selected)
            }

            BottomNavItemType.TRIPS -> {
                binding.navTrips.setImage(R.drawable.bottom_nav_trips_selected)
                binding.navHealth.setImage(R.drawable.bottom_nav_health_idle)
                binding.navMain.setImage(R.drawable.bottom_nav_main_idle)
            }

            BottomNavItemType.HEALTH -> {
                binding.navTrips.setImage(R.drawable.bottom_nav_trips_idle)
                binding.navHealth.setImage(R.drawable.bottom_nav_health_selected)
                binding.navMain.setImage(R.drawable.bottom_nav_main_idle)
            }
        }
    }


    override fun onClick(v: View?) {

        when (v) {
            binding.navTrips -> {
                if (selectedType != BottomNavItemType.TRIPS) {
                    Router.getInstance()
                        .clearTop(true)
                        .activityAnimation(ActivityAnimation.FADE)
                        .startBaseActivity(context, Activities.TRIPS)
                }
            }

            binding.navHealth -> {
                if (selectedType != BottomNavItemType.HEALTH) {
                    Router.getInstance()
                        // .clearTop(true)
                        .activityAnimation(ActivityAnimation.FADE)
                        .startBaseActivity(context, Activities.HEALTH)
                }
            }

            binding.navMain -> {
                if (selectedType != BottomNavItemType.MAIN) {
                    Router.getInstance()
                        .clearTop(true).homepage(context)
                }
            }
        }
    }
}