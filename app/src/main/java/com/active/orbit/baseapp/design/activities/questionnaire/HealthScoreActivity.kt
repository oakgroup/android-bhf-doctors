package com.active.orbit.baseapp.design.activities.questionnaire

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.database.models.DBHealth
import com.active.orbit.baseapp.core.database.tables.TableHealth
import com.active.orbit.baseapp.core.enums.HealthType
import com.active.orbit.baseapp.core.routing.enums.Extra
import com.active.orbit.baseapp.core.routing.enums.ResultCode
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.core.utils.ThreadHandler.backgroundThread
import com.active.orbit.baseapp.core.utils.ThreadHandler.mainThread
import com.active.orbit.baseapp.core.utils.TimeUtils
import com.active.orbit.baseapp.databinding.ActivityHealthScoreBinding
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.utils.UiUtils

class HealthScoreActivity : BaseActivity(), View.OnClickListener {


    private lateinit var binding: ActivityHealthScoreBinding

    var mobilityResponse = Constants.EMPTY
    var selfCareResponse = Constants.EMPTY
    var usualActivitiesResponse = Constants.EMPTY
    var painResponse = Constants.EMPTY
    var anxietyResponse = Constants.EMPTY

    var response = Constants.EMPTY

    var healthType: HealthType = HealthType.UNDEFINED

    var healthModel: DBHealth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHealthScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showBackButton()
        showLogoButton()

        mobilityResponse = activityBundle.getString(Extra.HEALTH_MOBILITY.key)!!
        selfCareResponse = activityBundle.getString(Extra.HEALTH_SELF_CARE.key)!!
        usualActivitiesResponse = activityBundle.getString(Extra.HEALTH_ACTIVITY.key)!!
        painResponse = activityBundle.getString(Extra.HEALTH_PAIN.key)!!
        anxietyResponse = activityBundle.getString(Extra.HEALTH_ANXIETY.key)!!


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

        backgroundThread {
            if (healthModel!!.isValid()) {
                TableHealth.upsert(this, healthModel!!)

            } else {
                Logger.d("Model wrong:" + healthModel!!.description())

            }
            mainThread {
                UiUtils.showShortToast(this, getString(R.string.success_symptom_report))
                setResult(ResultCode.RESULT_OK.value)
                finish()
            }
        }
        hideProgressView()

        //TODO
//        val symptomsToUpload = ArrayList<DBSymptom>()
//        symptomsToUpload.add(symptom!!)
//
//
//        SymptomsManager.uploadSymptoms(this, symptomsToUpload, object : ResultListener {
//            override fun onResult(success: Boolean) {
//                hideProgressView()
//                if (success) {
//                    mainThread {
//                        UiUtils.showShortToast(this@ReportSymptomTimeActivity, getString(R.string.success_symptom_report))
//                        setResult(SymptomsActivity.SYMPTOM_RESULT_CODE_UPDATED)
//                        finish()
//                    }
//                } else {
//                    UiUtils.showShortToast(this@ReportSymptomTimeActivity, R.string.error)
//                }
//            }
//        })
    }

}
