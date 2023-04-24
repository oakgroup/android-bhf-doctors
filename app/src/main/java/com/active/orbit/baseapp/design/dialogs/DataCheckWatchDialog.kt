package com.active.orbit.baseapp.design.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.design.widgets.BaseTextView

class DataCheckWatchDialog : DialogFragment() {

    companion object {
        const val ARGUMENT_DATA_AMOUNT = "arg_data_amount"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.dialog_data_check_watch, view as ViewGroup?, false)
        setup(view, arguments?.getString(ARGUMENT_DATA_AMOUNT) ?: Constants.EMPTY)

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("")
        builder.setView(view)
        return builder.create()
    }

    @SuppressLint("StringFormatInvalid")
    private fun setup(view: View, data: String) {
        val btnClose = view.findViewById<Button>(R.id.btnClose)
        val description = view.findViewById<BaseTextView>(R.id.description)

        btnClose.setOnClickListener {
            dismiss()
        }


        description.text = getString(R.string.watch_upload_dialog_description, data)


    }

    override fun onStart() {
        super.onStart()

        // make the background of the dialog transparent
        val window: Window? = dialog?.window
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}
