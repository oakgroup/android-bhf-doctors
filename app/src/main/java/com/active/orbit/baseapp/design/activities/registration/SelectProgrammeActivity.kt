package com.active.orbit.baseapp.design.activities.registration

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.database.models.DBProgram
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.routing.enums.Extra
import com.active.orbit.baseapp.databinding.ActivitySelectProgrammeBinding
import com.active.orbit.baseapp.design.activities.engine.Activities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation
import com.active.orbit.baseapp.design.dialogs.SelectProgrammeDialog
import com.active.orbit.baseapp.design.dialogs.listeners.SelectProgrammeDialogListener
import com.active.orbit.baseapp.design.utils.UiUtils

class SelectProgrammeActivity : BaseActivity(), View.OnClickListener{

    private lateinit var binding: ActivitySelectProgrammeBinding

    private var programSelected: DBProgram? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectProgrammeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showBackButton()
        showLogoButton()

        prepare()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun prepare() {

        binding.btnProgramme.setIcon(R.drawable.ic_dropdown)
        binding.btnProgramme.setText(getString(R.string.select))
        binding.btnProgramme.disableClick()

        binding.btnNext.setOnClickListener(this)
        binding.btnBack.setOnClickListener(this)
        binding.btnProgramme.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v) {

            binding.btnNext -> {
                if (programSelected != null) {
                    val dbProgram = programSelected
                    if (dbProgram?.isValid() == true) {
                        val bundle = Bundle()
                        bundle.putString(Extra.PROGRAM_ID.key, dbProgram.identifier())
                        Router.getInstance()
                            .activityAnimation(ActivityAnimation.LEFT_RIGHT)
                            .startBaseActivity(this, Activities.TERMS_AND_CONDITIONS, bundle)
                    } else {
                        UiUtils.showLongToast(this, R.string.programme_empty)
                    }
                } else {
                    UiUtils.showLongToast(this, R.string.programme_empty)
                }
            }

            binding.btnBack -> finish()

            binding.btnProgramme -> {
                val dialog = SelectProgrammeDialog()
                dialog.isCancelable = true
                dialog.listener = object : SelectProgrammeDialogListener {
                    override fun onProgrammeSelect(programme: DBProgram) {
                        dialog.dismiss()
                        programSelected = programme
                        binding.btnProgramme.setText(programme.name)
                    }
                }
                dialog.show(supportFragmentManager, SelectProgrammeDialog::javaClass.name)
            }
        }
    }


}