package com.active.orbit.baseapp.design.activities.registration

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.DatePicker
import androidx.core.content.ContextCompat
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.deserialization.UserRegistrationMap
import com.active.orbit.baseapp.core.enums.SuccessMessageType
import com.active.orbit.baseapp.core.listeners.UserRegistrationListener
import com.active.orbit.baseapp.core.managers.UserManager
import com.active.orbit.baseapp.core.preferences.engine.Preferences
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.routing.enums.Extra
import com.active.orbit.baseapp.core.serialization.UserRegistrationRequest
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.core.utils.TimeUtils
import com.active.orbit.baseapp.core.utils.Utils
import com.active.orbit.baseapp.core.utils.Validator
import com.active.orbit.baseapp.databinding.ActivityPatientDetailsBinding
import com.active.orbit.baseapp.design.activities.engine.Activities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation
import com.active.orbit.baseapp.design.dialogs.ConfirmRegistrationDialog
import com.active.orbit.baseapp.design.dialogs.SelectSexDialog
import com.active.orbit.baseapp.design.dialogs.listeners.ConfirmRegistrationDialogListener
import com.active.orbit.baseapp.design.dialogs.listeners.SelectSexDialogListener
import com.active.orbit.baseapp.design.recyclers.models.SexModel
import com.active.orbit.baseapp.design.utils.UiUtils
import uk.ac.shef.tracker.core.tracker.TrackerManager
import java.util.*

class PatientDetailsActivity : BaseActivity(), View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private lateinit var binding: ActivityPatientDetailsBinding

    private var sex: SexModel? = null
    private var dateOfBirth: Calendar? = null
    private var fromMenu = false

    private var userConsentName = Constants.EMPTY
    private var userConsentDate = Constants.INVALID.toLong()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showBackButton()

        fromMenu = activityBundle.getBoolean(Extra.FROM_MENU.key)

        prepare()
    }

    private fun prepare() {

        binding.btnSexSelection.setIcon(R.drawable.ic_dropdown)
        binding.btnSexSelection.setText(getString(R.string.select))
        binding.btnSexSelection.disableClick()

        binding.btnDateBirth.setIcon(R.drawable.ic_calendar)
        binding.btnDateBirth.setText(getString(R.string.select))
        binding.btnDateBirth.disableClick()


        if (fromMenu) {
            binding.nhsNumberTitle.text = getString(R.string.patient_id)
            binding.insertIdEntryView.isEnabled = false
            binding.insertIdEntryView.setPin(Preferences.user(this).userNhsNumber)
            binding.layoutID.setOnClickListener { UiUtils.showShortToast(this, "NHS number cannot change") }
            val sexModel = SexModel(Preferences.user(this).userSex!!)
            this@PatientDetailsActivity.sex = sexModel
            binding.btnSexSelection.setText(Preferences.user(this).userSex)

            binding.firstName.isEnabled = false
            binding.lastName.isEnabled = false
            binding.postcode.isEnabled = false
            binding.email.isEnabled = false
            binding.phone.isEnabled = false

            binding.firstName.setText(Preferences.user(this).userFirstName)
            binding.lastName.setText(Preferences.user(this).userLastName)
            binding.postcode.setText(Preferences.user(this).userPostcode)

            if (Preferences.user(this).userEmail != Constants.EMPTY) {
                binding.email.setText(Preferences.user(this).userEmail)
            } else {
                binding.email.hint = getString(R.string.email_address_hint)
            }

            if (Preferences.user(this).userPhone != Constants.EMPTY) {
                binding.phone.setText(Preferences.user(this).userPhone)
            } else {
                binding.phone.hint = getString(R.string.phone_number_hint)
            }

            dateOfBirth = TimeUtils.getCurrent(Preferences.user(this).userDateOfBirth!!)
            binding.btnDateBirth.setText(TimeUtils.format(dateOfBirth!!, Constants.DATE_FORMAT_YEAR_MONTH_DAY))

            binding.progressText.visibility = View.GONE
            binding.stepsLayout.visibility = View.GONE
            binding.buttons.visibility = View.GONE
            binding.btnSave.visibility = View.GONE
            binding.btnSave.setOnClickListener(this)

            binding.btnNhsUrl.visibility = View.GONE

        } else {

            userConsentName = activityBundle.getString(Extra.USER_CONSENT_NAME.key) ?: Constants.EMPTY
            userConsentDate = activityBundle.getLong(Extra.USER_CONSENT_DATE.key)


            binding.progressText.visibility = View.VISIBLE
            binding.stepsLayout.visibility = View.VISIBLE
            binding.buttons.visibility = View.VISIBLE
            binding.btnSave.visibility = View.GONE
            binding.btnNhsUrl.visibility = View.VISIBLE

            binding.btnNext.setOnClickListener(this)
            binding.btnBack.setOnClickListener(this)
            binding.btnNhsUrl.setOnClickListener(this)
            binding.btnSexSelection.setOnClickListener(this)
            binding.btnDateBirth.setOnClickListener(this)


            binding.firstName.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (TextUtils.isEmpty(binding.firstName.textTrim)) {
                        binding.firstName.error = getString(R.string.value_not_set)
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            binding.lastName.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (TextUtils.isEmpty(binding.lastName.textTrim)) {
                        binding.lastName.error = getString(R.string.value_not_set)
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            binding.postcode.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (!TextUtils.isEmpty(binding.postcode.textTrim)) {
                        if (!Validator.validatePostcode(binding.postcode.textTrim)) {
                            binding.postcode.error = getString(R.string.value_not_admissible_postcode)
                        }
                    } else {
                        binding.postcode.error = getString(R.string.value_not_set)
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            binding.email.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (!TextUtils.isEmpty(binding.email.textTrim)) {
                        if (!Validator.validateMail(binding.email.textTrim)) {
                            binding.email.error = getString(R.string.value_not_admissible_email)
                        }
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            binding.phone.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (!TextUtils.isEmpty(binding.phone.textTrim)) {
                        if (!Validator.validatePhone(binding.phone.textTrim)) {
                            binding.phone.error = getString(R.string.value_not_admissible_phone)
                        }
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        Logger.d("Date of birth set")
        dateOfBirth = TimeUtils.getZeroSeconds(TimeUtils.getCurrent())
        dateOfBirth!!.set(Calendar.YEAR, year)
        dateOfBirth!!.set(Calendar.MONTH, month)
        dateOfBirth!!.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val ageInMillis = TimeUtils.getCurrent().timeInMillis - dateOfBirth!!.timeInMillis
        val age = ageInMillis / TimeUtils.ONE_DAY_MILLIS / 365


        if (age >= 18) {
            binding.btnDateBirth.setText(TimeUtils.format(dateOfBirth!!, Constants.DATE_FORMAT_YEAR_MONTH_DAY))
        } else {
            dateOfBirth = null
            UiUtils.showLongToast(this, "Age of 18 and above is required")
        }

    }


    @SuppressLint("SetJavaScriptEnabled")
    override fun onClick(v: View?) {

        when (v) {
            binding.btnNext -> {
                hideKeyboard()

                if (binding.insertIdEntryView.isComplete()
                    && !TextUtils.isEmpty(binding.firstName.textTrim)
                    && !TextUtils.isEmpty(binding.lastName.textTrim)
                    && dateOfBirth != null && sex != null
                    && Validator.validatePostcode(binding.postcode.textTrim)
                    && (Validator.validateMail(binding.email.textTrim) || Validator.validatePhone(binding.phone.textTrim))) {

                    if (!Validator.validateNhsNumber(binding.insertIdEntryView.getPin())) {
                        UiUtils.showShortToast(this, "NHS number is not valid")
                    } else {
                        register()
                    }
                } else {
                    UiUtils.showShortToast(this, R.string.error_patient_details)
                }
            }

            binding.btnBack -> {
                finish()
            }

            binding.btnSexSelection -> {
                hideKeyboard()
                val dialog = SelectSexDialog()
                dialog.isCancelable = true
                dialog.listener = object : SelectSexDialogListener {
                    override fun onSexSelected(sex: SexModel) {
                        dialog.dismiss()
                        this@PatientDetailsActivity.sex = sex
                        binding.btnSexSelection.setText(sex.sex)
                    }
                }
                dialog.show(supportFragmentManager, SelectSexDialog::javaClass.name)
            }

            binding.btnSave -> {
                hideKeyboard()
                if (!TextUtils.isEmpty(binding.firstName.textTrim) && !TextUtils.isEmpty(binding.lastName.textTrim)
                    && dateOfBirth != null && sex != null
                    && Validator.validatePostcode(binding.postcode.textTrim)
                    && (Validator.validateMail(binding.email.textTrim) || Validator.validatePhone(binding.phone.textTrim))) {


                    Preferences.user(this).userFirstName = binding.firstName.textTrim
                    Preferences.user(this).userLastName = binding.lastName.textTrim
                    Preferences.user(this).userDateOfBirth = dateOfBirth!!.timeInMillis
                    Preferences.user(this).userSex = sex!!.sex
                    Preferences.user(this).userPostcode = binding.postcode.textTrim
                    Preferences.user(this).userEmail = binding.email.textTrim
                    Preferences.user(this).userPhone = binding.phone.textTrim


                    UiUtils.showShortToast(this, R.string.success)

                    // do not upload data on firestore
                    /*
                    Preferences.lifecycle(this).userDetailsUploaded = false
                    FirestoreProvider.getInstance().updateUserDetails(this)
                    */

                    finish()

                } else {
                    UiUtils.showShortToast(this, R.string.error_patient_details)
                }
            }

            binding.btnNhsUrl -> {
                val bundle = Bundle()
                bundle.putString(Extra.WEB_VIEW_URL.key, getString(R.string.find_nhs_number_link_default))
                Router.getInstance().activityAnimation(ActivityAnimation.BOTTOM_TOP).startBaseActivity(this, Activities.WEB_VIEW, bundle)
            }

            binding.btnDateBirth -> {
                val cal = dateOfBirth ?: GregorianCalendar()
                val dialog = DatePickerDialog(this, R.style.AppCompatAlertDialogStyle, this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
                dialog.show()
                dialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
            }
        }


    }


    private fun register() {
        showProgressView()

        val request = UserRegistrationRequest()
        request.phoneModel = Utils.getPhoneModel()
        request.appVersion = Utils.getAppVersion(this)
        request.androidVersion = Utils.getAndroidVersion()
        request.userNhsNumber = binding.insertIdEntryView.getPin().toBigInteger()
        request.userFirstName = binding.firstName.textTrim
        request.userLastName = binding.lastName.textTrim
        request.userSex = sex!!.sex
        request.userPostcode = binding.postcode.textTrim
        request.userDob = dateOfBirth!!.timeInMillis
        request.userEmail = binding.email.textTrim
        request.userPhoneNumber = binding.phone.textTrim
        request.userIPAddress = Utils.getLocalIPAddress()
        request.registrationTimestamp = TimeUtils.getCurrent().timeInMillis
        request.userConsentName = userConsentName
        request.userConsentDate = userConsentDate

        UserManager.registerUser(this, request, object : UserRegistrationListener {
            override fun onSuccess(map: UserRegistrationMap) {
                if (map.dataItem.participantIdCounter.counter > 1) {
                    Logger.d("Already existing user with patient id ${binding.insertIdEntryView.getPin()}, ask for confirmation")
                    val dialog = ConfirmRegistrationDialog()
                    dialog.isCancelable = false
                    dialog.listener = object : ConfirmRegistrationDialogListener {
                        override fun onRegister() {
                            completeRegistration(map)
                        }

                        override fun onCancel() {
                            finish()
                        }
                    }
                    dialog.show(supportFragmentManager, ConfirmRegistrationDialog::javaClass.name)
                } else {
                    completeRegistration(map)
                }
            }

            override fun onError() {
                hideProgressView()
                UiUtils.showShortToast(this@PatientDetailsActivity, R.string.error)
            }
        })
    }

    private fun completeRegistration(map: UserRegistrationMap) {
        hideProgressView()
        Logger.d("User successfully registered with id ${map.dataItem.userId.id}")
        Preferences.user(this).idUser = map.dataItem.userId.id
        Preferences.user(this).userNhsNumber = binding.insertIdEntryView.getPin()
        Preferences.user(this).userFirstName = binding.firstName.textTrim
        Preferences.user(this).userLastName = binding.lastName.textTrim
        Preferences.user(this).userDateOfBirth = dateOfBirth!!.timeInMillis
        Preferences.user(this).userSex = sex!!.sex
        Preferences.user(this).userPostcode = binding.postcode.textTrim
        Preferences.user(this).userConsentDate = userConsentDate
        Preferences.user(this).userConsentName = userConsentName
        Preferences.lifecycle(this).userDetailsUploaded = true

        // automatically start the study
        Preferences.user(thiss).studyStarted = true
        Preferences.user(thiss).dateStudyStarted = TimeUtils.getCurrent().timeInMillis

        // check registration with the server
        TrackerManager.getInstance(this).saveUserRegistrationId(map.dataItem.userId.id)

        // do not upload data on firestore
        // FirestoreProvider.getInstance().updateUserDetails(this)

        val bundle = Bundle()
        bundle.putInt(Extra.SUCCESS_MESSAGE.key, SuccessMessageType.REGISTRATION.id)
        Router.getInstance().activityAnimation(ActivityAnimation.BOTTOM_TOP).startBaseActivity(thiss, Activities.SUCCESS_MESSAGE, bundle)
        finish()
    }
}