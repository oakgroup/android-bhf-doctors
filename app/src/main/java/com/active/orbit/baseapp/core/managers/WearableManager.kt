package com.active.orbit.baseapp.core.managers

import android.content.Context
import android.text.TextUtils
import androidx.annotation.WorkerThread
import com.active.orbit.baseapp.core.enums.WearableMessageType
import com.active.orbit.baseapp.core.listeners.ResultListener
import com.active.orbit.baseapp.core.models.WearableMessageModel
import com.active.orbit.baseapp.core.preferences.engine.Preferences
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.core.utils.Logger
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.Wearable
import com.google.gson.Gson
import java.util.concurrent.ExecutionException

class WearableManager {

    var message: String = ""

    companion object {
        private const val MESSAGE_PATH = "/message"
    }

    /**
     * Send a message to all the paired wearable devices
     *
     * @param context an instance of [Context]
     * @param message the message [String] to send
     */
    private fun sendMessageToWearables(context: Context, message: String, listener: ResultListener? = null) {
        if (!TextUtils.isEmpty(message)) {
            this.message = message
            for (nodeId in getNodesIds(context)) {
                sendMessage(context, nodeId, listener)
            }
        } else {
            Logger.d("Message sent to wearable is empty")
        }
    }

    private fun sendMessage(context: Context, nodeId: String, listener: ResultListener? = null) {
        if (!TextUtils.isEmpty(nodeId)) {
            val sendTask: Task<Int> = Wearable.getMessageClient(context).sendMessage(nodeId, MESSAGE_PATH, message.toByteArray())
            sendTask.addOnSuccessListener {
                Logger.d("Message sent on wearable with id $nodeId")
                listener?.onResult(true)
            }
            sendTask.addOnFailureListener {
                Logger.e("Message failed on wearable with id $nodeId")
                listener?.onResult(false)
            }
        } else {
            Logger.d("Wearable nodeId is null")
            listener?.onResult(false)
        }
    }

    @WorkerThread
    private fun getNodesIds(context: Context): Collection<String> {
        val results = HashSet<String>()
        val nodes: List<com.google.android.gms.wearable.Node>
        try {
            nodes = Tasks.await<List<com.google.android.gms.wearable.Node>>(Wearable.getNodeClient(context).connectedNodes)
            for (node in nodes) {
                results.add(node.id)
            }
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return results
    }

    @WorkerThread
    fun syncWatch(context: Context, type: WearableMessageType) {
        val wearableMessageModel = WearableMessageModel()
        wearableMessageModel.type = type.key
        wearableMessageModel.idUser = Preferences.user(context).idUser ?: Constants.EMPTY
        wearableMessageModel.idPatient = Preferences.user(context).userNhsNumber ?: Constants.EMPTY

        when (type) {
            WearableMessageType.SYNC -> {
                //no other data to add
            }
            WearableMessageType.DISMISS -> {
                //no other data to add
            }
            WearableMessageType.DISMISS_CANCEL -> {
                //no other data to add
            }
            WearableMessageType.UPLOAD -> {
                wearableMessageModel.dataUploaded = false
                wearableMessageModel.remainingDataUpload = Constants.EMPTY
            }
            else -> {return}
        }

        val wearableMessageModelJson = Gson().toJson(wearableMessageModel)
        sendMessageToWearables(context, wearableMessageModelJson)
    }
}