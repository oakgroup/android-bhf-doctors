package com.active.orbit.baseapp.design.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.DialogFragment
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.preferences.engine.Preferences
import com.active.orbit.baseapp.design.dialogs.listeners.DismissPatientDialogListener

class DismissPatientDialog : DialogFragment() {

    var listener: DismissPatientDialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.dialog_dismiss_patient, view as ViewGroup?, false)
        setup(view)

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("")
        builder.setView(view)
        return builder.create()
    }

    private fun setup(view: View) {
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)
        val btnDismiss = view.findViewById<Button>(R.id.btnDismiss)
        val layoutDetails = view.findViewById<LinearLayoutCompat>(R.id.layoutDetails)

        btnCancel.setOnClickListener {
            listener?.onCancel()
            dismiss()
        }

        btnDismiss.setOnClickListener {
            listener?.onDismiss()
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()

        // make the background of the dialog transparent
        val window: Window? = dialog?.window
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}
