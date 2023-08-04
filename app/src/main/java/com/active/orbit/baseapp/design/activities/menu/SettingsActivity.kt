package com.active.orbit.baseapp.design.activities.menu

import android.os.Bundle
import android.view.View
import com.active.orbit.baseapp.core.preferences.engine.Preferences
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.routing.enums.Extra
import com.active.orbit.baseapp.databinding.ActivitySettingsBinding
import com.active.orbit.baseapp.design.activities.engine.Activities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation

class SettingsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showBackButton()

        prepare()
    }

    fun prepare() {

        binding.locationServices.disableClick()
        binding.patientDetails.disableClick()
        binding.batterySettings.disableClick()
        binding.unusedPermissions.disableClick()


        binding.locationServices.setOnClickListener(this)
        binding.patientDetails.setOnClickListener(this)
        binding.batterySettings.setOnClickListener(this)
        binding.unusedPermissions.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v) {
            binding.locationServices -> {
                val bundle = Bundle()
                bundle.putBoolean(Extra.FROM_MENU.key, true)
                Router.getInstance().activityAnimation(ActivityAnimation.LEFT_RIGHT).startBaseActivity(this, Activities.ON_BOARDING_LOCATION, bundle)
            }

            binding.patientDetails -> {
                val bundle = Bundle()
                bundle.putBoolean(Extra.FROM_MENU.key, true)
                Router.getInstance().activityAnimation(ActivityAnimation.LEFT_RIGHT).startBaseActivity(this, Activities.PATIENT_DETAILS, bundle)
            }

            binding.batterySettings -> {
                val bundle = Bundle()
                bundle.putBoolean(Extra.FROM_MENU.key, true)
                Router.getInstance().activityAnimation(ActivityAnimation.LEFT_RIGHT).startBaseActivity(this, Activities.ON_BOARDING_BATTERY, bundle)
            }

            binding.unusedPermissions -> {
                val bundle = Bundle()
                bundle.putBoolean(Extra.FROM_MENU.key, true)
                Router.getInstance().activityAnimation(ActivityAnimation.LEFT_RIGHT).startBaseActivity(this, Activities.ON_BOARDING_UNUSED_RESTRICTIONS, bundle)
            }
        }
    }


}