package com.active.orbit.baseapp.design.activities.engine

import android.os.Bundle
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.routing.enums.Extra
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation
import com.active.orbit.tracker.MyViewModel
import com.active.orbit.tracker.TrackerManager
import com.active.orbit.tracker.retrieval.ComputeDayDataAsync
import com.active.orbit.tracker.utils.Globals

/**
 * Abstract activity that should be extended from all the other activities
 *
 * @author omar.brugna
 */
abstract class BaseActivity : PermissionsActivity() {

    protected lateinit var activityBundle: Bundle
    private var isFromOnCreate = true

    protected lateinit var thiss: BaseActivity

    val viewModel: MyViewModel by viewModels()

    companion object {
        const val MESSAGE_PATH = "/message"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        thiss = this

        activityBundle = savedInstanceState ?: intent.extras ?: Bundle()
        when (getAnimationType().value) {
            ActivityAnimation.FADE.value -> overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            ActivityAnimation.LEFT_RIGHT.value -> overridePendingTransition(R.anim.shift_to_left_in, R.anim.no_animation)
            ActivityAnimation.BOTTOM_TOP.value -> overridePendingTransition(R.anim.shift_to_top_in, R.anim.no_animation)
            ActivityAnimation.TOP_BOTTOM.value -> overridePendingTransition(R.anim.shift_to_bottom_out, R.anim.no_animation)
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
            ActivityAnimation.TOP_BOTTOM.value -> overridePendingTransition(0, R.anim.shift_to_top_in)
            else -> Logger.e("Undefined activity animation")
        }
    }

    private fun getAnimationType(): ActivityAnimation {
        val value = activityBundle.getInt(Extra.ANIMATION.key)
        return ActivityAnimation.getByValue(value)
    }

    /**
     * it computes the results and causes the refresh of the interface via the active data
     */
    protected fun computeResults() {
        val currentDateTime = TrackerManager.getInstance(this).currentDateTime
        val midnight = com.active.orbit.tracker.utils.Utils.midnightinMsecs(currentDateTime)
        val context = this
        val endOfDay = midnight + Globals.MSECS_IN_A_DAY
        Logger.i("Computing results: viewModel? $viewModel")
        ComputeDayDataAsync(context, viewModel, midnight, endOfDay).computeResultsAsync(viewModel)
    }
}