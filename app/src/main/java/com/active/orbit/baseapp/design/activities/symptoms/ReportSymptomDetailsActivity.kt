package com.active.orbit.baseapp.design.activities.symptoms

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.database.models.DBReportSymptom
import com.active.orbit.baseapp.core.database.models.DBSeverity
import com.active.orbit.baseapp.core.database.models.DBSymptom
import com.active.orbit.baseapp.core.database.tables.TableReportedSymptoms
import com.active.orbit.baseapp.core.database.tables.TableSymptoms
import com.active.orbit.baseapp.core.preferences.engine.Preferences
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.core.utils.ThreadHandler.backgroundThread
import com.active.orbit.baseapp.core.utils.ThreadHandler.mainThread
import com.active.orbit.baseapp.core.utils.TimeUtils
import com.active.orbit.baseapp.core.utils.Utils
import com.active.orbit.baseapp.databinding.ActivityReportSymptomDetailsBinding
import com.active.orbit.baseapp.design.activities.engine.Activities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation
import com.active.orbit.baseapp.design.dialogs.SelectSeverityDialog
import com.active.orbit.baseapp.design.dialogs.SelectSymptomDialog
import com.active.orbit.baseapp.design.dialogs.listeners.SelectSeverityDialogListener
import com.active.orbit.baseapp.design.dialogs.listeners.SelectSymptomDialogListener
import com.active.orbit.baseapp.design.utils.UiUtils

class ReportSymptomDetailsActivity : BaseActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_SYMPTOM_ID = "symptom_id"
        const val EXTRA_SYMPTOM_NAME = "symptom_name"
        const val EXTRA_SYMPTOM_SEVERITY = "symptom_severity"
        const val EXTRA_SYMPTOM_DETAILS = "symptom_details"
    }

    private lateinit var binding: ActivityReportSymptomDetailsBinding
    private var reportedSymptom: DBReportSymptom? = null

    var symptomName: String = Constants.EMPTY
    var symptomSeverity: String = Constants.EMPTY
    var symptomDetails: String = Constants.EMPTY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportSymptomDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showBackButton()
        showLogoButton()


        backgroundThread {
            val modelId = activityBundle.getString(EXTRA_SYMPTOM_ID, Constants.EMPTY)
            if (modelId != Constants.EMPTY) {
                reportedSymptom = TableReportedSymptoms.getById(this, modelId)
                if (reportedSymptom?.isValid() != true) {
                    mainThread {
                        Logger.e("Model is not valid on on ${javaClass.name}")
                        UiUtils.showShortToast(this, R.string.symptom_show_error)
                        finish()
                    }
                } else {
                    mainThread {
                        prepare()
                    }
                }
            } else {
                mainThread {
                    prepare()
                }
            }
        }

        prepare()
    }

    private fun prepare() {

        binding.btnSymptoms.setIcon(R.drawable.ic_dropdown)
        binding.btnSeverity.setIcon(R.drawable.ic_dropdown)
        binding.btnDate.setIcon(R.drawable.ic_dropdown)
        binding.btnTime.setIcon(R.drawable.ic_dropdown)

        binding.btnSymptoms.disableClick()
        binding.btnSeverity.disableClick()
        binding.btnDate.disableClick()
        binding.btnTime.disableClick()


        if (reportedSymptom == null) {
            showAddSymptomLayout()
        } else {
            showViewSymptomLayout()
        }


    }

    private fun showAddSymptomLayout(){
        binding.title.text = getString(R.string.symptom_details_add)
        binding.dateTimeLayout.visibility = GONE
        binding.progressText.visibility = VISIBLE
        binding.steps.visibility = VISIBLE

        binding.btnNext.setOnClickListener(this)
        binding.btnBack.setOnClickListener(this)

        binding.btnSymptoms.setText(getString(R.string.select))
        binding.btnSymptoms.setOnClickListener(this)
        binding.btnSeverity.setText(getString(R.string.select))
        binding.btnSeverity.setOnClickListener(this)

        binding.edtMoreDetails.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                symptomDetails = p0.toString().trim()
            }
        })
    }

    private fun showViewSymptomLayout(){
        binding.title.text = getString(R.string.symptom_details)
        binding.dateTimeLayout.visibility = VISIBLE
        binding.progressText.visibility = GONE
        binding.steps.visibility = GONE

        binding.btnNext.visibility = GONE
        binding.btnBack.setOnClickListener(this)


        binding.btnSymptoms.setText(reportedSymptom!!.symptomName)
        binding.btnSeverity.setText(reportedSymptom!!.symptomSeverity)
        binding.edtMoreDetails.setText(reportedSymptom!!.symptomDetails)


        if(reportedSymptom!!.symptomDateTime != 0L) {
            val date = TimeUtils.format(TimeUtils.getCurrent(reportedSymptom!!.symptomDateTime), Constants.DATE_FORMAT_YEAR_MONTH_DAY)
            binding.btnDate.setText(date)
            val time = TimeUtils.format(TimeUtils.getCurrent(reportedSymptom!!.symptomDateTime), Constants.DATE_FORMAT_HOUR_MINUTE)
            binding.btnTime.setText(time)
        }

        binding.btnSymptoms.disableTouch()
        binding.btnSeverity.disableTouch()
        binding.btnDate.disableTouch()
        binding.btnTime.disableTouch()
        binding.btnSymptoms.isEnabled = false
        binding.btnSeverity.isEnabled = false
        binding.btnDate.isEnabled = false
        binding.btnTime.isEnabled = false
        binding.edtMoreDetails.isEnabled = false


    }


    override fun onClick(v: View?) {
        when (v) {

            binding.btnBack -> {
                finish()
            }

            binding.btnNext -> {
                hideKeyboard()
                if (isSelectionValid()) {
                    val bundle = Bundle()
                    bundle.putString(EXTRA_SYMPTOM_NAME, symptomName)
                    bundle.putString(EXTRA_SYMPTOM_SEVERITY, symptomSeverity)
                    bundle.putString(EXTRA_SYMPTOM_DETAILS, symptomDetails)

                    Router.getInstance().activityAnimation(ActivityAnimation.LEFT_RIGHT).startBaseActivityForResult(this, Activities.REPORT_SYMPTOM_DATE_TIME, bundle, SymptomsActivity.SYMPTOM_REQUEST_CODE)
                } else {
                    UiUtils.showShortToast(this, R.string.symptom_select_something)
                }
            }


            binding.btnSymptoms -> {
                Utils.hideKeyboard(binding.edtMoreDetails)
                val dialog = SelectSymptomDialog()
                dialog.isCancelable = true
                dialog.listener = object : SelectSymptomDialogListener {
                    override fun onSymptomSelect(symptom: String) {
                        dialog.dismiss()
                        this@ReportSymptomDetailsActivity.symptomName = symptom
                        binding.btnSymptoms.setText(this@ReportSymptomDetailsActivity.symptomName)
                    }
                }
                dialog.show(supportFragmentManager, SelectSymptomDialog::javaClass.name)
            }

            binding.btnSeverity -> {
                Utils.hideKeyboard(binding.edtMoreDetails)

                if (symptomName != Constants.EMPTY) {
                    val arguments = Bundle()
                    val dialog = SelectSeverityDialog()
                    dialog.isCancelable = true
                    dialog.arguments = arguments
                    dialog.listener = object : SelectSeverityDialogListener {
                        override fun onSeveritySelected(severity: String) {
                            dialog.dismiss()
                            symptomSeverity = severity
                            binding.btnSeverity.setText(symptomSeverity)
                        }
                    }
                    dialog.show(supportFragmentManager, SelectSeverityDialog::javaClass.name)
                } else {
                    UiUtils.showShortToast(this, R.string.symptom_select)
                }
            }

        }
    }

    private fun isSelectionValid(): Boolean {
        return (symptomName!= Constants.EMPTY && symptomSeverity!= Constants.EMPTY)

    }
}
