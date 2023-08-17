package com.active.orbit.baseapp.design.activities.menu

import android.os.Bundle
import android.view.View
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.preferences.engine.Preferences
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.routing.enums.Extra
import com.active.orbit.baseapp.core.utils.TimeUtils
import com.active.orbit.baseapp.databinding.ActivityHelpBinding
import com.active.orbit.baseapp.design.activities.engine.Activities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation
import com.active.orbit.baseapp.design.dialogs.DataUploadPhoneDialog
import com.active.orbit.baseapp.design.dialogs.listeners.DataUploadPhoneDialogListener
import com.active.orbit.baseapp.design.utils.UiUtils
import uk.ac.shef.tracker.core.upload.TrackerUploadUtils
import uk.ac.shef.tracker.core.utils.background
import uk.ac.shef.tracker.core.utils.main

class HelpActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showBackButton()

        prepare()
    }

    fun prepare() {
        binding.menuConsentForm.disableClick()
        binding.menuFaqs.disableClick()
        binding.menuContactUs.disableClick()
        binding.menuTroubleshooting.disableClick()
        binding.menuAbout.disableClick()
        binding.menuUploadData.disableClick()

        binding.menuConsentForm.setOnClickListener(this)
        binding.menuFaqs.setOnClickListener(this)
        binding.menuContactUs.setOnClickListener(this)
        binding.menuTroubleshooting.setOnClickListener(this)
        binding.menuAbout.setOnClickListener(this)
        binding.menuUploadData.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v) {
            binding.menuConsentForm -> {
                val bundle = Bundle()
                bundle.putBoolean(Extra.FROM_MENU.key, true)
                bundle.putBoolean(Extra.FROM_HELP.key, true)
                Router.getInstance().activityAnimation(ActivityAnimation.LEFT_RIGHT).startBaseActivity(this, Activities.CONSENT_PRIVACY, bundle)
            }

            binding.menuFaqs -> {
                Router.getInstance().activityAnimation(ActivityAnimation.LEFT_RIGHT).startBaseActivity(this, Activities.FAQ)
            }

            binding.menuContactUs -> {
                Router.getInstance().activityAnimation(ActivityAnimation.LEFT_RIGHT).startBaseActivity(this, Activities.CONTACT_US)
            }

            binding.menuTroubleshooting -> {
            }

            binding.menuAbout -> {
                Router.getInstance().activityAnimation(ActivityAnimation.LEFT_RIGHT).startBaseActivity(this, Activities.ABOUT)
            }

            binding.menuUploadData -> {
                if (Preferences.user(this).isUserRegistered()) {
                    background {
                        val limitMillis = System.currentTimeMillis() - TimeUtils.ONE_MINUTE_MILLIS
                        val unsentPhoneDataCount = TrackerUploadUtils(this@HelpActivity).dataToSend(limitMillis)
                        main {
                            if (unsentPhoneDataCount > 0) {
                                val dataUploadPhoneDialog = DataUploadPhoneDialog()
                                dataUploadPhoneDialog.listener = object : DataUploadPhoneDialogListener {
                                    override fun onUploadCompleted() {
                                        UiUtils.showLongToast(this@HelpActivity, getString(R.string.upload_completed))
                                    }

                                    override fun onUploadInBackground() {
                                        UiUtils.showLongToast(this@HelpActivity, getString(R.string.upload_background_description))
                                    }
                                }
                                dataUploadPhoneDialog.isCancelable = false
                                dataUploadPhoneDialog.show(supportFragmentManager, DataUploadPhoneDialog::javaClass.name)
                            } else {
                                UiUtils.showLongToast(this@HelpActivity, getString(R.string.data_sent))
                            }
                        }
                    }
                }
            }
        }
    }
}