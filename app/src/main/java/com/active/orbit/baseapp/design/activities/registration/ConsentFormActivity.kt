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
import com.active.orbit.baseapp.core.deserialization.UserRegistrationMap
import com.active.orbit.baseapp.core.download.Download
import com.active.orbit.baseapp.core.enums.SuccessMessageType
import com.active.orbit.baseapp.core.firestore.providers.FirestoreProvider
import com.active.orbit.baseapp.core.listeners.UserRegistrationListener
import com.active.orbit.baseapp.core.managers.UserManager
import com.active.orbit.baseapp.core.permissions.Permissions
import com.active.orbit.baseapp.core.preferences.engine.Preferences
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.routing.enums.Extra
import com.active.orbit.baseapp.core.serialization.UserRegistrationRequest
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.core.utils.TimeUtils
import com.active.orbit.baseapp.core.utils.Utils
import com.active.orbit.baseapp.databinding.ActivityConsentFormBinding
import com.active.orbit.baseapp.design.activities.engine.Activities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation
import com.active.orbit.baseapp.design.dialogs.ConfirmRegistrationDialog
import com.active.orbit.baseapp.design.dialogs.listeners.ConfirmRegistrationDialogListener
import com.active.orbit.baseapp.design.recyclers.adapters.ConsentQuestionsAdapter
import com.active.orbit.baseapp.design.recyclers.listeners.ConsentQuestionListener
import com.active.orbit.baseapp.design.utils.UiUtils
import com.active.orbit.tracker.core.tracker.TrackerManager
import java.util.*
import kotlin.math.roundToInt

class ConsentFormActivity : BaseActivity(), View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private lateinit var binding: ActivityConsentFormBinding
    private var dateOfConsent: Calendar? = null
    private var fromMenu = false

    private var userNhsNumber = Constants.EMPTY
    private var userFirstName = Constants.EMPTY
    private var userLastName = Constants.EMPTY
    private var userDOB = Constants.INVALID.toLong()
    private var userSex = Constants.EMPTY
    private var userPostcode = Constants.EMPTY

    private var questionsAdapter: ConsentQuestionsAdapter? = null
    private var questionListener: ConsentQuestionListener? = null

    var questionsAcceptedCounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsentFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showBackButton()
        showLogoButton()

        fromMenu = activityBundle.getBoolean(Extra.FROM_MENU.key)


        prepare()


        val linearLayoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = linearLayoutManager

        questionsAdapter = ConsentQuestionsAdapter(this, questionListener)
        binding.recyclerView.adapter = questionsAdapter

        binding.recyclerView.isVerticalScrollBarEnabled = false
        binding.recyclerView.isNestedScrollingEnabled = false

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
            binding.btnDownload.visibility = View.VISIBLE
            binding.btnDownload.setOnClickListener(this)

        } else {
            userNhsNumber = activityBundle.getString(Extra.USER_NHS_NUMBER.key)!!
            userFirstName = activityBundle.getString(Extra.USER_FIRST_NAME.key)!!
            userLastName = activityBundle.getString(Extra.USER_LAST_NAME.key)!!
            userDOB = activityBundle.getLong(Extra.USER_DOB.key)
            userSex = activityBundle.getString(Extra.USER_SEX.key)!!
            userPostcode = activityBundle.getString(Extra.USER_POSTCODE.key)!!

            binding.progressText.visibility = View.VISIBLE
            binding.steps.visibility = View.VISIBLE
            binding.buttons.visibility = View.VISIBLE
            binding.btnDownload.visibility = View.GONE

            dateOfConsent = TimeUtils.getCurrent()
            binding.btnDate.setText(TimeUtils.format(dateOfConsent!!, Constants.DATE_FORMAT_YEAR_MONTH_DAY))


            binding.btnConfirm.setOnClickListener(this)
            binding.btnBack.setOnClickListener(this)

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
                //TODO compare counter with the size of the
                if (!TextUtils.isEmpty(binding.fullName.textTrim) && dateOfConsent != null && questionsAcceptedCounter == questionsAdapter!!.numberOfQuestions) {
                    register()
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


    private fun register() {
        showProgressView()

        val request = UserRegistrationRequest()
        request.phoneModel = Utils.getPhoneModel()
        request.appVersion = Utils.getAppVersion(this)
        request.androidVersion = Utils.getAndroidVersion()
        request.userNhsNumber = userNhsNumber.toBigInteger()
        request.userFirstName = userFirstName
        request.userLastName = userLastName
        request.userSex = userSex
        request.userPostcode = userPostcode
        request.userDob = userDOB
        request.userIPAddress = Utils.getLocalIPAddress()
        request.registrationTimestamp = TimeUtils.getCurrent().timeInMillis
        request.userConsentName = binding.fullName.textTrim
        request.userConsentDate = dateOfConsent!!.timeInMillis

        UserManager.registerUser(this, request, object : UserRegistrationListener {
            override fun onSuccess(map: UserRegistrationMap) {
                //TODO tell Prassana to include participantIDCounter
                if (map.dataItem.participantIdCounter.counter > 1) {
                    Logger.d("Already existing user with patient id $userNhsNumber, ask for confirmation")
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
                UiUtils.showShortToast(this@ConsentFormActivity, R.string.error)
            }
        })
    }

    private fun completeRegistration(map: UserRegistrationMap) {
        hideProgressView()
        Logger.d("User successfully registered with id ${map.dataItem.userId.id}")
        Preferences.user(this).idUser = map.dataItem.userId.id
        Preferences.user(this).userNhsNumber = userNhsNumber
        Preferences.user(this).userFirstName = userFirstName
        Preferences.user(this).userLastName = userLastName
        Preferences.user(this).userDateOfBirth = userDOB
        Preferences.user(this).userSex = userSex
        Preferences.user(this).userConsentDate = dateOfConsent!!.timeInMillis
        Preferences.user(this).userConsentName = binding.fullName.textTrim

        Preferences.lifecycle(this).userDetailsUploaded = false

        // check registration with the server
        TrackerManager.getInstance(this).saveUserRegistrationId(map.dataItem.userId.id)
        FirestoreProvider.getInstance().updateUserDetails(this)

        val bundle = Bundle()
        bundle.putInt(Extra.SUCCESS_MESSAGE.key, SuccessMessageType.REGISTRATION.id)
        Router.getInstance().activityAnimation(ActivityAnimation.BOTTOM_TOP).startBaseActivity(thiss, Activities.SUCCESS_MESSAGE, bundle)
        finish()
    }

}