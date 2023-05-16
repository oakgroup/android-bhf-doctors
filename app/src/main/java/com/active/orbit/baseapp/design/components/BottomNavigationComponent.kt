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

        binding.navSymptoms.setOnClickListener(this)
        binding.navHealth.setOnClickListener(this)
        binding.navMain.setOnClickListener(this)

        setSelected(BottomNavItemType.MAIN)
    }


    fun setSelected(navType: BottomNavItemType) {
        when(navType){
            BottomNavItemType.MAIN -> {
                binding.navSymptoms.setImage(R.drawable.bottom_nav_symptoms_idle)
                binding.navHealth.setImage(R.drawable.bottom_nav_health_idle)
                binding.navMain.setImage(R.drawable.bottom_nav_main_selected)
            }
            BottomNavItemType.SYMPTOMS -> {
                binding.navSymptoms.setImage(R.drawable.bottom_nav_symptoms_selected)
                binding.navHealth.setImage(R.drawable.bottom_nav_health_idle)
                binding.navMain.setImage(R.drawable.bottom_nav_main_idle)
            }
            BottomNavItemType.HEALTH -> {
                binding.navSymptoms.setImage(R.drawable.bottom_nav_symptoms_idle)
                binding.navHealth.setImage(R.drawable.bottom_nav_health_selected)
                binding.navMain.setImage(R.drawable.bottom_nav_main_idle)
            }
        }
    }


    override fun onClick(v: View?) {
        
        //TODO manage this
        when (v) {
            binding.navSymptoms -> {
                setSelected(BottomNavItemType.SYMPTOMS)
                Router.getInstance()
                    .clearTop(true)
                    .activityAnimation(ActivityAnimation.FADE)
                    .startBaseActivity(context, Activities.SYMPTOMS)
            }
            binding.navHealth -> {
                setSelected(BottomNavItemType.HEALTH)
                Router.getInstance()
                    .clearTop(true)
                    .activityAnimation(ActivityAnimation.FADE)
                    .startBaseActivity(context, Activities.HEALTH)
            }
            binding.navMain -> {
                setSelected(BottomNavItemType.MAIN)
                Router.getInstance()
                    .clearTop(true).homepage(context)
            }
        }
    }


}