package com.active.orbit.baseapp.design.activities.registration

import android.app.DatePickerDialog
import android.graphics.Paint
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.DatePicker
import androidx.core.content.ContextCompat
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.preferences.engine.Preferences
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.routing.enums.Extra
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.core.utils.TimeUtils
import com.active.orbit.baseapp.databinding.ActivityTermsConditionsBinding
import com.active.orbit.baseapp.design.activities.engine.Activities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation
import com.active.orbit.baseapp.design.utils.UiUtils
import java.util.*

class TermsAndConditionsActivity : BaseActivity(), View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private lateinit var binding: ActivityTermsConditionsBinding
    private var programID = Constants.EMPTY
    private var dateOfConsent: Calendar? = null
    private var fromMenu = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsConditionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showBackButton()
        showLogoButton()

        programID = activityBundle.getString(Extra.PROGRAM_ID.key)!!
        fromMenu = activityBundle.getBoolean(Extra.FROM_MENU.key)


        prepare()

    }


    private fun prepare() {

        binding.btnDate.setIcon(R.drawable.ic_calendar)
        binding.btnDate.setText(getString(R.string.date))
        binding.btnDate.disableClick()
        binding.termsLink.paintFlags = binding.termsLink.paintFlags or Paint.UNDERLINE_TEXT_FLAG


        if (fromMenu) {

            binding.fullName.isEnabled = false
            binding.fullName.setText(Preferences.user(this).userFullName())

            dateOfConsent = TimeUtils.getCurrent(Preferences.user(this).userDateOfConsent!!)
            binding.btnDate.setText(TimeUtils.format(dateOfConsent!!, Constants.DATE_FORMAT_YEAR_MONTH_DAY))

            binding.progressText.visibility = View.GONE
            binding.steps.visibility = View.GONE
            binding.buttons.visibility = View.GONE
            binding.btnDownload.visibility = View.VISIBLE
            binding.btnDownload.setOnClickListener(this)

        } else {
            binding.progressText.visibility = View.VISIBLE
            binding.steps.visibility = View.VISIBLE
            binding.buttons.visibility = View.VISIBLE
            binding.btnDownload.visibility = View.GONE

            binding.btnNext.setOnClickListener(this)
            binding.btnBack.setOnClickListener(this)
            binding.btnDate.setOnClickListener(this)

            binding.fullName.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (TextUtils.isEmpty(binding.fullName.textTrim)) {
                        binding.fullName.error = getString(R.string.value_not_set)
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }


    }

    override fun onPermissionEnabled(requestCode: Int) {
        super.onPermissionEnabled(requestCode)
        when (requestCode) {
            // TODO George compilation error
            /*
            Permissions.Group.ACCESS_DOWNLOAD_PDF.requestCode -> {
                val downloader = Download(this)
                //TODO replace with url for consent form
                downloader.downloadFile("https://www.africau.edu/images/default/sample.pdf", "application/pdf", "consent_form.pdf")
            }
            */

            else -> {
                Logger.e("Undefined request code $requestCode on permission enabled ")
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {

            binding.btnNext -> {
                if (!TextUtils.isEmpty(binding.fullName.textTrim) && dateOfConsent != null) {
                    // TODO George compilation error
                    // Preferences.user(this).userDateOfConsent = dateOfConsent!!.timeInMillis
                    val bundle = Bundle()
                    bundle.putString(Extra.PROGRAM_ID.key, programID)
                    Router.getInstance()
                        .activityAnimation(ActivityAnimation.LEFT_RIGHT)
                        .startBaseActivity(this, Activities.PATIENT_DETAILS, bundle)
                } else {
                    UiUtils.showLongToast(this, R.string.accept_toc_please)

                    // TODO George compilation error
                    // binding.scrollView.scrollToBottom()
                }
            }

            binding.btnBack -> finish()

            binding.termsLink -> Router.getInstance().openTermsAndConditions(this)

            binding.btnDate -> {
                val cal = dateOfConsent ?: GregorianCalendar()
                val dialog = DatePickerDialog(this, R.style.AppCompatAlertDialogStyle, this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
                dialog.datePicker.minDate = TimeUtils.getCurrent().timeInMillis
                dialog.datePicker.maxDate = TimeUtils.getCurrent().timeInMillis
                dialog.show()
                dialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
            }

            binding.btnDownload -> {
                // TODO George compilation error
                /*
                if (!hasDownloadPdfPermissionGranted()) {
                    requestPermissionDownloadPdf()
                } else {
                    val downloader = Download(this)
                    //TODO replace with url for consent form
                    downloader.downloadFile("https://www.africau.edu/images/default/sample.pdf", "application/pdf", "consent_form.pdf")
                }
                */
            }


        }
    }


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        Logger.d("Date of consent")
        dateOfConsent = TimeUtils.getZeroSeconds(TimeUtils.getCurrent())
        dateOfConsent!!.set(Calendar.YEAR, year)
        dateOfConsent!!.set(Calendar.MONTH, month)
        dateOfConsent!!.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        binding.btnDate.setText(TimeUtils.format(dateOfConsent!!, Constants.DATE_FORMAT_YEAR_MONTH_DAY))
    }

}