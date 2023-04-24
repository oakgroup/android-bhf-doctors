package com.active.orbit.baseapp.design.dialogs

import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.design.dialogs.listeners.AdminAccessDialogListener
import com.active.orbit.baseapp.design.utils.DoneOnEditorActionListener
import com.active.orbit.baseapp.design.utils.UiUtils
import com.active.orbit.baseapp.design.widgets.BaseButton
import com.active.orbit.baseapp.design.widgets.BaseEditText

class AdminAccessDialog : DialogFragment() {

    var listener: AdminAccessDialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.dialog_admin_access, view as ViewGroup?, false)
        setup(view)

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("")
        builder.setView(view)
        return builder.create()
    }

    private fun setup(view: View) {
        val btnCancel = view.findViewById<BaseButton>(R.id.btnCancel)
        val btnConfirm = view.findViewById<BaseButton>(R.id.btnConfirm)
        val password = view.findViewById<BaseEditText>(R.id.password)

        password.setOnEditorActionListener(DoneOnEditorActionListener())

        btnCancel.setOnClickListener {
            listener?.onCancel()
            dismiss()
        }

        btnConfirm.setOnClickListener {
            val passwordText = password.textTrim
            if (TextUtils.isEmpty(passwordText) || !passwordText.equals(Constants.ADMIN_PASSWORD)) {
                UiUtils.showShortToast(requireContext(), R.string.admin_access_dialog_wrong_passwrod)
            } else {
                listener?.onConfirm()
                dismiss()
            }

        }
    }

    override fun onStart() {
        super.onStart()

        // make the background of the dialog transparent
        val window: Window? = dialog?.window
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}
