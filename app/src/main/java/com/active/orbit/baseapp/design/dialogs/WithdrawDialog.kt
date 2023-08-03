package com.active.orbit.baseapp.design.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.design.dialogs.listeners.WithdrawDialogListener

class WithdrawDialog : DialogFragment() {

    var listener: WithdrawDialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.dialog_withdraw, view as ViewGroup?, false)
        setup(view)

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("")
        builder.setView(view)
        return builder.create()
    }

    private fun setup(view: View) {
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)
        val btnDismiss = view.findViewById<Button>(R.id.btnDismiss)

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
