package com.active.orbit.baseapp.design.activities.registration

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.DatePicker
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.download.Download
import com.active.orbit.baseapp.core.permissions.Permissions
import com.active.orbit.baseapp.core.preferences.engine.Preferences
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.routing.enums.Extra
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.core.utils.TimeUtils
import com.active.orbit.baseapp.databinding.ActivityConsentFormBinding
import com.active.orbit.baseapp.design.activities.engine.Activities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation
import com.active.orbit.baseapp.design.recyclers.adapters.ConsentQuestionsAdapter
import com.active.orbit.baseapp.design.recyclers.listeners.ConsentQuestionListener
import com.active.orbit.baseapp.design.utils.UiUtils
import java.util.Calendar
import java.util.GregorianCalendar
import kotlin.math.roundToInt

class ConsentFormActivity : BaseActivity(), View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private lateinit var binding: ActivityConsentFormBinding
    private var dateOfConsent: Calendar? = null
    private var fromMenu = false

    private var questionsAdapter: ConsentQuestionsAdapter? = null
    private var questionListener: ConsentQuestionListener? = null

    var questionsAcceptedCounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsentFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showBackButton()

        fromMenu = activityBundle.getBoolean(Extra.FROM_MENU.key)


        prepare()

    }

    private fun prepare() {

        binding.btnDate.setIcon(R.drawable.ic_calendar)
        binding.btnDate.setText(getString(R.string.date))
        binding.btnDate.disableClick()

        binding.consentWebview.loadData(Preferences.user(this).consentFormText, "text/html; charset=utf-8", "UTF-8")


        if (fromMenu) {

            binding.fullName.isEnabled = false
            binding.fullName.setText(Preferences.user(this).userFullName())

            dateOfConsent = TimeUtils.getCurrent(Preferences.user(this).userConsentDate!!)
            binding.btnDate.setText(TimeUtils.format(dateOfConsent!!, Constants.DATE_FORMAT_YEAR_MONTH_DAY))

            binding.progressText.visibility = View.GONE
            binding.steps.visibility = View.GONE
            binding.buttons.visibility = View.GONE
            binding.btnDownload.visibility = View.GONE

            prepareQuestions(true)

        } else {
            binding.progressText.visibility = View.VISIBLE
            binding.steps.visibility = View.VISIBLE
            binding.buttons.visibility = View.VISIBLE
            binding.btnDownload.visibility = View.GONE
            binding.btnDownload.setOnClickListener(this)

            dateOfConsent = TimeUtils.getCurrent()
            binding.btnDate.setText(TimeUtils.format(dateOfConsent!!, Constants.DATE_FORMAT_YEAR_MONTH_DAY))


            binding.btnConfirm.setOnClickListener(this)
            binding.btnBack.setOnClickListener(this)

            prepareQuestions()

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

    private fun prepareQuestions(allAccepted: Boolean = false) {
        val linearLayoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = linearLayoutManager

        questionsAdapter = ConsentQuestionsAdapter(this, questionListener, allAccepted)
        binding.recyclerView.adapter = questionsAdapter

        questionsAdapter?.refresh(this)

        questionsAdapter?.listener = object : ConsentQuestionListener {

            override fun isAccepted(isAccepted: Boolean) {
                if (isAccepted) {
                    questionsAcceptedCounter += 1
                } else {
                    questionsAcceptedCounter -= 1
                }
            }
        }
    }

    override fun onPermissionEnabled(requestCode: Int) {
        super.onPermissionEnabled(requestCode)
        when (requestCode) {

            Permissions.Group.ACCESS_DOWNLOAD_PDF.requestCode -> {
                val downloader = Download(this)
                //TODO replace with url for consent form
                downloader.downloadFile("https://www.africau.edu/images/default/sample.pdf", "application/pdf", "consent_form.pdf")
            }

            else -> {
                Logger.e("Undefined request code $requestCode on permission enabled ")
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {

            binding.btnConfirm -> {
                if (!TextUtils.isEmpty(binding.fullName.textTrim) && dateOfConsent != null && questionsAcceptedCounter == questionsAdapter!!.numberOfQuestions) {
                    val bundle = Bundle()
                    bundle.putString(Extra.USER_CONSENT_NAME.key, binding.fullName.textTrim)
                    bundle.putLong(Extra.USER_CONSENT_DATE.key, dateOfConsent!!.timeInMillis)

                    Router.getInstance()
                        .activityAnimation(ActivityAnimation.LEFT_RIGHT)
                        .startBaseActivity(this, Activities.ON_BOARDING_LOCATION, bundle)
                } else {
                    UiUtils.showLongToast(this, R.string.accept_toc_please)
                    binding.scrollView.scrollTo(binding.termsLinkContainer.x.roundToInt(), binding.termsLinkContainer.y.roundToInt())
                }
            }

            binding.btnBack -> finish()

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

                if (!hasDownloadPdfPermissionGranted()) {
                    requestPermissionDownloadPdf()
                } else {
                    val downloader = Download(this)
                    //TODO replace with url for consent form
                    downloader.downloadFile("https://www.africau.edu/images/default/sample.pdf", "application/pdf", "consent_form.pdf")
                }
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