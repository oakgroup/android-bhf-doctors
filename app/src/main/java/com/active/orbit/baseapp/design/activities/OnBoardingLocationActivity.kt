package com.active.orbit.baseapp.design.activities

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.active.orbit.baseapp.core.permissions.Permissions
import com.active.orbit.baseapp.core.routing.enums.Extra
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.databinding.ActivityOnBoardingLocationBinding
import com.active.orbit.baseapp.design.activities.engine.BaseActivity

class OnBoardingLocationActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityOnBoardingLocationBinding
    private var fromHelp = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepare()
    }

    override fun onResume() {
        super.onResume()
        if (onboardedLocation()) {
            proceed()
        }
    }

    private fun prepare() {
        fromHelp = activityBundle.getBoolean(Extra.FROM_HELP.key, false)
        binding.btnPermission.setOnClickListener(this)
    }

    private fun proceed() {
        if (!fromHelp) {
            manageOnBoarding()
        }
        finish()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onPermissionEnabled(requestCode: Int) {
        super.onPermissionEnabled(requestCode)
        when (requestCode) {
            Permissions.Group.ACCESS_ACTIVITY_RECOGNITION.requestCode -> {
                requestPermissionLocation()
            }
            Permissions.Group.ACCESS_FINE_LOCATION.requestCode -> {
                requestPermissionBackgroundLocation()
            }
            Permissions.Group.ACCESS_BACKGROUND_LOCATION.requestCode -> {
                proceed()
            }
            else -> {
                Logger.e("Undefined request code $requestCode on permission enabled ")
            }
        }
    }

    override fun onClick(v: View?) {

        when(v){
            binding.btnPermission -> {
                if (!hasActivityRecognitionPermissionGranted()) {
                    requestPermissionActivityRecognition()
                } else if (!hasLocationPermissionGranted()) {
                    requestPermissionLocation()
                } else if (!hasBackgroundLocationPermissionGranted()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        requestPermissionBackgroundLocation()
                    }
                }
            }
        }
    }

    override fun getToolbarResource(): Int? {
        return null
    }
}