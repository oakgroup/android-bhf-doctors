package com.active.orbit.baseapp.design.activities.onboarding

import android.os.Bundle
import android.view.View
import com.active.orbit.baseapp.core.preferences.engine.Preferences
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.databinding.ActivityWelcomeBinding
import com.active.orbit.baseapp.design.activities.engine.Activities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation

class WelcomeActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        prepare()
    }

    private fun prepare() {
        binding.welcomeButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.welcomeButton -> {
                if (Preferences.lifecycle(this).tourShown) {
                    Router.getInstance()
                        .activityAnimation(ActivityAnimation.BOTTOM_TOP)
                        .startBaseActivity(this@WelcomeActivity, Activities.CONSENT_PRIVACY)
                } else {
                    Router.getInstance().activityAnimation(ActivityAnimation.FADE).startBaseActivity(this, Activities.TOUR)
                }
                finish()
            }
        }
    }

    override fun getToolbarResource(): Int? {
        return null
    }
}