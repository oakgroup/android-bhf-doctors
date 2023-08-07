package com.active.orbit.baseapp.core.permissions

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.active.orbit.baseapp.design.activities.engine.BaseActivity

/**
 * Utility class to manage runtime permissions
 *
 * @author omar.brugna
 */
class Permissions(val group: Group) {

    companion object {
        private const val REQUEST_ACCESS_FINE_LOCATION = 0
        private const val REQUEST_ACCESS_BACKGROUND_LOCATION = 1
        private const val REQUEST_ACCESS_EXTERNAL_STORAGE = 2
        private const val REQUEST_ACCESS_ACTIVITY_RECOGNITION = 3
        private const val REQUEST_ACCESS_CAMERA_FOR_SCAN = 4
        private const val REQUEST_ACCESS_CAMERA_FOR_CAPTURE = 5
        private const val REQUEST_DOWNLOAD_PDF = 6
        private const val REQUEST_NOTIFICATIONS = 7

    }

    fun check(activity: AppCompatActivity): Boolean {
        for (permission in group.permissions) {
            val status = ActivityCompat.checkSelfPermission(activity, permission)
            if (status != PackageManager.PERMISSION_GRANTED)
                return false
        }
        return true
    }

    fun shouldShowExplanation(activity: AppCompatActivity): Boolean {
        for (permission in group.permissions) {
            val status = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
            if (status) return true
        }
        return false
    }

    fun request(activity: AppCompatActivity) {
        ActivityCompat.requestPermissions(activity, group.permissions, group.requestCode)
    }

    enum class Group(val permissions: Array<String>, val requestCode: Int) {
        ACCESS_FINE_LOCATION(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), REQUEST_ACCESS_FINE_LOCATION
        ),

        @RequiresApi(Build.VERSION_CODES.Q)
        ACCESS_BACKGROUND_LOCATION(
            arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
            REQUEST_ACCESS_BACKGROUND_LOCATION
        ),
        ACCESS_EXTERNAL_STORAGE(
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_ACCESS_EXTERNAL_STORAGE
        ),

        @RequiresApi(Build.VERSION_CODES.Q)
        ACCESS_ACTIVITY_RECOGNITION(
            arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
            REQUEST_ACCESS_ACTIVITY_RECOGNITION
        ),
        ACCESS_CAMERA_FOR_SCAN(
            arrayOf(Manifest.permission.CAMERA),
            REQUEST_ACCESS_CAMERA_FOR_SCAN
        ),
        ACCESS_CAMERA_FOR_CAPTURE(
            arrayOf(Manifest.permission.CAMERA),
            REQUEST_ACCESS_CAMERA_FOR_CAPTURE
        ),
        ACCESS_DOWNLOAD_PDF(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            REQUEST_DOWNLOAD_PDF
        ),

        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        ACCESS_NOTIFICATIONS(
            arrayOf(
                Manifest.permission.POST_NOTIFICATIONS
            ),
            REQUEST_NOTIFICATIONS
        )
    }
}