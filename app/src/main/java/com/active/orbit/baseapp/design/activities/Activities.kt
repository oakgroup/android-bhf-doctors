package com.active.orbit.baseapp.design.activities

import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.protocols.ActivityProvider

enum class Activities(private val activity: Class<out BaseActivity>) : ActivityProvider {
    ABOUT(AboutActivity::class.java),
    FAQ(FaqActivity::class.java),
    SPLASH(SplashActivity::class.java),
    MAIN(MainActivity::class.java);


    override fun getActivity(): Class<out BaseActivity> {
        return activity
    }
}