package com.active.orbit.baseapp.design.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.DialogFragment
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.enums.WearableMessageType
import com.active.orbit.baseapp.core.models.WearableMessageModel
import com.active.orbit.baseapp.core.preferences.engine.Preferences
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.core.utils.ThreadHandler.backgroundThread
import com.active.orbit.baseapp.core.utils.ThreadHandler.mainThread
import com.active.orbit.baseapp.core.utils.TimeUtils
import com.active.orbit.baseapp.core.utils.Utils
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.dialogs.listeners.SyncWearDialogListener
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SyncWearDialog : DialogFragment(), MessageClient.OnMessageReceivedListener {

    var listener: SyncWearDialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.dialog_sync_wear, view as ViewGroup?, false)
        setup(view)

        Wearable.getMessageClient(requireContext()).addListener(this)

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("")
        builder.setView(view)
        return builder.create()
    }

    private fun setup(view: View) {
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)
        val btnSync = view.findViewById<Button>(R.id.btnSync)
        val layoutDetails = view.findViewById<LinearLayoutCompat>(R.id.layoutDetails)
        val waitingForWatchLayout = view.findViewById<LinearLayoutCompat>(R.id.waitingForWatchLayout)

        btnCancel.setOnClickListener {
            listener?.onCancel()
            dismiss()
        }

        btnSync.setOnClickListener {
            listener?.onSync()
            layoutDetails.visibility = View.GONE
            waitingForWatchLayout.visibility = View.VISIBLE
        }
    }

    override fun onStart() {
        super.onStart()

        // make the background of the dialog transparent
        val window: Window? = dialog?.window
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        try {
            Wearable.getMessageClient(requireContext()).removeListener(this)
        } catch (e: Exception) {
            Logger.e("Exception removing wearable message client listener on ${javaClass.name}")
        }
    }

    override fun onMessageReceived(p0: MessageEvent) {
        backgroundThread {
            if (BaseActivity.MESSAGE_PATH == p0.path) {
                val messageJsonString = String(p0.data)
                Logger.d("Message received from wear app: $messageJsonString")
                try {
                    val type = object : TypeToken<WearableMessageModel>() {}.type
                    val wearableMessageModel = Gson().fromJson<WearableMessageModel>(messageJsonString, type)

                    if (wearableMessageModel.isValid()) {
                        when (wearableMessageModel.type) {
                            WearableMessageType.SYNC.key -> {
                                if (!TextUtils.isEmpty(wearableMessageModel.idUser) && wearableMessageModel.idUser == Preferences.user(requireContext()).idUser) {
                                    mainThread {
                                        Utils.delay(TimeUtils.ONE_SECOND_MILLIS.toLong() * 2) {
                                            Preferences.user(requireContext()).watchSynchronized = true
                                            listener?.onWatchSynced()
                                            dismiss()
                                        }
                                    }
                                } else {
                                    Logger.e("Wearable message user ID ${wearableMessageModel.idUser} does not match the user id ${Preferences.user(requireContext()).idUser}  on this mobile")
                                }
                            }
                        }
                    } else {
                        Logger.e("Wearable message model is not valid")
                    }
                } catch (e: Exception) {
                    Logger.e("Error parsing wearable message json")
                }
            } else {
                Logger.w("Message received from mobile app unknown")
            }
        }
    }
}
