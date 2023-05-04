package com.active.orbit.baseapp.design.activities.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.active.orbit.baseapp.core.database.tables.TablePrograms
import com.active.orbit.baseapp.core.enums.MainPanelType
import com.active.orbit.baseapp.core.firestore.providers.FirestoreProvider
import com.active.orbit.baseapp.core.listeners.ResultListener
import com.active.orbit.baseapp.core.managers.ProgrammesManager
import com.active.orbit.baseapp.core.preferences.engine.Preferences
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.core.utils.ThreadHandler.backgroundThread
import com.active.orbit.baseapp.core.utils.ThreadHandler.mainThread
import com.active.orbit.baseapp.core.utils.TimeUtils
import com.active.orbit.baseapp.databinding.ActivityDoctorBinding
import com.active.orbit.baseapp.design.activities.engine.Activities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation
import com.active.orbit.baseapp.design.dialogs.StartProgrammeDialog
import com.active.orbit.baseapp.design.dialogs.listeners.StartProgrammeDialogListener

class DoctorActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDoctorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showMenuComponent()
        showLogo()

        prepare()

        ProgrammesManager.downloadProgrammes(thiss)

        if (!Preferences.lifecycle(this).userDetailsUploaded) {
            FirestoreProvider.getInstance().updateUserDetails(this)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun prepare() {

        binding.tourPanel.disableClick()
        binding.registerPanel.disableClick()
        binding.tourPanel.setOnClickListener(this)
        binding.registerPanel.setOnClickListener(this)

        if (Preferences.user(this).isUserRegistered()) {
            binding.tourPanel.setPanel(MainPanelType.PRESCRIPTIONS_DOCTOR)

            backgroundThread {
                val idProgram = Preferences.user(this).idProgram ?: Constants.EMPTY
                val dbProgram = TablePrograms.getById(this, idProgram)
                mainThread {
                    if (dbProgram?.isValid() == true) {
                        binding.registerPanel.setPanel(MainPanelType.START_PROGRAMME_WITH_NAME, dbProgram.name)
                    } else {
                        Logger.e("Program with id $idProgram not found on database")
                        binding.registerPanel.setPanel(MainPanelType.START_PROGRAMME)
                    }
                }
            }
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
                    val dialog = StartProgrammeDialog()
                    dialog.isCancelable = true
                    dialog.listener = object : StartProgrammeDialogListener {
                        override fun onStart() {
                            Preferences.user(thiss).programStarted = true
                            Preferences.user(thiss).dateProgramStarted = TimeUtils.getCurrent().timeInMillis
                            Router.getInstance()
                                .activityAnimation(ActivityAnimation.FADE)
                                .homepage(thiss)
                        }
                    }
                    dialog.show(supportFragmentManager, StartProgrammeDialog::javaClass.name)
                } else {
                    Router.getInstance()
                        .activityAnimation(ActivityAnimation.LEFT_RIGHT)
                        .startBaseActivity(this, Activities.SELECT_PROGRAMME)
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