package com.active.orbit.baseapp.design.activities.questionnaire

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.enums.HealthType
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.routing.enums.Extra
import com.active.orbit.baseapp.core.routing.enums.ResultCode
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.databinding.ActivityHealthUsualActivitiesBinding
import com.active.orbit.baseapp.design.activities.engine.Activities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation
import com.active.orbit.baseapp.design.utils.UiUtils

class HealthUsualActivitiesActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityHealthUsualActivitiesBinding

    var mobilityResponse = Constants.INVALID
    var selfCareResponse = Constants.INVALID

    var response: Int = Constants.INVALID

    var healthType: HealthType = HealthType.UNDEFINED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHealthUsualActivitiesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showBackButton()
        showLogoButton()

        mobilityResponse = activityBundle.getInt(Extra.HEALTH_MOBILITY.key)
        selfCareResponse = activityBundle.getInt(Extra.HEALTH_SELF_CARE.key)


        prepare()
    }

    private fun prepare() {

        healthType = HealthType.USUAL_ACTIVITIES

        binding.title.text = getString(healthType.title)
        binding.description.text = healthType.getDescription(this)
        binding.responseOne.text = healthType.getResponse(Constants.HEALTH_RESPONSE_ONE_ID, this)
        binding.responseTwo.text = healthType.getResponse(Constants.HEALTH_RESPONSE_TWO_ID, this)
        binding.responseThree.text = healthType.getResponse(Constants.HEALTH_RESPONSE_THREE_ID, this)
        binding.responseFour.text = healthType.getResponse(Constants.HEALTH_RESPONSE_FOUR_ID, this)
        binding.responseFive.text = healthType.getResponse(Constants.HEALTH_RESPONSE_FIVE_ID, this)

        binding.btnBack.setOnClickListener(this)
        binding.btnNext.setOnClickListener(this)
        binding.responseOne.setOnClickListener(this)
        binding.responseTwo.setOnClickListener(this)
        binding.responseThree.setOnClickListener(this)
        binding.responseFour.setOnClickListener(this)
        binding.responseFive.setOnClickListener(this)

    }

    // TODO manage deprecation
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == HealthActivity.HEALTH_REQUEST_CODE && resultCode == ResultCode.RESULT_OK.value) {
            finish()
        }
    }

    override fun onClick(v: View?) {
        when (v) {

            binding.btnBack -> {
                finish()
            }

            binding.btnNext -> {
                hideKeyboard()
                if (isSelectionValid()) {
                    val bundle = Bundle()
                    bundle.putInt(Extra.HEALTH_MOBILITY.key, mobilityResponse)
                    bundle.putInt(Extra.HEALTH_SELF_CARE.key, selfCareResponse)
                    bundle.putInt(Extra.HEALTH_ACTIVITY.key, response)
                    Router.getInstance().activityAnimation(ActivityAnimation.LEFT_RIGHT).startBaseActivityForResult(this, Activities.HEALTH_PAIN, bundle, HealthActivity.HEALTH_REQUEST_CODE)
                } else {
                    UiUtils.showShortToast(this, R.string.health_select_something)
                }
            }

            binding.responseOne -> {
                if (binding.responseOne.isChecked) {
                    response = Constants.HEALTH_RESPONSE_ONE_ID
                    binding.responseTwo.isChecked = false
                    binding.responseThree.isChecked = false
                    binding.responseFour.isChecked = false
                    binding.responseFive.isChecked = false
                }
            }
            binding.responseTwo -> {
                if (binding.responseTwo.isChecked) {
                    response = Constants.HEALTH_RESPONSE_TWO_ID
                    binding.responseOne.isChecked = false
                    binding.responseThree.isChecked = false
                    binding.responseFour.isChecked = false
                    binding.responseFive.isChecked = false
                }
            }
            binding.responseThree -> {
                if (binding.responseThree.isChecked) {
                    response = Constants.HEALTH_RESPONSE_THREE_ID
                    binding.responseOne.isChecked = false
                    binding.responseTwo.isChecked = false
                    binding.responseFour.isChecked = false
                    binding.responseFive.isChecked = false
                }
            }
            binding.responseFour -> {
                if (binding.responseFour.isChecked) {
                    response = Constants.HEALTH_RESPONSE_FOUR_ID
                    binding.responseOne.isChecked = false
                    binding.responseTwo.isChecked = false
                    binding.responseThree.isChecked = false
                    binding.responseFive.isChecked = false
                }
            }
            binding.responseFive -> {
                if (binding.responseFive.isChecked) {
                    response = Constants.HEALTH_RESPONSE_FIVE_ID
                    binding.responseOne.isChecked = false
                    binding.responseTwo.isChecked = false
                    binding.responseThree.isChecked = false
                    binding.responseFour.isChecked = false
                }
            }
        }
    }

    private fun isSelectionValid(): Boolean {
        if ((binding.responseOne.isChecked || binding.responseTwo.isChecked || binding.responseThree.isChecked || binding.responseFour.isChecked || binding.responseFive.isChecked) && response != Constants.INVALID) {
            return true
        }
        return false
    }
}
