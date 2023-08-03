package com.active.orbit.baseapp.design.activities.onboarding

import android.os.Bundle
import android.view.View
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.routing.enums.Extra
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.databinding.ActivityOnBoardingBatteryBinding
import com.active.orbit.baseapp.design.activities.engine.Activities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation
import com.aeqora.corepowersettings.BaseApplicationUtils
import com.aeqora.corepowersettings.utils.AppType

class OnBoardingBatteryActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityOnBoardingBatteryBinding
    private var fromMenu = false

    private var userConsentName = Constants.EMPTY
    private var userConsentDate = Constants.INVALID.toLong()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBatteryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userConsentName = activityBundle.getString(Extra.USER_CONSENT_NAME.key)!!
        userConsentDate = activityBundle.getLong(Extra.USER_CONSENT_DATE.key)

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

        if (fromMenu) {
            showBackButton()
        } else {
            hideToolbar()
        }
    }

    private fun proceed() {
        if (!fromMenu) {
            val bundle = Bundle()
            bundle.putString(Extra.USER_CONSENT_NAME.key, userConsentName)
            bundle.putLong(Extra.USER_CONSENT_DATE.key, userConsentDate)
            Router.getInstance()
                .activityAnimation(ActivityAnimation.LEFT_RIGHT)
                .startBaseActivity(this, Activities.ON_BOARDING_UNUSED_RESTRICTIONS, bundle)
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
                    baseActivityUtils.requestBatteryPermission2(this, true, AppType.MOVING_HEALTH)
                } else proceed()
            }
        }
    }
}