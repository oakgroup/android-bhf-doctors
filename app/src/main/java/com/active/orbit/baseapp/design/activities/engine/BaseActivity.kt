package com.active.orbit.baseapp.design.activities.engine

import android.content.pm.PackageManager
import android.os.Bundle
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.permissions.Permissions
import com.active.orbit.baseapp.core.routing.enums.Extra
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation

/**
 * Abstract activity that should be extended from all the other activities
 *
 * @author omar.brugna
 */
abstract class BaseActivity : AbstractActivity() {

    protected lateinit var activityBundle: Bundle
    private var isFromOnCreate = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityBundle = savedInstanceState ?: intent.extras ?: Bundle()
        when (getAnimationType().value) {
            ActivityAnimation.FADE.value -> overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            ActivityAnimation.LEFT_RIGHT.value -> overridePendingTransition(R.anim.shift_to_left_in, R.anim.no_animation)
            ActivityAnimation.BOTTOM_TOP.value -> overridePendingTransition(R.anim.shift_to_top_in, R.anim.no_animation)
            else -> Logger.e("Undefined activity animation")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putAll(activityBundle)
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        if (isFromOnCreate) isFromOnCreate = false
        else onUpdate()
    }

    protected open fun onUpdate() {
        // override to customize
    }

    override fun onPause() {
        super.onPause()

        when (getAnimationType().value) {
            ActivityAnimation.FADE.value -> overridePendingTransition(0, R.anim.fade_out)
            ActivityAnimation.LEFT_RIGHT.value -> overridePendingTransition(0, R.anim.shift_to_right_out)
            ActivityAnimation.BOTTOM_TOP.value -> overridePendingTransition(0, R.anim.shift_to_bottom_out)
            else -> Logger.e("Undefined activity animation")
        }
    }

    private fun getAnimationType(): ActivityAnimation {
        val value = activityBundle.getInt(Extra.ANIMATION.key)
        return ActivityAnimation.getByValue(value)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            when (requestCode) {
                Permissions.Group.ACCESS_FINE_LOCATION.requestCode -> {
                    Logger.i("Location permission enabled")
                    onPermissionAccessLocationEnabled()
                }
                Permissions.Group.ACCESS_BACKGROUND_LOCATION.requestCode -> {
                    Logger.i("Background location permission enabled")
                    onPermissionAccessBackgroundLocationEnabled()
                }
                Permissions.Group.ACCESS_EXTERNAL_STORAGE.requestCode -> {
                    Logger.i("Access external storage permission enabled")
                    onPermissionAccessStorageEnabled()
                }
            }
        } else {
            when (requestCode) {
                Permissions.Group.ACCESS_FINE_LOCATION.requestCode -> {
                    Logger.i("Access location permission disabled")
                    // TODO access fine location - show dialog
                }
                Permissions.Group.ACCESS_BACKGROUND_LOCATION.requestCode -> {
                    Logger.i("Access background location permission disabled")
                    // TODO background location - show dialog
                }
                Permissions.Group.ACCESS_EXTERNAL_STORAGE.requestCode -> {
                    Logger.i("Access external storage permission disabled")
                    // TODO external storage - show dialog
                }
            }
        }
    }

    open fun onPermissionAccessStorageEnabled() {
        // override to customize
    }

    open fun onPermissionAccessLocationEnabled() {
        // override to customize
    }

    open fun onPermissionAccessBackgroundLocationEnabled() {
        // override to customize
    }
}