package com.active.orbit.baseapp.design.activities.onboarding

import android.os.Bundle
import android.view.View
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.listeners.ResultListener
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.routing.enums.Extra
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.databinding.ActivityOnBoardingUnusedRestrictionsBinding
import com.active.orbit.baseapp.design.activities.engine.Activities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation

class OnBoardingUnusedRestrictionsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityOnBoardingUnusedRestrictionsBinding
    private var fromHelp = false

    private var userConsentName = Constants.EMPTY
    private var userConsentDate = Constants.INVALID.toLong()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingUnusedRestrictionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userConsentName = activityBundle.getString(Extra.USER_CONSENT_NAME.key)!!
        userConsentDate = activityBundle.getLong(Extra.USER_CONSENT_DATE.key)

        prepare()
    }

    override fun onResume() {
        super.onResume()
        onboardedUnusedRestrictions(object : ResultListener {
            override fun onResult(success: Boolean) {
                if (!success) showPermissionButton(R.string.disable_restrictions)
                else proceed()
            }
        })
    }

    private fun prepare() {
        fromHelp = activityBundle.getBoolean(Extra.FROM_HELP.key, false)
        binding.btnPermission.setOnClickListener(this)
    }

    private fun proceed() {
        if (!fromHelp) {
            val bundle = Bundle()
            bundle.putString(Extra.USER_CONSENT_NAME.key, userConsentName)
            bundle.putLong(Extra.USER_CONSENT_DATE.key, userConsentDate)
            Router.getInstance()
                .activityAnimation(ActivityAnimation.LEFT_RIGHT)
                .startBaseActivity(this, Activities.PATIENT_DETAILS, bundle)
        }
        finish()
    }

    private fun showPermissionButton(resId: Int) {
        binding.btnPermission.text = getString(resId)
    }

    override fun onClick(v: View?) {

        when (v) {
            binding.btnPermission -> {
                onboardedUnusedRestrictions(object : ResultListener {
                    override fun onResult(success: Boolean) {
                        if (!success) requestUnusedRestrictions()
                        else proceed()
                    }
                })
            }
        }
    }

    override fun getToolbarResource(): Int? {
        return null
    }
}