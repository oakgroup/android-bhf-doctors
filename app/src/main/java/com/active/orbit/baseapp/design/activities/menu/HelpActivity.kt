package com.active.orbit.baseapp.design.activities.menu

import android.os.Bundle
import android.view.View
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.preferences.engine.Preferences
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.utils.ThreadHandler.backgroundThread
import com.active.orbit.baseapp.core.utils.ThreadHandler.mainThread
import com.active.orbit.baseapp.core.utils.TimeUtils
import com.active.orbit.baseapp.databinding.ActivityHelpBinding
import com.active.orbit.baseapp.design.activities.engine.Activities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation
import com.active.orbit.baseapp.design.dialogs.DataUploadPhoneDialog
import com.active.orbit.baseapp.design.dialogs.listeners.DataUploadPhoneDialogListener
import com.active.orbit.baseapp.design.utils.UiUtils
import com.active.orbit.tracker.core.upload.UploadUtils

class HelpActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showBackButton()
        showLogoButton()

        prepare()
    }

    fun prepare() {
        binding.menuWhatWeOffer.disableClick()
        binding.menuFaqs.disableClick()
        binding.menuContactUs.disableClick()
        binding.menuTroubleshooting.disableClick()
        binding.menuAbout.disableClick()
        binding.menuUploadData.disableClick()

        binding.menuWhatWeOffer.setOnClickListener(this)
        binding.menuFaqs.setOnClickListener(this)
        binding.menuContactUs.setOnClickListener(this)
        binding.menuTroubleshooting.setOnClickListener(this)
        binding.menuAbout.setOnClickListener(this)
        binding.menuUploadData.setOnClickListener(this)

        if (Preferences.user(this).isUserRegistered()) {
            binding.menuUploadData.visibility = View.VISIBLE
        } else {
            binding.menuUploadData.visibility = View.GONE
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.menuWhatWeOffer -> {
                Router.getInstance().activityAnimation(ActivityAnimation.LEFT_RIGHT).startBaseActivity(this, Activities.TOUR)
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
                    backgroundThread {
                        val limitMillis = System.currentTimeMillis() - TimeUtils.ONE_MINUTE_MILLIS
                        val unsentPhoneDataCount = UploadUtils(this).dataToSend(limitMillis)
                        mainThread {
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