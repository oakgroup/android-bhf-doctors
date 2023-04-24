package com.active.orbit.baseapp.design.activities.onboarding

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.enums.SuccessMessageType
import com.active.orbit.baseapp.core.preferences.engine.Preferences
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.routing.enums.Extra
import com.active.orbit.baseapp.databinding.ActivityPrivacyPolicyBinding
import com.active.orbit.baseapp.design.activities.engine.Activities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation
import com.active.orbit.baseapp.design.utils.UiUtils

@SuppressLint("CustomSplashScreen")
class PrivacyPolicyActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityPrivacyPolicyBinding
    private var fromHelp = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepare()

    }

    override fun onResume() {
        super.onResume()
        if (onboardedPrivacyPolicy()) proceed()
    }

    private fun prepare() {
        fromHelp = activityBundle.getBoolean(Extra.FROM_HELP.key, false)

        binding.privacyLink.setOnClickListener(this)
        binding.btnContinue.setOnClickListener(this)

        binding.privacyLink.paintFlags = binding.privacyLink.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        if (Preferences.lifecycle(this).isPrivacyPolicyAccepted) {
            binding.privacyCheckBox.isChecked = true
        }
    }

    private fun proceed() {
        if (!fromHelp) {
            val bundle = Bundle()
            bundle.putInt(Extra.SUCCESS_MESSAGE.key, SuccessMessageType.ON_BOARDING_COMPLETED.id)
            Router.getInstance().activityAnimation(ActivityAnimation.LEFT_RIGHT).startBaseActivity(this, Activities.SUCCESS_MESSAGE, bundle)
        }
        finish()
    }


    override fun getToolbarResource(): Int? {
        return null
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.privacyLink -> {
                Router.getInstance().openPrivacyPolicy(this)
            }

            binding.btnContinue -> {
                if (binding.privacyCheckBox.isChecked) {
                    Preferences.lifecycle(this).isPrivacyPolicyAccepted = true
                    proceed()
                } else {
                    UiUtils.showLongToast(this, R.string.accept_privacy_please)
                }
            }
        }
    }
}