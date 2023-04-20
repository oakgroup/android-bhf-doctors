package com.active.orbit.baseapp.design.activities

import android.os.Bundle
import com.active.orbit.baseapp.BuildConfig
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.listeners.ResultListener
import com.active.orbit.baseapp.core.preferences.engine.BasePreferences
import com.active.orbit.baseapp.core.preferences.engine.Preferences
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.core.utils.TimeUtils
import com.active.orbit.baseapp.core.utils.Utils
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (Preferences.lifecycle(this).firstInstall == Constants.INVALID.toLong()) {
            // remember first installation date
            Preferences.lifecycle(this).firstInstall = TimeUtils.getCurrent().timeInMillis
        }

        if (BuildConfig.DEBUG) printInformationLogs()
    }

    override fun onResume() {
        super.onResume()

        Utils.delay(TimeUtils.ONE_SECOND_MILLIS.toLong() * 2) {
            Router.getInstance()
                .activityAnimation(ActivityAnimation.FADE)
                .homepage(this@SplashActivity)

            //TODO use this after all submodules are added
//            if (Preferences.lifecycle(this).welcomeShown) {
//                onboarded(object : ResultListener {
//                    override fun onResult(success: Boolean) {
//                        if (!success) {
//                            Router.getInstance()
//                                .activityAnimation(ActivityAnimation.FADE)
//                                .startBaseActivity(this@SplashActivity, Activities.ON_BOARDING)
//                        } else {
//                            Router.getInstance()
//                                .activityAnimation(ActivityAnimation.FADE)
//                                .homepage(this@SplashActivity)
//                        }
//                    }
//                })
//            } else {
//                Router.getInstance()
//                    .activityAnimation(ActivityAnimation.FADE)
//                    .startBaseActivity(this, Activities.WELCOME)
//            }
            finish()
        }
    }

    override fun getToolbarResource(): Int? {
        return null
    }

    private fun printInformationLogs() {
        val appName = getString(R.string.app_name)
        val appPackageName = Utils.getPackageName(this)
        val appVersionName = Utils.getVersionName(this)
        val appVersionCode = Utils.getVersionCode(this)

        Logger.i("------------------------------------------")
        Logger.i("App name             -> $appName")
        Logger.i("App bundle id        -> $appPackageName")
        Logger.i("App version name     -> $appVersionName")
        Logger.i("App version code     -> $appVersionCode")
        Logger.i("------------------------------------------")

        BasePreferences.printAll(this)
    }
}