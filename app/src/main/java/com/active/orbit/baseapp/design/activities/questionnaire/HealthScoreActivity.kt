package com.active.orbit.baseapp.design.activities.questionnaire

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.database.models.DBHealth
import com.active.orbit.baseapp.core.database.tables.TableHealth
import com.active.orbit.baseapp.core.enums.HealthType
import com.active.orbit.baseapp.core.listeners.ResultListener
import com.active.orbit.baseapp.core.managers.HealthManager
import com.active.orbit.baseapp.core.notifications.NotificationType
import com.active.orbit.baseapp.core.notifications.NotificationsManager
import com.active.orbit.baseapp.core.preferences.engine.Preferences
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.routing.enums.Extra
import com.active.orbit.baseapp.core.routing.enums.ResultCode
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.core.utils.TimeUtils
import com.active.orbit.baseapp.databinding.ActivityHealthScoreBinding
import com.active.orbit.baseapp.design.activities.engine.Activities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.utils.UiUtils
import uk.ac.shef.tracker.core.utils.background
import uk.ac.shef.tracker.core.utils.main

class HealthScoreActivity : BaseActivity(), View.OnClickListener {


    private lateinit var binding: ActivityHealthScoreBinding

    var mobilityResponse = Constants.INVALID
    var selfCareResponse = Constants.INVALID
    var usualActivitiesResponse = Constants.INVALID
    var painResponse = Constants.INVALID
    var anxietyResponse = Constants.INVALID

    var response: String = Constants.EMPTY

    var healthType: HealthType = HealthType.UNDEFINED

    var healthModel: DBHealth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHealthScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showBackButton()

        mobilityResponse = activityBundle.getInt(Extra.HEALTH_MOBILITY.key)
        selfCareResponse = activityBundle.getInt(Extra.HEALTH_SELF_CARE.key)
        usualActivitiesResponse = activityBundle.getInt(Extra.HEALTH_ACTIVITY.key)
        painResponse = activityBundle.getInt(Extra.HEALTH_PAIN.key)
        anxietyResponse = activityBundle.getInt(Extra.HEALTH_ANXIETY.key)


        prepare()
    }

    private fun prepare() {

        healthType = HealthType.SCORE

        binding.title.text = getString(healthType.title)
        binding.description.text = healthType.getDescription(this)

        binding.btnBack.setOnClickListener(this)
        binding.btnFinish.setOnClickListener(this)

        binding.healthScore.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                response = p0.toString().trim()
            }
        })
    }


    override fun onClick(v: View?) {
        when (v) {

            binding.btnBack -> {
                finish()
            }

            binding.btnFinish -> {
                hideKeyboard()
                if (isSelectionValid()) {

                    healthModel = DBHealth()
                    healthModel!!.healthID = TimeUtils.getCurrent().timeInMillis
                    healthModel!!.healthMobility = mobilityResponse
                    healthModel!!.healthSelfCare = selfCareResponse
                    healthModel!!.healthActivities = usualActivitiesResponse
                    healthModel!!.healthPain = painResponse
                    healthModel!!.healthAnxiety = anxietyResponse
                    healthModel!!.healthScore = response.toInt()
                    healthModel!!.healthTimestamp = TimeUtils.getCurrent().timeInMillis


                    sendData()
                } else {
                    UiUtils.showShortToast(this, R.string.health_add_score)
                }
            }

        }
    }

    private fun isSelectionValid(): Boolean {
        if (response != Constants.EMPTY && response.toInt() >= 0 && response.toInt() <= 100) {
            return true
        }
        return false
    }


    private fun sendData() {
        showProgressView()

        background {
            if (healthModel!!.isValid()) {
                TableHealth.upsert(this@HealthScoreActivity, healthModel!!)

                main {
                    scheduleNotification()

                    HealthManager.uploadHealth(this@HealthScoreActivity, healthModel!!, object : ResultListener {
                        override fun onResult(success: Boolean) {
                            hideProgressView()
                            if (success) {
                                UiUtils.showShortToast(this@HealthScoreActivity, getString(R.string.success_health_report))
                                setResult(ResultCode.RESULT_OK.value)
                                Router.getInstance()
                                    .clearTop(true)
                                    .startBaseActivity(this@HealthScoreActivity, Activities.HEALTH)
                            } else {
                                UiUtils.showShortToast(this@HealthScoreActivity, R.string.error)
                                setResult(ResultCode.RESULT_OK.value)
                                Router.getInstance()
                                    .clearTop(true)
                                    .startBaseActivity(this@HealthScoreActivity, Activities.HEALTH)
                            }
                        }
                    })
                }

            } else {
                hideProgressView()
                UiUtils.showShortToast(this@HealthScoreActivity, R.string.error)
                setResult(ResultCode.RESULT_OK.value)
                Router.getInstance()
                    .clearTop(true)
                    .startBaseActivity(this@HealthScoreActivity, Activities.HEALTH)
            }

        }
        hideProgressView()


    }

    private fun scheduleNotification() {
        background {
            val scheduledNotification: NotificationType
            if (Preferences.lifecycle(this@HealthScoreActivity).notificationScheduled != Constants.INVALID) {
                scheduledNotification = NotificationType.getById(Preferences.lifecycle(this@HealthScoreActivity).notificationScheduled)
                NotificationsManager.cancelNotification(this@HealthScoreActivity, scheduledNotification, true)
            }
            val notificationToSchedule = NotificationType.HEALTH
            Preferences.lifecycle(this@HealthScoreActivity).notificationScheduled = notificationToSchedule.id
            NotificationsManager.scheduleNotification(this@HealthScoreActivity, (TimeUtils.ONE_DAY_MILLIS * 30), notificationToSchedule)
        }
    }
}
