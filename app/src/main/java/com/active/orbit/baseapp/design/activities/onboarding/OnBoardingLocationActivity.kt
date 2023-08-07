package com.active.orbit.baseapp.design.activities.onboarding

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.text.HtmlCompat
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.listeners.ResultListener
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
    private var fromMenu = false

    private var userConsentName = Constants.EMPTY
    private var userConsentDate = Constants.INVALID.toLong()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showBackButton()


        fromMenu = activityBundle.getBoolean(Extra.FROM_MENU.key, false)

        if (!fromMenu) {
            userConsentName = activityBundle.getString(Extra.USER_CONSENT_NAME.key)!!
            userConsentDate = activityBundle.getLong(Extra.USER_CONSENT_DATE.key)
        }

        prepare()
    }


    private fun prepare() {


        binding.description.text = HtmlCompat.fromHtml(getString(R.string.onboarding_location_1), HtmlCompat.FROM_HTML_MODE_COMPACT)

        if (fromMenu) {
            binding.bottomLayout.visibility = View.GONE
            binding.title.text = getString(R.string.location_services_title_two)
            binding.physicalActivityCheckBox.isEnabled = false
        } else {
            binding.btnNext.setOnClickListener(this)
            binding.title.text = getString(R.string.location_services_title_one)
        }

        binding.btnBack.setOnClickListener(this)

        binding.physicalActivityCheckBox.setOnClickListener(this)
        binding.locationServicesCheckBox.setOnClickListener(this)
    }

    private fun updateCheckBoxes() {
        binding.physicalActivityCheckBox.isChecked = hasActivityRecognitionPermissionGranted()
        binding.locationServicesCheckBox.isChecked = hasLocationPermissionGranted() && hasBackgroundLocationPermissionGranted()
    }

    override fun onResume() {
        super.onResume()

        updateCheckBoxes()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onPermissionEnabled(requestCode: Int) {
        super.onPermissionEnabled(requestCode)
        updateCheckBoxes()
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
        when (v) {
            binding.physicalActivityCheckBox -> {
                if (!hasActivityRecognitionPermissionGranted()) requestPermissionActivityRecognition()
                else Router.getInstance().openSettings(this)
            }

            binding.locationServicesCheckBox -> {
                if (!hasLocationPermissionGranted()) {
                    requestPermissionLocation()
                } else if (!hasBackgroundLocationPermissionGranted()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        requestPermissionBackgroundLocation()
                    }
                } else {
                    Router.getInstance().openSettings(this)
                }
            }

            binding.btnNext -> {
                if (hasActivityRecognitionPermissionGranted()) {
                    val bundle = Bundle()
                    bundle.putString(Extra.USER_CONSENT_NAME.key, userConsentName)
                    bundle.putLong(Extra.USER_CONSENT_DATE.key, userConsentDate)
                    Router.getInstance()
                        .activityAnimation(ActivityAnimation.LEFT_RIGHT)
                        .startBaseActivity(this, Activities.ON_BOARDING_BATTERY, bundle)
                } else {
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