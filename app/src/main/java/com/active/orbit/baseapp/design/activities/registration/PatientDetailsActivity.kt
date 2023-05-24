package com.active.orbit.baseapp.design.activities.registration

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.DatePicker
import androidx.core.content.ContextCompat
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.firestore.providers.FirestoreProvider
import com.active.orbit.baseapp.core.preferences.engine.Preferences
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.routing.enums.Extra
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.core.utils.TimeUtils
import com.active.orbit.baseapp.databinding.ActivityPatientDetailsBinding
import com.active.orbit.baseapp.design.activities.engine.Activities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation
import com.active.orbit.baseapp.design.dialogs.SelectSexDialog
import com.active.orbit.baseapp.design.dialogs.listeners.SelectSexDialogListener
import com.active.orbit.baseapp.design.recyclers.models.SexModel
import com.active.orbit.baseapp.design.utils.UiUtils
import java.util.*

class PatientDetailsActivity : BaseActivity(), View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private lateinit var binding: ActivityPatientDetailsBinding

    private var programID = Constants.EMPTY
    private var sex: SexModel? = null
    private var dateOfBirth: Calendar? = null
    private var fromMenu = false

    companion object {
        private const val HEIGHT_MIN = 50
        private const val HEIGHT_MAX = 250
        private const val WEIGHT_MIN = 10
        private const val WEIGHT_MAX = 400
        private const val AGE_MIN = 18
        private const val AGE_MAX = 120
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showBackButton()
        showLogoButton()


        if (activityBundle.getString(Extra.PROGRAM_ID.key) != null) {
            programID = activityBundle.getString(Extra.PROGRAM_ID.key)!!
        }

        if (activityBundle.getBoolean(Extra.FROM_MENU.key) != null) {
            fromMenu = activityBundle.getBoolean(Extra.FROM_MENU.key)
        }

        prepare()
    }

    private fun prepare() {

        binding.btnSexSelection.setIcon(R.drawable.ic_dropdown)
        binding.btnSexSelection.setText(getString(R.string.sex))
        binding.btnSexSelection.disableClick()

        binding.btnDateBirth.setIcon(R.drawable.ic_calendar)
        binding.btnDateBirth.setText(getString(R.string.date))
        binding.btnDateBirth.disableClick()


        if (fromMenu) {
            binding.nhsNumberTitle.text = getString(R.string.patient_id)
            binding.insertIdEntryView.isEnabled = false
            binding.insertIdEntryView.setPin(Preferences.user(this).userNhsNumber)
            binding.layoutID.setOnClickListener { UiUtils.showShortToast(this, "NHS number cannot change") }
            val sexModel = SexModel(Preferences.user(this).userSex!!)
            this@PatientDetailsActivity.sex = sexModel
            binding.btnSexSelection.setText(Preferences.user(this).userSex)
            binding.ageSelection.setText(Preferences.user(this).userAge)
            binding.weightSelection.setText(Preferences.user(this).userWeight)
            binding.heightSelection.setText(Preferences.user(this).userHeight)

            binding.firstName.setText(Preferences.user(this).userFirstName)
            binding.lastName.setText(Preferences.user(this).userLastName)

            dateOfBirth = TimeUtils.getCurrent(Preferences.user(this).userDateOfBirth!!)
            binding.btnDateBirth.setText(TimeUtils.format(dateOfBirth!!, Constants.DATE_FORMAT_YEAR_MONTH_DAY))


            binding.progressText.visibility = View.GONE
            binding.stepsLayout.visibility = View.GONE
            binding.buttons.visibility = View.GONE
            binding.btnSave.visibility = View.VISIBLE
            binding.btnSave.setOnClickListener(this)

            binding.btnNhsUrl.visibility = View.GONE

        } else {
            binding.progressText.visibility = View.VISIBLE
            binding.stepsLayout.visibility = View.VISIBLE
            binding.buttons.visibility = View.VISIBLE
            binding.btnSave.visibility = View.GONE
            binding.btnNhsUrl.visibility = View.VISIBLE

            binding.btnNext.setOnClickListener(this)
            binding.btnBack.setOnClickListener(this)
            binding.btnNhsUrl.setOnClickListener(this)

        }

        binding.btnSexSelection.setOnClickListener(this)
        binding.btnDateBirth.setOnClickListener(this)


        binding.ageSelection.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!TextUtils.isEmpty(binding.ageSelection.textTrim)) {
                    val ageX = Integer.parseInt(binding.ageSelection.textTrim)
                    when {
                        ageX < AGE_MIN -> binding.ageSelection.error =
                            getString(R.string.value_not_admissible)
                        ageX > AGE_MAX -> binding.ageSelection.error =
                            getString(R.string.value_not_admissible)
                        else -> binding.ageSelection.error = null
                    }
                } else binding.ageSelection.error = getString(R.string.value_not_set)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.weightSelection.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!TextUtils.isEmpty(binding.weightSelection.textTrim)) {
                    val weightX = Integer.parseInt(binding.weightSelection.textTrim)
                    when {
                        weightX < WEIGHT_MIN -> binding.weightSelection.error = getString(R.string.value_not_admissible)
                        weightX > WEIGHT_MAX -> binding.weightSelection.error = getString(R.string.value_not_admissible)
                        else -> binding.weightSelection.error = null
                    }
                } else binding.weightSelection.error = getString(R.string.value_not_set)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.heightSelection.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!TextUtils.isEmpty(binding.heightSelection.textTrim)) {
                    val heightX = Integer.parseInt(binding.heightSelection.textTrim)
                    when {
                        heightX < HEIGHT_MIN -> binding.heightSelection.error =
                            getString(R.string.value_not_admissible)
                        heightX > HEIGHT_MAX -> binding.heightSelection.error =
                            getString(R.string.value_not_admissible)
                        else -> binding.heightSelection.error = null
                    }
                } else binding.heightSelection.error = getString(R.string.value_not_set)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

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


    override fun onClick(v: View?) {

        when (v) {
            binding.btnNext -> {
                hideKeyboard()
                if (binding.insertIdEntryView.isComplete() && !TextUtils.isEmpty(binding.firstName.textTrim) && !TextUtils.isEmpty(binding.lastName.textTrim) && dateOfBirth != null) {
                    val bundle = Bundle()
                    bundle.putString(Extra.PROGRAM_ID.key, programID)
                    bundle.putString(Extra.USER_NHS_NUMBER.key, binding.insertIdEntryView.getPin())
                    bundle.putString(Extra.USER_FIRST_NAME.key, binding.firstName.textTrim)
                    bundle.putString(Extra.USER_LAST_NAME.key, binding.lastName.textTrim)
                    bundle.putLong(Extra.USER_DOB.key, dateOfBirth!!.timeInMillis)

                    Router.getInstance()
                        .activityAnimation(ActivityAnimation.LEFT_RIGHT)
                        .startBaseActivity(this, Activities.PATIENT_REGISTER, bundle)
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
                if (!TextUtils.isEmpty(binding.firstName.textTrim) && !TextUtils.isEmpty(binding.lastName.textTrim) && dateOfBirth != null) {
                    Preferences.user(this).userFirstName = binding.firstName.textTrim
                    Preferences.user(this).userLastName = binding.lastName.textTrim
                    Preferences.user(this).userDateOfBirth = dateOfBirth!!.timeInMillis
                    UiUtils.showShortToast(this, R.string.success)

                    Preferences.lifecycle(this).userDetailsUploaded = false
                    FirestoreProvider.getInstance().updateUserDetails(this)

                    finish()

                } else {
                    UiUtils.showShortToast(this, R.string.error_patient_details)
                }
            }

            binding.btnNhsUrl -> {
                Router.getInstance().openNHS(this)
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
}