package com.active.orbit.baseapp.design.activities.onboarding

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.text.HtmlCompat
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.permissions.Permissions
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.routing.enums.Extra
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.databinding.ActivityOnBoardingLocationBinding
import com.active.orbit.baseapp.design.activities.engine.Activities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation
import com.active.orbit.baseapp.design.utils.UiUtils
import kotlin.math.roundToInt

class OnBoardingLocationActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityOnBoardingLocationBinding
    private var fromHelp = false

    private var userConsentName = Constants.EMPTY
    private var userConsentDate = Constants.INVALID.toLong()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showBackButton()

        prepare()
    }


    private fun prepare() {

        userConsentName = activityBundle.getString(Extra.USER_CONSENT_NAME.key)!!
        userConsentDate = activityBundle.getLong(Extra.USER_CONSENT_DATE.key)
        fromHelp = activityBundle.getBoolean(Extra.FROM_HELP.key, false)

        binding.description.text = HtmlCompat.fromHtml(getString(R.string.onboarding_location_1), HtmlCompat.FROM_HTML_MODE_COMPACT)

        binding.btnBack.setOnClickListener(this)
        binding.btnNext.setOnClickListener(this)

        binding.physicalActivityCheckBox.setOnClickListener(this)
        binding.locationServicesCheckBox.setOnClickListener(this)

        if (hasActivityRecognitionPermissionGranted()){
            binding.physicalActivityCheckBox.isChecked = true
        }

        if (hasLocationPermissionGranted() && hasBackgroundLocationPermissionGranted()){
            binding.locationServicesCheckBox.isChecked = true
        }


    }

    override fun onResume() {
        super.onResume()


    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onPermissionEnabled(requestCode: Int) {
        super.onPermissionEnabled(requestCode)
        when (requestCode) {
            Permissions.Group.ACCESS_FINE_LOCATION.requestCode -> {
                requestPermissionBackgroundLocation()
            }
            else -> {
                Logger.e("Undefined request code $requestCode on permission enabled ")
            }
        }
    }

    override fun onClick(v: View?) {

        when(v){

            binding.physicalActivityCheckBox -> {
                if (binding.physicalActivityCheckBox.isChecked) {
                    if (!hasActivityRecognitionPermissionGranted())requestPermissionActivityRecognition()
                }
            }

            binding.locationServicesCheckBox -> {
                if (binding.locationServicesCheckBox.isChecked) {
                    if (!hasLocationPermissionGranted()) {
                        requestPermissionLocation()
                    } else if (!hasBackgroundLocationPermissionGranted()){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            requestPermissionBackgroundLocation()
                        }
                    }
                }
            }


            binding.btnNext -> {
                if (binding.physicalActivityCheckBox.isChecked) {
                    if (!hasActivityRecognitionPermissionGranted()) {
                        requestPermissionActivityRecognition()

                    } else if (!hasLocationPermissionGranted() && binding.locationServicesCheckBox.isChecked ) {
                        requestPermissionLocation()

                    } else if (!hasBackgroundLocationPermissionGranted() && binding.locationServicesCheckBox.isChecked) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            requestPermissionBackgroundLocation()
                        }
                    } else{
                        val bundle = Bundle()
                        bundle.putString(Extra.USER_CONSENT_NAME.key, userConsentName)
                        bundle.putLong(Extra.USER_CONSENT_DATE.key, userConsentDate)

                        Router.getInstance()
                            .activityAnimation(ActivityAnimation.LEFT_RIGHT)
                            .startBaseActivity(this, Activities.PATIENT_DETAILS, bundle)
                    }

                } else{
                    UiUtils.showShortToast(this, R.string.activity_rec_permissions_not_granted)
                    binding.scrollView.scrollTo(binding.permissionsContainer.x.roundToInt(), binding.permissionsContainer.y.roundToInt())

                }
            }

            binding.btnBack -> {
                finish()
            }
        }
    }

}