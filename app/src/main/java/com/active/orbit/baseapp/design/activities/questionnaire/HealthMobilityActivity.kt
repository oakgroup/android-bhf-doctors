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
import com.active.orbit.baseapp.databinding.ActivityHealthMobilityBinding
import com.active.orbit.baseapp.design.activities.engine.Activities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation
import com.active.orbit.baseapp.design.utils.UiUtils

class HealthMobilityActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityHealthMobilityBinding

    var response: String = Constants.EMPTY

    var healthType: HealthType = HealthType.UNDEFINED


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHealthMobilityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showBackButton()
        showLogoButton()


        prepare()
    }

    private fun prepare() {

        healthType = HealthType.MOBILITY

        binding.title.text = getString(healthType.title)
        binding.description.text = healthType.getDescription(this)
        binding.responseOne.text = getString(healthType.responseOne)
        binding.responseTwo.text = getString(healthType.responseTwo)
        binding.responseThree.text = getString(healthType.responseThree)


        binding.btnBack.setOnClickListener(this)
        binding.btnNext.setOnClickListener(this)
        binding.responseOne.setOnClickListener(this)
        binding.responseTwo.setOnClickListener(this)
        binding.responseThree.setOnClickListener(this)

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
                    bundle.putString(Extra.HEALTH_MOBILITY.key, response)
                    Router.getInstance().activityAnimation(ActivityAnimation.LEFT_RIGHT).startBaseActivityForResult(this, Activities.HEALTH_SELFCARE, bundle, HealthActivity.HEALTH_REQUEST_CODE)
                } else {
                    UiUtils.showShortToast(this, R.string.health_select_something)
                }
            }

            binding.responseOne -> {
                if (binding.responseOne.isChecked) {
                    if (binding.responseOne.isChecked) {
                        response = getString(healthType.responseOne)
                        binding.responseTwo.isChecked = false
                        binding.responseThree.isChecked = false
                    }
                }
            }
            binding.responseTwo -> {
                if (binding.responseTwo.isChecked) {
                    if (binding.responseTwo.isChecked) {
                        response = getString(healthType.responseTwo)
                        binding.responseOne.isChecked = false
                        binding.responseThree.isChecked = false
                    }
                }
            }
            binding.responseThree -> {
                if (binding.responseThree.isChecked) {
                    response = getString(healthType.responseThree)
                    binding.responseOne.isChecked = false
                    binding.responseTwo.isChecked = false
                }
            }
        }
    }

    private fun isSelectionValid(): Boolean {
        if ((binding.responseOne.isChecked || binding.responseTwo.isChecked || binding.responseThree.isChecked) && response != Constants.EMPTY) {
            return true
        }
        return false
    }

}
