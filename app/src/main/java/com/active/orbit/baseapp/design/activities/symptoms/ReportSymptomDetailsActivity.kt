package com.active.orbit.baseapp.design.activities.symptoms

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.database.models.DBSeverity
import com.active.orbit.baseapp.core.database.models.DBSymptom
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.utils.Constants
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
        const val EXTRA_SYMPTOM_SEVERITY = "symptom_severity"
        const val EXTRA_SYMPTOM_DETAILS = "symptom_details"
    }

    private lateinit var binding: ActivityReportSymptomDetailsBinding
    var symptom = DBSymptom()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportSymptomDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showBackButton()
        showLogoButton()

        prepare()
    }

    private fun prepare() {

        binding.btnNext.setOnClickListener(this)

        binding.btnSymptoms.setOnClickListener(this)
        binding.btnSeverity.setOnClickListener(this)

        binding.btnSymptoms.setIcon(R.drawable.ic_dropdown)
        binding.btnSymptoms.setText(getString(R.string.select))
        binding.btnSymptoms.disableClick()

        binding.btnSeverity.setIcon(R.drawable.ic_dropdown)
        binding.btnSeverity.setText(getString(R.string.select))
        binding.btnSeverity.disableClick()


        binding.edtMoreDetails.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                symptom.symptomDetails = p0.toString().trim()
            }
        })

    }


    override fun onClick(v: View?) {
        when (v) {

            binding.btnNext -> {
                hideKeyboard()
                if (isSelectionValid()) {
                    if (symptom.symptomDetails == null) {
                        symptom.symptomDetails = Constants.EMPTY
                    }
                    val bundle = Bundle()
                    bundle.putString(EXTRA_SYMPTOM_ID, symptom.idSymptom)
                    bundle.putString(EXTRA_SYMPTOM_SEVERITY, symptom.severity?.idSeverity)
                    bundle.putString(EXTRA_SYMPTOM_DETAILS, symptom.symptomDetails)

                    Router.getInstance().activityAnimation(ActivityAnimation.LEFT_RIGHT).startBaseActivity(this, Activities.REPORT_SYMPTOM_DATE_TIME, bundle)
                } else {
                    UiUtils.showShortToast(this, R.string.symptom_select_something)
                }
            }


            binding.btnSymptoms -> {
                Utils.hideKeyboard(binding.edtMoreDetails)
                val dialog = SelectSymptomDialog()
                dialog.isCancelable = true
                dialog.listener = object : SelectSymptomDialogListener {
                    override fun onSymptomSelect(symptom: DBSymptom) {
                        dialog.dismiss()
                        this@ReportSymptomDetailsActivity.symptom = symptom
                        binding.btnSymptoms.setText(this@ReportSymptomDetailsActivity.symptom.question ?: Constants.EMPTY)
                    }
                }
                dialog.show(supportFragmentManager, SelectSymptomDialog::javaClass.name)
            }

            binding.btnSeverity -> {
                Utils.hideKeyboard(binding.edtMoreDetails)

                if (symptom.idSymptom != Constants.EMPTY) {
                    val arguments = Bundle()
                    arguments.putString(SelectSeverityDialog.ARGUMENT_ID_SYMPTOM, symptom.idSymptom)
                    val dialog = SelectSeverityDialog()
                    dialog.isCancelable = true
                    dialog.arguments = arguments
                    dialog.listener = object : SelectSeverityDialogListener {
                        override fun onSeveritySelected(severity: DBSeverity) {
                            dialog.dismiss()
                            symptom.severity = severity
                            binding.btnSeverity.setText(symptom.severity?.symptomResponse ?: Constants.EMPTY)
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
        return (symptom.idSymptom != Constants.EMPTY && symptom.severitySelected())

    }
}
