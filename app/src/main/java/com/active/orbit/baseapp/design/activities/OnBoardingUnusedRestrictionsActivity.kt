package com.active.orbit.baseapp.design.activities

import android.os.Bundle
import android.view.View
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.listeners.ResultListener
import com.active.orbit.baseapp.core.routing.enums.Extra
import com.active.orbit.baseapp.databinding.ActivityOnBoardingUnusedRestrictionsBinding
import com.active.orbit.baseapp.design.activities.engine.BaseActivity

class OnBoardingUnusedRestrictionsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityOnBoardingUnusedRestrictionsBinding
    private var fromHelp = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingUnusedRestrictionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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