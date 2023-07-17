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
import com.active.orbit.baseapp.core.utils.ThreadHandler.mainThread
import com.active.orbit.baseapp.core.utils.TimeUtils
import com.active.orbit.baseapp.core.utils.Utils
import com.active.orbit.baseapp.design.dialogs.listeners.DataUploadPhoneDialogListener
import uk.ac.shef.tracker.core.listeners.ResultListener
import uk.ac.shef.tracker.core.restarter.TrackerRestarter

class DataUploadPhoneDialog : DialogFragment() {

    var listener: DataUploadPhoneDialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.dialog_data_upload_phone, view as ViewGroup?, false)
        setup(view)

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("")
        builder.setView(view)
        return builder.create()
    }

    private fun setup(view: View) {
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)
        val btnUpload = view.findViewById<Button>(R.id.btnUpload)
        val layoutDetails = view.findViewById<LinearLayoutCompat>(R.id.layoutDetails)
        val uploadingDataLayout = view.findViewById<LinearLayoutCompat>(R.id.uploadingDataLayout)

        btnCancel.setOnClickListener {
            listener?.onCancel()
            dismiss()
        }

        btnUpload.setOnClickListener {
            btnUpload.setText(R.string.upload_background)
            btnCancel.visibility = View.GONE
            btnUpload.setOnClickListener {
                listener?.onUploadInBackground()
                dismiss()
            }

            layoutDetails.visibility = View.GONE
            uploadingDataLayout.visibility = View.VISIBLE

            val trackerRestarter = TrackerRestarter()
            trackerRestarter.startDataUploader(requireContext(), false, object : ResultListener {
                override fun onResult(success: Boolean) {
                    mainThread {
                        Utils.delay(TimeUtils.ONE_SECOND_MILLIS.toLong() * 1) {
                            listener?.onUploadCompleted()
                            dismiss()
                        }
                    }
                }
            })
        }
    }

    override fun onStart() {
        super.onStart()

        // make the background of the dialog transparent
        val window: Window? = dialog?.window
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}
