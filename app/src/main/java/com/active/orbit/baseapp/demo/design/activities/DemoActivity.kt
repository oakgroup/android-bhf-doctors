package com.active.orbit.baseapp.demo.design.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.deserialization.UserRegistrationMap
import com.active.orbit.baseapp.core.listeners.UserRegistrationListener
import com.active.orbit.baseapp.core.managers.UserManager
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.serialization.UserRegistrationRequest
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.core.utils.TimeUtils
import com.active.orbit.baseapp.core.utils.Utils
import com.active.orbit.baseapp.databinding.ActivityDemoBinding
import com.active.orbit.baseapp.demo.design.recyclers.adapters.DemoRecyclerAdapter
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation
import com.active.orbit.baseapp.design.recyclers.engine.managers.BaseLinearLayoutManager

class DemoActivity : BaseActivity() {

    private lateinit var binding: ActivityDemoBinding
    private var adapter: DemoRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepare()
    }

    override fun onResume() {
        super.onResume()

        adapter?.refresh(this)
    }

    private fun prepare() {
        val manager = BaseLinearLayoutManager(this)
        binding.demoRecyclerView.layoutManager = manager

        adapter = DemoRecyclerAdapter(this)
        binding.demoRecyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add -> {
                Router.getInstance()
                    .activityAnimation(ActivityAnimation.LEFT_RIGHT)
                    .startBaseActivity(this, DemoActivities.DEMO_IMAGES)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun testUserRegistration() {
        val request = UserRegistrationRequest()
        request.phoneModel = Utils.getPhoneModel()
        request.appVersion = Utils.getAppVersion(this)
        request.androidVersion = Utils.getAndroidVersion()
        request.idProgram = "62f6402ccbdb315cd3fe32b7"
        request.idPatient = "0000000099"
        request.userSex = "Male"
        request.userAge = "33"
        request.userWeight = "70"
        request.userHeight = "175"
        request.batteryLevel = Utils.getBatteryPercentage(this)
        request.isCharging = Utils.isCharging(this)
        request.registrationTimestamp = TimeUtils.getCurrent().timeInMillis

        UserManager.registerUser(this, request, object : UserRegistrationListener {
            override fun onSuccess(map: UserRegistrationMap) {
                if (map.participantIdCounter > 1) {
                    Logger.d("Already existing user with patient id ${map.participantId}, ask for confirmation")
                } else {
                    Logger.d("User registered with id ${map.id}")
                }
            }

            override fun onError() {
                Logger.w("User registration error")
            }
        })
    }
}