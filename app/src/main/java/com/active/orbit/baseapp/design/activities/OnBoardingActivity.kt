package com.active.orbit.baseapp.design.activities

import android.os.Bundle
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.preferences.engine.Preferences
import com.active.orbit.baseapp.databinding.ActivityOnBoardingBinding
import com.active.orbit.baseapp.design.activities.engine.BaseActivity

class OnBoardingActivity : BaseActivity() {

    private lateinit var binding: ActivityOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepare()
    }

    private fun prepare() {

        //to show the appropriate message
        if (Preferences.lifecycle(this).onboardingshown) {
            binding.onboardingDescription.text = getString(R.string.onboarding_description)
        } else {
            binding.onboardingDescription.text = getString(R.string.onboarding_description_first_install, getString(R.string.app_name))
        }

        binding.onboardingButton.setOnClickListener {
            Preferences.lifecycle(this).onboardingshown = true
            manageOnBoarding()
            finish()
        }
    }

    override fun getToolbarResource(): Int? {
        return null
    }
}