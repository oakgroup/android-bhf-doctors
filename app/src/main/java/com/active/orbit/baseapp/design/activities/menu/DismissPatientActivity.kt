package com.active.orbit.baseapp.design.activities.menu

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.enums.WearableMessageType
import com.active.orbit.baseapp.core.managers.WearableManager
import com.active.orbit.baseapp.core.models.WearableMessageModel
import com.active.orbit.baseapp.core.preferences.engine.Preferences
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.core.utils.ThreadHandler.backgroundThread
import com.active.orbit.baseapp.core.utils.ThreadHandler.mainThread
import com.active.orbit.baseapp.core.utils.TimeUtils
import com.active.orbit.baseapp.core.utils.Utils
import com.active.orbit.baseapp.databinding.ActivityDismissPatientBinding
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.dialogs.DataCheckPhoneDialog
import com.active.orbit.baseapp.design.dialogs.DataCheckWatchDialog
import com.active.orbit.baseapp.design.dialogs.DataUploadPhoneDialog
import com.active.orbit.baseapp.design.dialogs.listeners.DataCheckPhoneDialogListener
import com.active.orbit.baseapp.design.dialogs.listeners.DataUploadPhoneDialogListener
import com.active.orbit.baseapp.design.utils.UiUtils
import com.active.orbit.tracker.data_upload.data_senders.DataSenderUtility
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DismissPatientActivity : BaseActivity(), View.OnClickListener, MessageClient.OnMessageReceivedListener {

    private lateinit var binding: ActivityDismissPatientBinding

    private var phoneDataUploaded = false
    private var watchDataUploaded = false
    private var remainingPhoneData = Constants.EMPTY
    private var remainingWatchData = Constants.EMPTY

    private var watchResponded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDismissPatientBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showBackButton()
        showLogoButton()


        prepare()
    }

    override fun onResume() {
        super.onResume()

        Wearable.getMessageClient(this).addListener(this)
    }

    override fun onPause() {
        super.onPause()

        try {
            Wearable.getMessageClient(this).removeListener(this)
        } catch (e: Exception) {
            Logger.e("Exception removing wearable message client listener on ${javaClass.name}")
        }
    }

    private fun prepare() {
        binding.btnCheckPhoneData.setIcon(R.drawable.ic_check_data)
        binding.btnCheckPhoneData.setText(getString(R.string.check_phone_data))
        binding.btnCheckPhoneData.disableClick()
        binding.btnCheckPhoneData.setOnClickListener(this)


        binding.btnCheckWatchData.setIcon(R.drawable.ic_check_data)
        binding.btnCheckWatchData.setText(getString(R.string.check_watch_data))
        binding.btnCheckWatchData.disableClick()
        binding.btnCheckWatchData.setOnClickListener(this)

        binding.btnDismiss.setOnClickListener(this)
    }


    private fun updatePhoneStatus() {
        binding.btnCheckPhoneData.hideProgress()
        binding.phoneDataMessage.visibility = View.VISIBLE

        if (phoneDataUploaded) {
            binding.btnCheckPhoneData.setIcon(R.drawable.ic_success_small)
            binding.phoneDataMessage.text = getString(R.string.data_sent)
        } else {
            val arguments = Bundle()
            arguments.putString(DataCheckPhoneDialog.ARGUMENT_DATA_AMOUNT, remainingPhoneData)
            val dataCheckPhoneDialog = DataCheckPhoneDialog()
            dataCheckPhoneDialog.listener = object : DataCheckPhoneDialogListener {
                override fun onUploadNow() {
                    val dataUploadPhoneDialog = DataUploadPhoneDialog()
                    dataUploadPhoneDialog.listener = object : DataUploadPhoneDialogListener {
                        override fun onUploadCompleted() {
                            UiUtils.showLongToast(this@DismissPatientActivity, getString(R.string.upload_completed))
                            binding.btnCheckPhoneData.showProgress()
                            Utils.delay(TimeUtils.ONE_SECOND_MILLIS.toLong()) {
                                // run with delay because the tracker database requires time for the data to be marked as uploaded
                                binding.btnCheckPhoneData.performClick()
                            }
                        }

                        override fun onUploadInBackground() {
                            UiUtils.showLongToast(this@DismissPatientActivity, getString(R.string.upload_background_description))
                        }
                    }
                    dataUploadPhoneDialog.isCancelable = false
                    dataUploadPhoneDialog.show(supportFragmentManager, DataUploadPhoneDialog::javaClass.name)
                }
            }
            dataCheckPhoneDialog.isCancelable = false
            dataCheckPhoneDialog.arguments = arguments
            dataCheckPhoneDialog.show(supportFragmentManager, DataCheckPhoneDialog::javaClass.name)

            binding.btnCheckPhoneData.setIcon(R.drawable.ic_failure)
            binding.phoneDataMessage.text = getString(R.string.data_to_be_sent, remainingPhoneData)
        }
    }


    private fun updateWatchStatus() {
        binding.btnCheckWatchData.hideProgress()
        binding.watchDataMessage.visibility = View.VISIBLE

        if (!watchResponded) {
            binding.btnCheckWatchData.setIcon(R.drawable.ic_check_data)
            binding.watchDataMessage.text = getString(R.string.open_wear_app_check_data)
            UiUtils.showLongToast(this, getString(R.string.watch_not_responding))
        } else if (watchDataUploaded) {
            binding.btnCheckWatchData.setIcon(R.drawable.ic_success_small)
            binding.watchDataMessage.text = getString(R.string.data_sent)
        } else {
            val arguments = Bundle()
            arguments.putString(DataCheckWatchDialog.ARGUMENT_DATA_AMOUNT, remainingWatchData)
            val dialog = DataCheckWatchDialog()
            dialog.isCancelable = false
            dialog.arguments = arguments
            dialog.show(supportFragmentManager, DataCheckWatchDialog::javaClass.name)

            binding.btnCheckWatchData.setIcon(R.drawable.ic_failure)
            binding.watchDataMessage.text = getString(R.string.data_to_be_sent, remainingWatchData)
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnCheckPhoneData -> {
                backgroundThread {

                    val limitMillis = System.currentTimeMillis() - TimeUtils.ONE_MINUTE_MILLIS
                    val unsentPhoneDataCount = DataSenderUtility(this).dataToSend(limitMillis)

                    if (unsentPhoneDataCount > 0) {
                        phoneDataUploaded = false
                        remainingPhoneData = unsentPhoneDataCount.toString()
                    } else {
                        phoneDataUploaded = true
                    }

                    mainThread {
                        binding.btnCheckPhoneData.showProgress()
                        Utils.delay(TimeUtils.ONE_SECOND_MILLIS.toLong() * 2) {
                            updatePhoneStatus()
                        }
                    }
                }
            }

            binding.btnCheckWatchData -> {
                backgroundThread {
                    val wearableManager = WearableManager()
                    wearableManager.syncWatch(this, WearableMessageType.UPLOAD)

                    mainThread {
                        binding.watchDataMessage.text = getString(R.string.watch_contact)
                        binding.btnCheckWatchData.showProgress()
                        Utils.delay(TimeUtils.ONE_SECOND_MILLIS.toLong() * 5) {
                            updateWatchStatus()
                        }
                    }
                }
            }

            binding.btnDismiss -> {
                val watchSynchronized = Preferences.user(this).watchSynchronized
                if (watchSynchronized) {
                    if (watchDataUploaded && phoneDataUploaded) {
                        showDismissPatientDialog()
                    } else {
                        showForceDismissPatientDialog()
                    }
                } else {
                    // the watch has never been synchronise, just skip the data upload check
                    showDismissPatientDialog()
                }
            }
        }
    }


    override fun onMessageReceived(p0: MessageEvent) {
        backgroundThread {
            if (MESSAGE_PATH == p0.path) {
                val messageJsonString = String(p0.data)

                Logger.d("Message received from wear app: $messageJsonString")
                try {
                    val type = object : TypeToken<WearableMessageModel>() {}.type
                    val wearableMessageModel = Gson().fromJson<WearableMessageModel>(messageJsonString, type)

                    if (wearableMessageModel.isValid()) {
                        when (wearableMessageModel.type) {
                            WearableMessageType.DISMISS.key -> {
                                if (!TextUtils.isEmpty(wearableMessageModel.idUser) && wearableMessageModel.idUser == Preferences.user(this).idUser) {
                                    mainThread {
                                        Router.getInstance().logout(this)
                                    }
                                } else {
                                    Logger.e("Wearable message user ID ${wearableMessageModel.idUser} does not match the user id ${Preferences.user(this).idUser}  on this mobile")
                                }
                            }
                            WearableMessageType.UPLOAD.key -> {
                                if (!TextUtils.isEmpty(wearableMessageModel.idUser) && wearableMessageModel.idUser == Preferences.user(this).idUser) {
                                    mainThread {
                                        watchResponded = true
                                        watchDataUploaded = wearableMessageModel.dataUploaded
                                        remainingWatchData = wearableMessageModel.remainingDataUpload
                                    }
                                } else {
                                    Logger.e("Wearable message user ID ${wearableMessageModel.idUser} does not match the user id ${Preferences.user(this).idUser}  on this mobile")
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