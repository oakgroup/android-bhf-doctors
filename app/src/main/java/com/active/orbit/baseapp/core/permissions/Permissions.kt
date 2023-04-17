package com.active.orbit.baseapp.core.permissions

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.active.orbit.baseapp.design.activities.engine.BaseActivity

/**
 * Utility class to manage runtime permissions
 *
 * @author omar.brugna
 */
class Permissions(private val context: Context, private val group: Group) {

    companion object {
        const val REQUEST_ACCESS_FINE_LOCATION = 0
        const val REQUEST_ACCESS_BACKGROUND_LOCATION = 1
        const val REQUEST_ACCESS_EXTERNAL_STORAGE = 2
    }

    fun check(): Boolean {
        for (permission in group.permissions) {
            val status = ContextCompat.checkSelfPermission(context, permission)
            if (status != PackageManager.PERMISSION_GRANTED)
                return false
        }
        return true
    }

    fun request(activity: BaseActivity) {
        ActivityCompat.requestPermissions(activity, group.permissions, group.requestCode)
    }

    enum class Group(val permissions: Array<String>, val requestCode: Int) {
        ACCESS_FINE_LOCATION(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), REQUEST_ACCESS_FINE_LOCATION
        ),

        @SuppressLint("InlinedApi")
        ACCESS_BACKGROUND_LOCATION(
            arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
            REQUEST_ACCESS_BACKGROUND_LOCATION
        ),
        ACCESS_EXTERNAL_STORAGE(
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_ACCESS_EXTERNAL_STORAGE
        )
    }
}