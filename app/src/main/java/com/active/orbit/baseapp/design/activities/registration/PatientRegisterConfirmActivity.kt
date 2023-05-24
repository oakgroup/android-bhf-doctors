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
import com.active.orbit.tracker.core.tracker.TrackerManager

class PatientRegisterConfirmActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRegisterPatientConfirmBinding

    private var programID = Constants.EMPTY
    private var userNhsNumber = Constants.EMPTY
    private var userFirstName = Constants.EMPTY
    private var userLastName = Constants.EMPTY
    private var userDOB = Constants.INVALID.toLong()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterPatientConfirmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showBackButton()
        showLogoButton()

        programID = activityBundle.getString(Extra.PROGRAM_ID.key)!!
        userNhsNumber = activityBundle.getString(Extra.USER_NHS_NUMBER.key)!!
        userFirstName = activityBundle.getString(Extra.USER_FIRST_NAME.key)!!
        userLastName = activityBundle.getString(Extra.USER_LAST_NAME.key)!!
        userDOB = activityBundle.getLong(Extra.USER_DOB.key)

        prepare()
    }

    private fun prepare() {

        binding.btnBack.setOnClickListener(this)

        backgroundThread {
            val program = TablePrograms.getById(this, programID)
            mainThread {
                binding.userNhsNumber.text = getString(R.string.patient_id_value, userNhsNumber)
                binding.userName.text = getString(R.string.name_value, userFirstName, userLastName)
                binding.userDOB.text = getString(R.string.date_of_birth_value, TimeUtils.format(TimeUtils.getCurrent(userDOB), Constants.DATE_FORMAT_YEAR_MONTH_DAY))

                if (program?.isValid() == true) {
                    binding.programme.text = getString(R.string.patient_programme, "BHF")

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

                //TODO add first/last name and dob
                val request = UserRegistrationRequest()
                request.phoneModel = Utils.getPhoneModel()
                request.appVersion = Utils.getAppVersion(this)
                request.androidVersion = Utils.getAndroidVersion()
                request.idProgram = programID
                request.userNhsNumber = userNhsNumber

                request.batteryLevel = Utils.getBatteryPercentage(this)
                request.isCharging = Utils.isCharging(this)
                request.registrationTimestamp = TimeUtils.getCurrent().timeInMillis

                UserManager.registerUser(this, request, object : UserRegistrationListener {
                    override fun onSuccess(map: UserRegistrationMap) {
                        if (map.participantIdCounter > 1) {
                            Logger.d("Already existing user with patient id $userNhsNumber, ask for confirmation")
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
        Preferences.user(this).userNhsNumber = userNhsNumber
        Preferences.user(this).userFirstName = userFirstName
        Preferences.user(this).userLastName = userLastName
        Preferences.user(this).userDateOfBirth = userDOB

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