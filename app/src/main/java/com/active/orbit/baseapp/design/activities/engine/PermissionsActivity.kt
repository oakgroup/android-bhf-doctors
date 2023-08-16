package com.active.orbit.baseapp.design.activities.engine

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import androidx.annotation.CallSuper
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.IntentCompat
import androidx.core.content.PackageManagerCompat
import androidx.core.content.UnusedAppRestrictionsConstants
import com.active.orbit.baseapp.core.listeners.ResultListener
import com.active.orbit.baseapp.core.permissions.Permissions
import com.active.orbit.baseapp.core.preferences.engine.Preferences
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.design.dialogs.PermissionsDialog
import com.active.orbit.baseapp.design.dialogs.listeners.PermissionsDialogListener

abstract class PermissionsActivity : AbstractActivity() {

    private var permissionsDialog: PermissionsDialog? = null
    protected var permissionsDialogShown = false

    fun onboarded(): Boolean {
        return onboardedActivityRecognition()
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun onboardedActivityRecognition(): Boolean {
        return hasActivityRecognitionPermissionGranted()
    }

    fun onboardedLocation(): Boolean {
        return hasLocationPermissionGranted() && hasBackgroundLocationPermissionGranted()
    }

    @SuppressLint("NewApi")
    fun onboardedBattery(): Boolean {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        return powerManager.isIgnoringBatteryOptimizations(packageName)
    }

    fun onboardedUnusedRestrictions(listener: ResultListener) {
        val future = PackageManagerCompat.getUnusedAppRestrictionsStatus(this)
        future.addListener({
            when (future.get()) {
                UnusedAppRestrictionsConstants.ERROR -> listener.onResult(true)
                UnusedAppRestrictionsConstants.FEATURE_NOT_AVAILABLE -> listener.onResult(true)
                UnusedAppRestrictionsConstants.DISABLED -> listener.onResult(true)
                UnusedAppRestrictionsConstants.API_30_BACKPORT, UnusedAppRestrictionsConstants.API_30, UnusedAppRestrictionsConstants.API_31 -> listener.onResult(false)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    fun requestUnusedRestrictions() {
        // If your app works primarily in the background, you can ask the user
        // to disable these restrictions. Check if you have already asked the
        // user to disable these restrictions. If not, you can show a message to
        // the user explaining why permission auto-reset and Hibernation should be
        // disabled. Tell them that they will now be redirected to a page where
        // they can disable these features.
        val intent = IntentCompat.createManageUnusedAppRestrictionsIntent(this, packageName)
        // Must use startActivityForResult(), not startActivity(), even if
        // you don't use the result code returned in onActivityResult().
        @Suppress("DEPRECATION")
        startActivityForResult(intent, 983724)
    }

    fun onboardedPrivacyPolicy(): Boolean {
        return Preferences.lifecycle(this).isPrivacyPolicyAccepted
    }

    protected fun hasLocationPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) true
        else {
            val permission = Permissions(Permissions.Group.ACCESS_FINE_LOCATION)
            permission.check(this)
        }
    }

    protected fun hasBackgroundLocationPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) true
        else {
            val permission = Permissions(Permissions.Group.ACCESS_BACKGROUND_LOCATION)
            permission.check(this)
        }
    }

    protected fun hasActivityRecognitionPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) true
        else {
            val permission = Permissions(Permissions.Group.ACCESS_ACTIVITY_RECOGNITION)
            permission.check(this)
        }
    }

    protected fun hasDownloadPdfPermissionGranted(): Boolean {
        val permission = Permissions(Permissions.Group.ACCESS_DOWNLOAD_PDF)
        return permission.check(this)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    protected fun hasNotificationPermissionGranted(): Boolean {
        val permission = Permissions(Permissions.Group.ACCESS_NOTIFICATIONS)
        return permission.check(this)
    }

    protected fun requestPermissionLocation() {
        val permission = Permissions(Permissions.Group.ACCESS_FINE_LOCATION)
        permission.request(this)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    protected fun requestPermissionBackgroundLocation() {
        val permission = Permissions(Permissions.Group.ACCESS_BACKGROUND_LOCATION)
        permission.request(this)
    }

    protected fun requestPermissionActivityRecognition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !hasActivityRecognitionPermissionGranted())
            requestPermissionRecognition()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun requestPermissionRecognition() { // from android Q, we have to ask for activity recognition permission before asking for google fit permission
        val permission = Permissions(Permissions.Group.ACCESS_ACTIVITY_RECOGNITION)
        permission.request(this)
    }

    private fun requestPermissionCameraForScan() {
        val permission = Permissions(Permissions.Group.ACCESS_CAMERA_FOR_SCAN)
        permission.request(this)
    }

    private fun requestPermissionCameraForCapture() {
        val permission = Permissions(Permissions.Group.ACCESS_CAMERA_FOR_CAPTURE)
        permission.request(this)
    }

    private fun requestPermissionReadExternalStorage() {
        val permission = Permissions(Permissions.Group.ACCESS_EXTERNAL_STORAGE)
        permission.request(this)
    }

    protected fun requestPermissionDownloadPdf() {
        val permission = Permissions(Permissions.Group.ACCESS_DOWNLOAD_PDF)
        permission.request(this)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    protected fun requestPermissionNotifications() {
        val permission = Permissions(Permissions.Group.ACCESS_NOTIFICATIONS)
        permission.request(this)
    }

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            when (requestCode) {
                Permissions.Group.ACCESS_FINE_LOCATION.requestCode -> {
                    Logger.i("Location permission enabled")
                    onPermissionEnabled(requestCode)
                }

                Permissions.Group.ACCESS_BACKGROUND_LOCATION.requestCode -> {
                    Logger.i("Background location permission enabled")
                    onPermissionEnabled(requestCode)
                }

                Permissions.Group.ACCESS_EXTERNAL_STORAGE.requestCode -> {
                    Logger.i("Access external storage permission enabled")
                    onPermissionEnabled(requestCode)
                }

                Permissions.Group.ACCESS_ACTIVITY_RECOGNITION.requestCode -> {
                    Logger.i("Access activity recognition permission enabled")
                    onPermissionEnabled(requestCode)
                }

                Permissions.Group.ACCESS_CAMERA_FOR_SCAN.requestCode -> {
                    Logger.i("Access camera permission enabled")
                    onPermissionEnabled(requestCode)
                }

                Permissions.Group.ACCESS_CAMERA_FOR_CAPTURE.requestCode -> {
                    Logger.i("Access camera permission enabled")
                    onPermissionEnabled(requestCode)
                }

                Permissions.Group.ACCESS_DOWNLOAD_PDF.requestCode -> {
                    Logger.i("Download pdf permission enabled")
                    onPermissionEnabled(requestCode)
                }

                Permissions.Group.ACCESS_NOTIFICATIONS.requestCode -> {
                    Logger.i("Notification permission enabled")
                    onPermissionEnabled(requestCode)
                }
            }
        } else {
            when (requestCode) {
                Permissions.Group.ACCESS_FINE_LOCATION.requestCode -> {
                    Logger.i("Access location permission disabled")
                    onPermissionDisabled(Permissions(Permissions.Group.ACCESS_FINE_LOCATION))
                }

                Permissions.Group.ACCESS_BACKGROUND_LOCATION.requestCode -> {
                    Logger.i("Access background location permission disabled")
                    onPermissionDisabled(Permissions(Permissions.Group.ACCESS_BACKGROUND_LOCATION))
                }

                Permissions.Group.ACCESS_EXTERNAL_STORAGE.requestCode -> {
                    Logger.i("Access external storage permission disabled")
                    onPermissionDisabled(Permissions(Permissions.Group.ACCESS_EXTERNAL_STORAGE))
                }

                Permissions.Group.ACCESS_ACTIVITY_RECOGNITION.requestCode -> {
                    Logger.i("Access activity recognition permission disabled")
                    onPermissionDisabled(Permissions(Permissions.Group.ACCESS_ACTIVITY_RECOGNITION))
                }

                Permissions.Group.ACCESS_CAMERA_FOR_SCAN.requestCode -> {
                    Logger.i("Access camera permission disabled")
                    onPermissionDisabled(Permissions(Permissions.Group.ACCESS_CAMERA_FOR_SCAN))
                }

                Permissions.Group.ACCESS_CAMERA_FOR_CAPTURE.requestCode -> {
                    Logger.i("Access camera permission disabled")
                    onPermissionDisabled(Permissions(Permissions.Group.ACCESS_CAMERA_FOR_CAPTURE))
                }

                Permissions.Group.ACCESS_DOWNLOAD_PDF.requestCode -> {
                    Logger.i("Access download pdf permission disabled")
                    onPermissionDisabled(Permissions(Permissions.Group.ACCESS_DOWNLOAD_PDF))
                }

                Permissions.Group.ACCESS_NOTIFICATIONS.requestCode -> {
                    Logger.i("Access notification permission disabled")
                    onPermissionDisabled(Permissions(Permissions.Group.ACCESS_NOTIFICATIONS))
                }
            }
        }
    }

    @CallSuper
    open fun onPermissionEnabled(requestCode: Int) {
        // override to customise
    }

    open fun onPermissionDisabled(permission: Permissions) {
        // override to customise
        showPermissionsDialog(permission)
    }

    private fun showPermissionsDialog(permission: Permissions) {
        val deniedForever = !permission.shouldShowExplanation(this)
        showPermissionsDialog(permission.group.requestCode, deniedForever)
    }

    private fun showPermissionsDialog(requestCode: Int, deniedForever: Boolean) {
        permissionsDialogShown = true
        if (permissionsDialog?.isVisible != true) {
            val bundle = Bundle()
            bundle.putInt(PermissionsDialog.REQUEST_CODE, requestCode)
            permissionsDialog = PermissionsDialog()
            permissionsDialog?.arguments = bundle
            permissionsDialog?.isCancelable = false
            permissionsDialog?.listener = object : PermissionsDialogListener {
                @SuppressLint("NewApi")
                @RequiresApi(Build.VERSION_CODES.Q)
                override fun onShowPermission(requestCode: Int) {
                    permissionsDialogShown = false
                    permissionsDialog = null
                    when (requestCode) {

                        Permissions.Group.ACCESS_FINE_LOCATION.requestCode -> {
                            if (deniedForever) Router.getInstance().openSettings(this@PermissionsActivity)
                            else requestPermissionLocation()
                        }

                        Permissions.Group.ACCESS_BACKGROUND_LOCATION.requestCode -> {
                            if (deniedForever) Router.getInstance().openSettings(this@PermissionsActivity)
                            else requestPermissionBackgroundLocation()
                        }

                        Permissions.Group.ACCESS_EXTERNAL_STORAGE.requestCode -> {
                            if (deniedForever) Router.getInstance().openSettings(this@PermissionsActivity)
                            else requestPermissionReadExternalStorage()
                        }

                        Permissions.Group.ACCESS_ACTIVITY_RECOGNITION.requestCode -> {
                            if (deniedForever) Router.getInstance().openSettings(this@PermissionsActivity)
                            else requestPermissionRecognition()
                        }

                        Permissions.Group.ACCESS_CAMERA_FOR_SCAN.requestCode -> {
                            if (deniedForever) Router.getInstance().openSettings(this@PermissionsActivity)
                            else requestPermissionCameraForScan()
                        }

                        Permissions.Group.ACCESS_CAMERA_FOR_CAPTURE.requestCode -> {
                            if (deniedForever) Router.getInstance().openSettings(this@PermissionsActivity)
                            else requestPermissionCameraForCapture()
                        }

                        Permissions.Group.ACCESS_DOWNLOAD_PDF.requestCode -> {
                            if (deniedForever) Router.getInstance().openSettings(this@PermissionsActivity)
                            else requestPermissionDownloadPdf()
                        }

                        Permissions.Group.ACCESS_NOTIFICATIONS.requestCode -> {
                            if (deniedForever) Router.getInstance().openSettings(this@PermissionsActivity)
                            else requestPermissionNotifications()
                        }
                    }
                }

                override fun onCancel() {
                    super.onCancel()
                    permissionsDialogShown = false
                    permissionsDialog = null
                }
            }
            permissionsDialog?.show(supportFragmentManager, PermissionsDialog::javaClass.name)
        }
    }
}