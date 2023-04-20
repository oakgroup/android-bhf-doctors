package com.active.orbit.baseapp.design.activities

import android.os.Bundle
import android.view.View
import com.active.orbit.baseapp.core.listeners.ResultListener
import com.active.orbit.baseapp.core.preferences.engine.Preferences
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.databinding.ActivityWelcomeBinding
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation

class WelcomeActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Preferences.lifecycle(this).welcomeShown = true
    }

    override fun onResume() {
        super.onResume()
        prepare()
    }

    private fun prepare() {
        binding.welcomeButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            binding.welcomeButton -> {
                onboarded(object : ResultListener {
                    override fun onResult(success: Boolean) {
                        if (!success) {
                            Router.getInstance()
                                .activityAnimation(ActivityAnimation.FADE)
                                .startBaseActivity(this@WelcomeActivity, Activities.ON_BOARDING)
                        } else {
                            Router.getInstance()
                                .activityAnimation(ActivityAnimation.FADE)
                                .homepage(this@WelcomeActivity)
                        }
                        finish()
                    }
                })
            }
        }
    }

    override fun getToolbarResource(): Int? {
        return null
    }
}