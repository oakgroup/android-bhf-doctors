package com.active.orbit.baseapp.design.activities.registration

import android.os.Bundle
import android.view.View
import androidx.core.text.HtmlCompat
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.listeners.ResultListener
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.routing.enums.Extra
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.databinding.ActivityOnBoardingUnusedRestrictionsBinding
import com.active.orbit.baseapp.design.activities.engine.Activities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation

class OnBoardingUnusedRestrictionsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityOnBoardingUnusedRestrictionsBinding
    private var fromMenu = false
    private var settingsOpened = false


    private var userConsentName = Constants.EMPTY
    private var userConsentDate = Constants.INVALID.toLong()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingUnusedRestrictionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showBackButton()


        fromMenu = activityBundle.getBoolean(Extra.FROM_MENU.key, false)

        if (!fromMenu) {
            userConsentName = activityBundle.getString(Extra.USER_CONSENT_NAME.key)!!
            userConsentDate = activityBundle.getLong(Extra.USER_CONSENT_DATE.key)
        }

        prepare()
    }

    override fun onResume() {
        super.onResume()

        onboardedUnusedRestrictions(object : ResultListener {
            override fun onResult(success: Boolean) {
                if (success) {
                    binding.authorisedView.visibility = View.VISIBLE
                    if (!fromMenu) {
                        binding.buttons.visibility = View.VISIBLE
                        binding.btnSettings.visibility = View.GONE
                    } else {
                        binding.buttons.visibility = View.GONE
                        binding.btnSettings.visibility = View.VISIBLE
                    }
                } else {
                    binding.authorisedView.visibility = View.GONE
                    if (!fromMenu && settingsOpened) {
                        binding.buttons.visibility = View.VISIBLE
                        binding.btnSettings.visibility = View.GONE
                    } else {
                        binding.buttons.visibility = View.GONE
                        binding.btnSettings.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    private fun prepare() {
        binding.description.text = HtmlCompat.fromHtml(getString(R.string.disable_restrictions_description), HtmlCompat.FROM_HTML_MODE_COMPACT)

        binding.btnSettings.setOnClickListener(this)
        binding.btnNext.setOnClickListener(this)
        binding.btnBack.setOnClickListener(this)


        if (fromMenu) {
            binding.stepsLayout.visibility = View.GONE
            binding.progressText.visibility = View.GONE
            binding.title.text = getString(R.string.disable_restrictions)
        } else {
            binding.stepsLayout.visibility = View.VISIBLE
            binding.progressText.visibility = View.VISIBLE
            binding.title.text = getString(R.string.disable_restrictions_title)
        }

    }

    private fun proceed() {
        if (!fromMenu) {
            val bundle = Bundle()
            bundle.putString(Extra.USER_CONSENT_NAME.key, userConsentName)
            bundle.putLong(Extra.USER_CONSENT_DATE.key, userConsentDate)
            Router.getInstance()
                .activityAnimation(ActivityAnimation.LEFT_RIGHT)
                .startBaseActivity(this, Activities.PATIENT_DETAILS, bundle)
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnNext -> {
                proceed()
            }

            binding.btnBack -> {
                finish()
            }

            binding.btnSettings -> {
                settingsOpened = true
                requestUnusedRestrictions()
            }
        }
    }
}