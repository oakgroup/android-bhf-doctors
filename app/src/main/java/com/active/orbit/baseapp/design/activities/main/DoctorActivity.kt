package com.active.orbit.baseapp.design.activities.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.enums.MainPanelType
import com.active.orbit.baseapp.core.listeners.ResultListener
import com.active.orbit.baseapp.core.managers.ConsentFormManager
import com.active.orbit.baseapp.core.preferences.engine.Preferences
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.utils.TimeUtils
import com.active.orbit.baseapp.databinding.ActivityDoctorBinding
import com.active.orbit.baseapp.design.activities.engine.Activities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation
import com.active.orbit.baseapp.design.dialogs.StartStudyDialog
import com.active.orbit.baseapp.design.dialogs.listeners.StartStudyDialogListener

class DoctorActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDoctorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showMenuComponent()
        setToolbarTitle(getString(R.string.app_name))

        prepare()

        // do not upload data on firestore
        /*
        if (Preferences.user(this).isUserRegistered()) {
            if (!Preferences.lifecycle(this).userDetailsUploaded) {
                FirestoreProvider.getInstance().updateUserDetails(this)
            }
        }
        */

        ConsentFormManager.retrieveConsentForm(thiss)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun prepare() {

        binding.tourPanel.disableClick()
        binding.registerPanel.disableClick()
        binding.tourPanel.setOnClickListener(this)
        binding.registerPanel.setOnClickListener(this)

        if (Preferences.user(this).isUserRegistered()) {
            binding.tourPanel.setPanel(MainPanelType.PRESCRIPTIONS_DOCTOR)
            binding.tourPanel.visibility = View.GONE

            binding.registerPanel.setPanel(MainPanelType.START_STUDY_WITH_NAME, getString(R.string.app_name))

        } else {
            binding.tourPanel.setPanel(MainPanelType.TOUR)
            binding.registerPanel.setPanel(MainPanelType.REGISTER)
        }
    }

    override fun onResume() {
        super.onResume()

        onboarded(object : ResultListener {
            override fun onResult(success: Boolean) {
                if (!success) {
                    // we cannot be here if the app has not been correctly onboarded
                    Router.getInstance()
                        .newTask(true)
                        .startBaseActivity(this@DoctorActivity, Activities.SPLASH)
                    finish()
                }
            }
        })
    }

    override fun onClick(v: View?) {

        when (v) {
            binding.registerPanel -> {
                if (Preferences.user(this).isUserRegistered()) {
                    val dialog = StartStudyDialog()
                    dialog.isCancelable = true
                    dialog.listener = object : StartStudyDialogListener {
                        override fun onStart() {
                            Preferences.user(thiss).studyStarted = true
                            Preferences.user(thiss).dateStudyStarted = TimeUtils.getCurrent().timeInMillis
                            Router.getInstance()
                                .activityAnimation(ActivityAnimation.FADE)
                                .homepage(thiss)
                        }
                    }
                    dialog.show(supportFragmentManager, StartStudyDialog::javaClass.name)
                } else {
                    Router.getInstance()
                        .activityAnimation(ActivityAnimation.LEFT_RIGHT)
                        .startBaseActivity(this, Activities.PATIENT_DETAILS)
                }
            }

            binding.tourPanel -> {
                Router.getInstance()
                    .activityAnimation(ActivityAnimation.LEFT_RIGHT)
                    .startBaseActivity(this, Activities.TOUR)
            }
        }
    }
}