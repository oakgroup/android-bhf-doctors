package com.active.orbit.baseapp.design.activities.symptoms

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.core.content.ContextCompat
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.database.models.DBReportSymptom
import com.active.orbit.baseapp.core.database.models.DBSymptom
import com.active.orbit.baseapp.core.database.tables.TableReportedSymptoms
import com.active.orbit.baseapp.core.preferences.engine.Preferences
import com.active.orbit.baseapp.core.routing.enums.ResultCode
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.core.utils.ThreadHandler.backgroundThread
import com.active.orbit.baseapp.core.utils.ThreadHandler.mainThread
import com.active.orbit.baseapp.core.utils.TimeUtils
import com.active.orbit.baseapp.databinding.ActivityReportSymptomTimeBinding
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.utils.UiUtils
import java.util.*

class ReportSymptomTimeActivity : BaseActivity(), View.OnClickListener, DatePickerDialog.OnDateSetListener {


    private lateinit var binding: ActivityReportSymptomTimeBinding
    var symptom: DBSymptom? = null
    var symptomToReport = DBReportSymptom()
    private var symptomDate: Calendar? = null
    private var symptomTime: Calendar? = null
    private var symptomName: String? = null
    private var symptomSeverity: String? = null
    private var symptomDetails: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportSymptomTimeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showBackButton()
        showLogoButton()

        symptomName = activityBundle.getString(ReportSymptomDetailsActivity.EXTRA_SYMPTOM_NAME)!!
        symptomSeverity = activityBundle.getString(ReportSymptomDetailsActivity.EXTRA_SYMPTOM_SEVERITY)!!
        symptomDetails = activityBundle.getString(ReportSymptomDetailsActivity.EXTRA_SYMPTOM_DETAILS)!!


        symptomToReport.symptomName = symptomName
        symptomToReport.symptomSeverity = symptomSeverity
        symptomToReport.symptomDetails = symptomDetails


        prepare()


    }

    private fun prepare() {

        binding.btnNow.paintFlags = binding.btnNow.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        binding.btnReport.setOnClickListener(this)
        binding.btnBack.setOnClickListener(this)
        binding.btnNow.setOnClickListener(this)


        binding.btnDate.setOnClickListener(this)
        binding.btnTime.setOnClickListener(this)

        binding.btnDate.setIcon(R.drawable.ic_calendar)
        binding.btnDate.setText(getString(R.string.date))
        binding.btnDate.disableClick()
        binding.btnTime.setIcon(R.drawable.ic_time)
        binding.btnTime.setText(getString(R.string.min))
        binding.btnTime.disableClick()

    }


    override fun onClick(v: View?) {
        when (v) {

            binding.btnReport -> {
                if (symptomDate != null && symptomTime != null) {
                    symptomToReport.symptomId = TimeUtils.getCurrent().timeInMillis
                    val symptomDateTime = TimeUtils.getZeroSeconds(TimeUtils.getCurrent())
                    symptomDateTime.set(Calendar.YEAR, symptomDate!!.get(Calendar.YEAR))
                    symptomDateTime.set(Calendar.MONTH, symptomDate!!.get(Calendar.MONTH))
                    symptomDateTime.set(Calendar.DAY_OF_MONTH, symptomDate!!.get(Calendar.DAY_OF_MONTH))
                    symptomDateTime.set(Calendar.MINUTE, symptomTime!!.get(Calendar.MINUTE))
                    symptomDateTime.set(Calendar.HOUR_OF_DAY, symptomTime!!.get(Calendar.HOUR_OF_DAY))
                    symptomToReport.symptomDateTime = symptomDateTime.timeInMillis
                    symptomToReport.symptomTimestamp = TimeUtils.getCurrent().timeInMillis
                    sendData()
                } else {
                    UiUtils.showShortToast(this, R.string.symptom_date_time)
                }
            }

            binding.btnBack -> {
                finish()
            }


            binding.btnDate -> {
                val cal = symptomDate ?: GregorianCalendar()
                val dialog = DatePickerDialog(this, R.style.AppCompatAlertDialogStyle, this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
                dialog.datePicker.minDate = Preferences.user(this).dateStudyStarted!!
                dialog.show()
                dialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
            }
            binding.btnTime -> {
                var hour = 0
                var minute = 0
                if (symptomTime != null) {
                    val cal = symptomTime ?: GregorianCalendar()
                    hour = cal.get(Calendar.HOUR_OF_DAY)
                    minute = cal.get(Calendar.MINUTE)
                }

                val dialog = TimePickerDialog(this, R.style.AppCompatAlertDialogStyle, { _, hourOfDay, minuteOfHour ->
                    symptomTime = TimeUtils.getZeroSeconds(TimeUtils.getCurrent())
                    symptomTime!!.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    symptomTime!!.set(Calendar.MINUTE, minuteOfHour)
                    binding.btnTime.setText(TimeUtils.format(symptomTime!!, Constants.DATE_FORMAT_HOUR_MINUTE))

                }, hour, minute, true)
                dialog.show()
                dialog.getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                dialog.getButton(TimePickerDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
            }

            binding.btnNow -> {
                symptomDate = TimeUtils.getZeroSeconds(TimeUtils.getCurrent())
                symptomTime = TimeUtils.getZeroSeconds(TimeUtils.getCurrent())
                binding.btnDate.setText(TimeUtils.format(symptomDate!!, Constants.DATE_FORMAT_YEAR_MONTH_DAY))
                binding.btnTime.setText(TimeUtils.format(symptomTime!!, Constants.DATE_FORMAT_HOUR_MINUTE))
            }


        }
    }


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        Logger.d("Medicine date set")
        symptomDate = TimeUtils.getZeroSeconds(TimeUtils.getCurrent())
        symptomDate!!.set(Calendar.YEAR, year)
        symptomDate!!.set(Calendar.MONTH, month)
        symptomDate!!.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        binding.btnDate.setText(TimeUtils.format(symptomDate!!, Constants.DATE_FORMAT_YEAR_MONTH_DAY))
    }

    private fun sendData() {
        showProgressView()

        backgroundThread {
            TableReportedSymptoms.upsert(this, symptomToReport)
            mainThread {
                UiUtils.showShortToast(this@ReportSymptomTimeActivity, getString(R.string.success_symptom_report))
                setResult(ResultCode.RESULT_OK.value)
                finish()
            }
        }
        hideProgressView()

        //TODO
//        val symptomsToUpload = ArrayList<DBSymptom>()
//        symptomsToUpload.add(symptom!!)
//
//
//        SymptomsManager.uploadSymptoms(this, symptomsToUpload, object : ResultListener {
//            override fun onResult(success: Boolean) {
//                hideProgressView()
//                if (success) {
//                    mainThread {
//                        UiUtils.showShortToast(this@ReportSymptomTimeActivity, getString(R.string.success_symptom_report))
//                        setResult(SymptomsActivity.SYMPTOM_RESULT_CODE_UPDATED)
//                        finish()
//                    }
//                } else {
//                    UiUtils.showShortToast(this@ReportSymptomTimeActivity, R.string.error)
//                }
//            }
//        })
    }

}
