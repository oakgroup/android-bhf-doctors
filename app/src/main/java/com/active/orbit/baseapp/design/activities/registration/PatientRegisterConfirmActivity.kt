package com.active.orbit.baseapp.design.activities.registration

import android.os.Bundle
import android.view.View
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.database.tables.TablePrograms
import com.active.orbit.baseapp.core.deserialization.UserRegistrationMap
import com.active.orbit.baseapp.core.enums.SuccessMessageType
import com.active.orbit.baseapp.core.firestore.providers.FirestoreProvider
import com.active.orbit.baseapp.core.listeners.UserRegistrationListener
import com.active.orbit.baseapp.core.managers.UserManager
import com.active.orbit.baseapp.core.preferences.engine.Preferences
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.routing.enums.Extra
import com.active.orbit.baseapp.core.serialization.UserRegistrationRequest
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.core.utils.ThreadHandler.backgroundThread
import com.active.orbit.baseapp.core.utils.ThreadHandler.mainThread
import com.active.orbit.baseapp.core.utils.TimeUtils
import com.active.orbit.baseapp.core.utils.Utils
import com.active.orbit.baseapp.databinding.ActivityRegisterPatientConfirmBinding
import com.active.orbit.baseapp.design.activities.engine.Activities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation
import com.active.orbit.baseapp.design.dialogs.ConfirmRegistrationDialog
import com.active.orbit.baseapp.design.dialogs.listeners.ConfirmRegistrationDialogListener
import com.active.orbit.baseapp.design.utils.UiUtils
import com.active.orbit.tracker.TrackerManager

class PatientRegisterConfirmActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRegisterPatientConfirmBinding

    private var programID = Constants.EMPTY
    private var patientID = Constants.EMPTY
    private var patientSex = Constants.EMPTY
    private var patientAge = Constants.EMPTY
    private var patientWeight = Constants.EMPTY
    private var patientHeight = Constants.EMPTY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterPatientConfirmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showBackButton()
        showLogoButton()

        programID = activityBundle.getString(Extra.PROGRAM_ID.key)!!
        patientID = activityBundle.getString(Extra.USER_PATIENT_ID.key)!!
        patientSex = activityBundle.getString(Extra.USER_SEX.key)!!
        patientAge = activityBundle.getString(Extra.USER_AGE.key)!!
        patientWeight = activityBundle.getString(Extra.USER_WEIGHT.key)!!
        patientHeight = activityBundle.getString(Extra.USER_HEIGHT.key)!!

        prepare()
    }

    private fun prepare() {

        binding.btnBack.setOnClickListener(this)

        backgroundThread {
            val program = TablePrograms.getById(this, programID)
            mainThread {
                binding.patientId.text = getString(R.string.patient_id_value, patientID)
                binding.patientSex.text = getString(R.string.sex_value, patientSex)
                binding.patientAge.text = getString(R.string.age_value, patientAge)
                binding.patientWeight.text = getString(R.string.weight_value, patientWeight)
                binding.patientHeight.text = getString(R.string.height_value, patientHeight)

                if (program?.isValid() == true) {
                    binding.patientProgramme.text = getString(R.string.patient_programme, program.name)

                    binding.btnConfirm.setOnClickListener(this)
                } else {
                    UiUtils.showShortToast(this, R.string.error)
                }
            }
        }
    }


    override fun onClick(v: View?) {
        when (v) {
            binding.btnConfirm -> {
                showProgressView()

                val request = UserRegistrationRequest()
                request.phoneModel = Utils.getPhoneModel()
                request.appVersion = Utils.getAppVersion(this)
                request.androidVersion = Utils.getAndroidVersion()
                request.idProgram = programID
                request.idPatient = patientID
                request.userSex = patientSex
                request.userAge = patientAge
                request.userWeight = patientWeight
                request.userHeight = patientHeight
                request.batteryLevel = Utils.getBatteryPercentage(this)
                request.isCharging = Utils.isCharging(this)
                request.registrationTimestamp = TimeUtils.getCurrent().timeInMillis

                UserManager.registerUser(this, request, object : UserRegistrationListener {
                    override fun onSuccess(map: UserRegistrationMap) {
                        if (map.participantIdCounter > 1) {
                            Logger.d("Already existing user with patient id $patientID, ask for confirmation")
                            val dialog = ConfirmRegistrationDialog()
                            dialog.isCancelable = false
                            dialog.listener = object : ConfirmRegistrationDialogListener {
                                override fun onRegister() {
                                    completeRegistration(request, map)
                                }

                                override fun onCancel() {
                                    finish()
                                }
                            }
                            dialog.show(supportFragmentManager, ConfirmRegistrationDialog::javaClass.name)
                        } else {
                            completeRegistration(request, map)
                        }
                    }

                    override fun onError() {
                        UiUtils.showShortToast(this@PatientRegisterConfirmActivity, R.string.error)
                    }
                })
            }

            binding.btnBack -> {
                finish()
            }
        }
    }

    private fun completeRegistration(request: UserRegistrationRequest, map: UserRegistrationMap) {
        Logger.d("User successfully registered with id ${map.id}")
        Preferences.user(this).register(map.id, request.idProgram!!)
        Preferences.user(this).idPatient = patientID
        Preferences.user(this).userSex = patientSex
        Preferences.user(this).userAge = patientAge
        Preferences.user(this).userWeight = patientWeight
        Preferences.user(this).userHeight = patientHeight

        Preferences.lifecycle(this).userDetailsUploaded = false

        // check registration with the server
        TrackerManager.getInstance(this).saveUserRegistrationId(map.id)
        FirestoreProvider.getInstance().updateUserDetails(this)

        val bundle = Bundle()
        bundle.putInt(Extra.SUCCESS_MESSAGE.key, SuccessMessageType.REGISTRATION.id)
        Router.getInstance().activityAnimation(ActivityAnimation.BOTTOM_TOP).startBaseActivity(thiss, Activities.SUCCESS_MESSAGE, bundle)
        finish()
    }
}