package com.active.orbit.baseapp.design.activities.registration

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.routing.enums.Extra
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.databinding.ActivityTermsConditionsBinding
import com.active.orbit.baseapp.design.activities.engine.Activities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation
import com.active.orbit.baseapp.design.utils.UiUtils

class TermsAndConditionsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityTermsConditionsBinding
    private var programID = Constants.EMPTY


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsConditionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showBackButton()
        showLogoButton()

        programID = activityBundle.getString(Extra.PROGRAM_ID.key)!!

        prepare()

    }

    private fun prepare() {

        binding.btnNext.setOnClickListener(this)
        binding.btnBack.setOnClickListener(this)

        binding.termsLink.paintFlags = binding.termsLink.paintFlags or Paint.UNDERLINE_TEXT_FLAG
    }

    override fun onClick(v: View?) {
        when (v) {

            binding.btnNext -> {
                if (binding.termsCheckBox.isChecked) {
                    val bundle = Bundle()
                    bundle.putString(Extra.PROGRAM_ID.key, programID)
                    Router.getInstance()
                        .activityAnimation(ActivityAnimation.LEFT_RIGHT)
                        .startBaseActivity(this, Activities.PATIENT_DETAILS, bundle)
                } else {
                    UiUtils.showLongToast(this, R.string.accept_toc_please)
                }
            }

            binding.btnBack -> finish()

            binding.termsLink -> Router.getInstance().openTermsAndConditions(this)

        }
    }

}