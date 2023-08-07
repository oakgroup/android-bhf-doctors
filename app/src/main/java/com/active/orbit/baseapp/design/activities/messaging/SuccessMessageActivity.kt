package com.active.orbit.baseapp.design.activities.messaging

import android.os.Bundle
import android.view.View
import com.active.orbit.baseapp.core.enums.SuccessMessageType
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.routing.enums.Extra
import com.active.orbit.baseapp.core.utils.BaseException
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.databinding.ActivitySuccessMessageBinding
import com.active.orbit.baseapp.design.activities.engine.Activities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation

class SuccessMessageActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySuccessMessageBinding
    var successMessage = SuccessMessageType.UNDEFINED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuccessMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val successMessageID = activityBundle.getInt(Extra.SUCCESS_MESSAGE.key)
        if (successMessageID != Constants.INVALID) {
            successMessage = SuccessMessageType.getById(successMessageID)
        } else {
            throw BaseException("Missing object identifier on ${javaClass.name}")
        }

        prepare()
    }

    private fun prepare() {
        binding.btnClose.setOnClickListener(this)
        binding.description.text = getString(successMessage.message)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnClose -> {
                when (successMessage) {
                    SuccessMessageType.REGISTRATION -> {
                        Router.getInstance()
                            .activityAnimation(ActivityAnimation.TOP_BOTTOM)
                            .homepage(thiss)
                    }

                    SuccessMessageType.MEDICINE_REPORTED -> {
                        finishAndRemoveTask()
                    }

                    SuccessMessageType.SYMPTOM_REPORTED -> {
                        finishAndRemoveTask()
                    }

                    SuccessMessageType.DISMISS_PATIENT -> {
                        Router.getInstance()
                            .newTask(true)
                            .clearTop(true)
                            .activityAnimation(ActivityAnimation.FADE)
                            .startBaseActivity(this, Activities.SPLASH)
                    }

                    SuccessMessageType.UNDEFINED -> {
                        Logger.e("Undefined success message type on ${javaClass.name}")
                        Router.getInstance().clearTop(true).startBaseActivity(this, Activities.SPLASH)
                    }
                }
                finish()
            }
        }
    }

    override fun getToolbarResource(): Int? {
        return null
    }

}