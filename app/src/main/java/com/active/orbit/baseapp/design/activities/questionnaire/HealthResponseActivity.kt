package com.active.orbit.baseapp.design.activities.questionnaire

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.database.models.DBHealth
import com.active.orbit.baseapp.core.database.tables.TableHealth
import com.active.orbit.baseapp.core.enums.HealthType
import com.active.orbit.baseapp.core.routing.enums.Extra
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.core.utils.TimeUtils
import com.active.orbit.baseapp.databinding.ActivityHealthResponseDetailsBinding
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.utils.UiUtils
import uk.ac.shef.tracker.core.utils.background
import uk.ac.shef.tracker.core.utils.main

class HealthResponseActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityHealthResponseDetailsBinding

    var response: Int = Constants.INVALID

    var healthResponse: DBHealth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHealthResponseDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showBackButton()

        background {
            val modelId = activityBundle.getString(Extra.IDENTIFIER.key, Constants.EMPTY)
            if (modelId != Constants.EMPTY) {
                healthResponse = TableHealth.getById(this@HealthResponseActivity, modelId)
                if (healthResponse?.isValid() != true) {
                    main {
                        Logger.e("Model is not valid on on ${javaClass.name}")
                        UiUtils.showShortToast(this@HealthResponseActivity, R.string.health_show_error)
                        finish()
                    }
                } else {
                    main {
                        prepare()
                    }
                }
            } else {
                Logger.e("Model is null ${javaClass.name}")
                UiUtils.showShortToast(this@HealthResponseActivity, R.string.health_show_error)
                finish()
            }
        }

    }

    private fun prepare() {

        binding.timestamp.text = TimeUtils.format(TimeUtils.getCurrent(healthResponse?.healthID!!), Constants.DATE_FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)

        binding.mobilityTitle.text = getString(HealthType.MOBILITY.title)
        binding.selfcareTitle.text = getString(HealthType.SELF_CARE.title)
        binding.usualActivitiesTitle.text = getString(HealthType.USUAL_ACTIVITIES.title)
        binding.painTitle.text = getString(HealthType.PAIN.title)
        binding.anxietyTitle.text = getString(HealthType.ANXIETY.title)

        binding.mobilityResponse.text = HealthType.MOBILITY.getResponse(healthResponse?.healthMobility!!, this)
        binding.selfcareResponse.text = HealthType.SELF_CARE.getResponse(healthResponse?.healthSelfCare!!, this)
        binding.usualActivitiesResponse.text = HealthType.USUAL_ACTIVITIES.getResponse(healthResponse?.healthActivities!!, this)
        binding.painResponse.text = HealthType.PAIN.getResponse(healthResponse?.healthPain!!, this)
        binding.anxietyResponse.text = HealthType.ANXIETY.getResponse(healthResponse?.healthAnxiety!!, this)

        binding.mobilityResponse.isChecked = true
        binding.selfcareResponse.isChecked = true
        binding.usualActivitiesResponse.isChecked = true
        binding.painResponse.isChecked = true
        binding.anxietyResponse.isChecked = true

        binding.healthScoreProgress.setBackgroundLineColorOne(ContextCompat.getColor(this, R.color.colorSecondaryLight))
        binding.healthScoreProgress.setBackgroundLineColorTwo(ContextCompat.getColor(this, R.color.colorSecondaryLight))
        binding.healthScoreProgress.setProgressLineColorOne(ContextCompat.getColor(this, R.color.colorPrimary))
        binding.healthScoreProgress.setProgressLineColorTwo(ContextCompat.getColor(this, R.color.colorSecondary))
        binding.healthScoreProgress.setLineWidth(20f)
        binding.healthScoreProgress.hideProgressIcon()
        binding.healthScoreProgress.setMaxProgress(Constants.HEALTH_MAX_PROGRESS.toFloat())
        binding.healthScoreProgress.setProgress(healthResponse?.healthScore!!.toFloat())

        binding.healthScore.text = getString(R.string.health_score_label, healthResponse?.healthScore.toString())


        binding.mobilityResponse.setOnClickListener(this)
        binding.selfcareResponse.setOnClickListener(this)
        binding.usualActivitiesResponse.setOnClickListener(this)
        binding.painResponse.setOnClickListener(this)
        binding.anxietyResponse.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v) {
            binding.mobilityResponse -> {
                binding.mobilityResponse.isChecked = true
                UiUtils.showShortToast(this, R.string.your_answer_cannot_change)
            }

            binding.selfcareResponse -> {
                binding.selfcareResponse.isChecked = true
                UiUtils.showShortToast(this, R.string.your_answer_cannot_change)
            }

            binding.usualActivitiesResponse -> {
                binding.usualActivitiesResponse.isChecked = true
                UiUtils.showShortToast(this, R.string.your_answer_cannot_change)
            }

            binding.painResponse -> {
                binding.painResponse.isChecked = true
                UiUtils.showShortToast(this, R.string.your_answer_cannot_change)
            }

            binding.anxietyResponse -> {
                binding.anxietyResponse.isChecked = true
                UiUtils.showShortToast(this, R.string.your_answer_cannot_change)
            }
        }
    }

}
