package com.active.orbit.baseapp.demo.design.activities

import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.protocols.ActivityProvider

enum class DemoActivities(private val activity: Class<out BaseActivity>) : ActivityProvider {
    DEMO(DemoActivity::class.java),
    DEMO_IMAGES(DemoImagesActivity::class.java),
    DEMO_VOTE(DemoVoteActivity::class.java);

    override fun getActivity(): Class<out BaseActivity> {
        return activity
    }
}