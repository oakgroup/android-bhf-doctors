package com.active.orbit.baseapp.design.activities

import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.protocols.ActivityProvider

enum class Activities(private val activity: Class<out BaseActivity>) : ActivityProvider {
    ABOUT(AboutActivity::class.java),
    DEBUG(DebugActivity::class.java),
    DETAILED_ACTIVITY(DetailedActivityActivity::class.java),
    FAQ(FaqActivity::class.java),
    MAIN(MainActivity::class.java),
    ON_BOARDING(OnBoardingActivity::class.java),
    ON_BOARDING_BATTERY(OnBoardingBatteryActivity::class.java),
    ON_BOARDING_LOCATION(OnBoardingLocationActivity::class.java),
    ON_BOARDING_UNUSED_RESTRICTIONS(OnBoardingUnusedRestrictionsActivity::class.java),
    PRIVACY_POLICY(PrivacyPolicyActivity::class.java),
    SPLASH(SplashActivity::class.java),
    SUCCESS_MESSAGE(SuccessMessageActivity::class.java),
    WELCOME(WelcomeActivity::class.java);

    override fun getActivity(): Class<out BaseActivity> {
        return activity
    }
}