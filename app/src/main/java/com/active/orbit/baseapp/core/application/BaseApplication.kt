package com.active.orbit.baseapp.core.application

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.active.orbit.baseapp.core.preferences.engine.Preferences

@Suppress("unused")
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // set app url
        Preferences.backend(this).baseUrl = "https://pixabay.com" // DEMO_CODE!
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        MultiDex.install(this)
    }
}