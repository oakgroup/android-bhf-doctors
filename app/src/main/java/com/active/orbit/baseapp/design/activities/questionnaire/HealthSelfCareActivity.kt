package com.active.orbit.baseapp.design.activities.questionnaire

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.database.models.DBHealth
import com.active.orbit.baseapp.core.database.tables.TableReportedSymptoms
import com.active.orbit.baseapp.core.enums.HealthType
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.routing.enums.Extra
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.core.utils.ThreadHandler.backgroundThread
import com.active.orbit.baseapp.core.utils.ThreadHandler.mainThread
import com.active.orbit.baseapp.core.utils.TimeUtils
import com.active.orbit.baseapp.core.utils.Utils
import com.active.orbit.baseapp.databinding.ActivityHealthMobilityBinding
import com.active.orbit.baseapp.databinding.ActivityHealthSelfcareBinding
import com.active.orbit.baseapp.databinding.ActivityReportSymptomDetailsBinding
import com.active.orbit.baseapp.design.activities.engine.Activities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation
import com.active.orbit.baseapp.design.dialogs.SelectSeverityDialog
import com.active.orbit.baseapp.design.dialogs.SelectSymptomDialog
import com.active.orbit.baseapp.design.dialogs.listeners.SelectSeverityDialogListener
import com.active.orbit.baseapp.design.dialogs.listeners.SelectSymptomDialogListener
import com.active.orbit.baseapp.design.utils.UiUtils

class HealthSelfCareActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityHealthSelfcareBinding

    var mobilityResponse = Constants.EMPTY
    var response = Constants.EMPTY

    var healthType: HealthType = HealthType.UNDEFINED


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHealthSelfcareBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showBackButton()
        showLogoButton()

        mobilityResponse = activityBundle.getString(Extra.HEALTH_MOBILITY.key)!!


        prepare()
    }

    private fun prepare() {

        healthType = HealthType.SELF_CARE

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


    override fun onClick(v: View?) {
        when (v) {

            binding.btnBack -> {
                finish()
            }

            binding.btnNext -> {
                hideKeyboard()
                if (isSelectionValid()) {
                    val bundle = Bundle()
                    bundle.putString(Extra.HEALTH_MOBILITY.key, mobilityResponse)
                    bundle.putString(Extra.HEALTH_SELF_CARE.key, response)
                    Router.getInstance().activityAnimation(ActivityAnimation.LEFT_RIGHT).startBaseActivityForResult(this, Activities.HEALTH_USUAL_ACTIVITIES, bundle, HealthActivity.HEALTH_REQUEST_CODE)
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
