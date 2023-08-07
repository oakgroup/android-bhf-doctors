package com.active.orbit.baseapp.design.activities.questionnaire

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.enums.BottomNavItemType
import com.active.orbit.baseapp.core.managers.HealthManager
import com.active.orbit.baseapp.core.notifications.NotificationType
import com.active.orbit.baseapp.core.notifications.NotificationsManager
import com.active.orbit.baseapp.core.permissions.Permissions
import com.active.orbit.baseapp.core.preferences.engine.Preferences
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.databinding.ActivityHealthBinding
import com.active.orbit.baseapp.design.activities.engine.Activities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation
import com.active.orbit.baseapp.design.recyclers.adapters.HealthAdapter
import com.active.orbit.baseapp.design.recyclers.listeners.RefreshListener
import com.active.orbit.baseapp.design.utils.UiUtils
import uk.ac.shef.tracker.core.utils.background

class HealthActivity : BaseActivity(), View.OnClickListener {

    companion object {
        const val HEALTH_REQUEST_CODE = 312
        private const val HEALTH_RESPONSES_MAX = 20
    }

    private lateinit var binding: ActivityHealthBinding
    private var adapter: HealthAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHealthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showMenuComponent()
        setToolbarTitle(getString(R.string.app_name))
        binding.bottomNav.setSelected(BottomNavItemType.HEALTH)

        prepare()



        //TODO Omar to check this code and test on emulator
        if (!Preferences.lifecycle(this).notificationPermissionRequested) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (!hasNotificationPermissionGranted()) {
                    Preferences.lifecycle(this).notificationPermissionRequested = true
                    requestPermissionNotifications()
                }
            } else {
                scheduleNotification()
            }
        } else {
            scheduleNotification()
        }

        HealthManager.checkForNotUploaded(this)
    }

    override fun onResume() {
        super.onResume()


        showResponses()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onPermissionEnabled(requestCode: Int) {
        super.onPermissionEnabled(requestCode)
        when (requestCode) {
            Permissions.Group.ACCESS_NOTIFICATIONS.requestCode -> {
                scheduleNotification()
            }
            else -> {
                Logger.e("Undefined request code $requestCode on permission enabled ")
            }
        }
    }

    private fun prepare() {

        binding.btnFillQuestionnaire.setOnClickListener(this)

        val linearLayoutManager = LinearLayoutManager(this)
        binding.healthRecyclerView.layoutManager = linearLayoutManager

        adapter = HealthAdapter(this)
        binding.healthRecyclerView.adapter = adapter

        binding.noHealth.text = getString(R.string.health_empty_patient)
        binding.btnFillQuestionnaire.visibility = View.VISIBLE
    }

    private fun showResponses() {
        adapter?.refresh(this, object : RefreshListener {
            override fun onRefreshed(itemCount: Int) {
                if (itemCount == 0) {
                    binding.noHealth.visibility = View.VISIBLE
                } else {
                    binding.noHealth.visibility = View.GONE
                }
            }
        })
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnFillQuestionnaire -> {
                if ((adapter?.itemCount ?: 0) < HEALTH_RESPONSES_MAX) {
                    Router.getInstance().activityAnimation(ActivityAnimation.LEFT_RIGHT).startBaseActivityForResult(this, Activities.HEALTH_MOBILITY, Bundle(), HEALTH_REQUEST_CODE)
                } else {
                    UiUtils.showLongToast(this, getString(R.string.full_health_list_message))
                }
            }
        }
    }

    private fun scheduleNotification() {
        //schedule notification only if it has not been scheduled before
        background {
            if (Preferences.lifecycle(this@HealthActivity).notificationScheduled == Constants.INVALID) {
                val notificationToSchedule = NotificationType.HEALTH
                Preferences.lifecycle(this@HealthActivity).notificationScheduled = notificationToSchedule.id
                NotificationsManager.scheduleNotification(this@HealthActivity, (com.active.orbit.baseapp.core.utils.TimeUtils.ONE_DAY_MILLIS * 30), notificationToSchedule)
            }
        }
    }
}