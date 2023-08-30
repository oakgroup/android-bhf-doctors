package com.active.orbit.baseapp.design.activities.registration

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.core.text.HtmlCompat
import com.active.orbit.baseapp.core.preferences.engine.Preferences
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.routing.enums.Extra
import com.active.orbit.baseapp.databinding.ActivityConsentPrivacyBinding
import com.active.orbit.baseapp.design.activities.engine.Activities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation

class ConsentPrivacyActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityConsentPrivacyBinding
    private var fromMenu = false
    private var fromHelp = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsentPrivacyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fromMenu = activityBundle.getBoolean(Extra.FROM_MENU.key)
        fromHelp = activityBundle.getBoolean(Extra.FROM_HELP.key)

        if (fromMenu || fromHelp) showBackButton()

        prepare()
    }

    private fun prepare() {

        binding.consentText.text = HtmlCompat.fromHtml(Preferences.user(this).consentFormText, HtmlCompat.FROM_HTML_MODE_COMPACT)

        binding.privacyLink.paintFlags = binding.privacyLink.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        binding.privacyLink.setOnClickListener(this)


        if (fromMenu) {
            binding.progressText.visibility = View.GONE
            binding.steps.visibility = View.GONE
            binding.buttons.visibility = View.GONE
            binding.btnBack.setOnClickListener(this)
        } else {
            binding.progressText.visibility = View.VISIBLE
            binding.steps.visibility = View.VISIBLE
            binding.buttons.visibility = View.VISIBLE
            binding.btnNext.setOnClickListener(this)
            binding.btnBack.visibility = View.GONE
        }
    }

    override fun onClick(v: View?) {
        when (v) {

            binding.btnNext -> {
                Router.getInstance()
                    .activityAnimation(ActivityAnimation.LEFT_RIGHT)
                    .startBaseActivity(this, Activities.CONSENT_FORM)
            }

            binding.btnBack -> finish()

            binding.privacyLink -> {
                Router.getInstance().openPrivacyPolicy(this)
            }
        }
    }
}