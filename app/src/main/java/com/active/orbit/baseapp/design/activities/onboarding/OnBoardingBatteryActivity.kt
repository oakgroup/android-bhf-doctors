package com.active.orbit.baseapp.design.activities.onboarding

import android.os.Bundle
import android.view.View
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.routing.enums.Extra
import com.active.orbit.baseapp.databinding.ActivityOnBoardingBatteryBinding
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.aeqora.corepowersettings.BaseApplicationUtils
import com.aeqora.corepowersettings.utils.AppType

class OnBoardingBatteryActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityOnBoardingBatteryBinding
    private var fromMenu = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBatteryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepare()
    }

    override fun onResume() {
        super.onResume()
        if (!onboardedBattery() || fromMenu) showPermissionButton(R.string.battery_open_settings)
        else proceed()
    }

    private fun prepare() {
        fromMenu = activityBundle.getBoolean(Extra.FROM_MENU.key, false)
        binding.btnPermission.setOnClickListener(this)

    }

    private fun proceed() {
        if (!fromMenu) {
            manageOnBoarding()
        }
        finish()
    }

    private fun showPermissionButton(resId: Int) {
        binding.btnPermission.text = getString(resId)
    }

    override fun onClick(v: View?) {

        when (v) {
            binding.btnPermission -> {
                if (!onboardedBattery() || fromMenu) {
                    val baseActivityUtils = BaseApplicationUtils()
                    baseActivityUtils.requestBatteryPermission2(this, true, AppType.BHF)
                } else proceed()
            }
        }
    }

    override fun getToolbarResource(): Int? {
        return null
    }


}