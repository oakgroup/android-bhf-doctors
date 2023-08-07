package com.active.orbit.baseapp.design.dialogs

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.permissions.Permissions
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.design.dialogs.engine.BaseDialogFragment
import com.active.orbit.baseapp.design.dialogs.listeners.PermissionsDialogListener
import com.active.orbit.baseapp.design.widgets.BaseButton
import com.active.orbit.baseapp.design.widgets.BaseTextView

class PermissionsDialog : BaseDialogFragment() {

    var listener: PermissionsDialogListener? = null
    private var requestCode: Int? = null

    companion object {
        const val REQUEST_CODE = "request_code"
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.dialog_permissions, view as ViewGroup?, false)
        requestCode = arguments?.getInt(REQUEST_CODE)

        setup(view)

        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(Constants.EMPTY)
        builder.setView(view)
        return builder.create()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun setup(view: View) {
        val title = view.findViewById<BaseTextView>(R.id.title)
        val description = view.findViewById<BaseTextView>(R.id.description)
        val btnPermission = view.findViewById<BaseButton>(R.id.btnPermission)
        val btnCancel = view.findViewById<BaseButton>(R.id.btnCancel)

        when (requestCode) {
            Permissions.Group.ACCESS_FINE_LOCATION.requestCode -> {
                title.text = getString(R.string.location_services)
                description.text = getString(R.string.permissions_location_dialog_title)
            }
            Permissions.Group.ACCESS_BACKGROUND_LOCATION.requestCode -> {
                title.text = getString(R.string.location_services)
                description.text = getString(R.string.permissions_background_location_dialog_title)
            }
            Permissions.Group.ACCESS_EXTERNAL_STORAGE.requestCode -> {
                description.text = getString(R.string.permissions_external_storage_dialog_title)
            }
            Permissions.Group.ACCESS_ACTIVITY_RECOGNITION.requestCode -> {
                description.text = getString(R.string.permissions_activity_recognition_dialog_title)
            }
            Permissions.Group.ACCESS_CAMERA_FOR_SCAN.requestCode -> {
                description.text = getString(R.string.permissions_camera_dialog_title)
            }
            Permissions.Group.ACCESS_CAMERA_FOR_CAPTURE.requestCode -> {
                description.text = getString(R.string.permissions_camera_capture_dialog_title)
            }
            Permissions.Group.ACCESS_DOWNLOAD_PDF.requestCode -> {
                description.text = getString(R.string.permissions_download_pdf_title)
            }

        }

        btnPermission.setOnClickListener {
            listener?.onShowPermission(requestCode!!)
            dismiss()
        }

        btnCancel.setOnClickListener {
            listener?.onCancel()
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
