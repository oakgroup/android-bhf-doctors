package com.active.orbit.baseapp.design.activities.engine

import android.os.Bundle
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.routing.enums.Extra
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import uk.ac.shef.tracker.core.computation.DailyComputation
import uk.ac.shef.tracker.core.observers.TrackerObserver
import uk.ac.shef.tracker.core.observers.TrackerObserverType
import uk.ac.shef.tracker.core.tracker.TrackerManager
import uk.ac.shef.tracker.core.utils.TimeUtils
import java.util.Calendar
import kotlin.coroutines.CoroutineContext

/**
 * Abstract activity that should be extended from all the other activities
 *
 * @author omar.brugna
 */
abstract class BaseActivity : PermissionsActivity(), CoroutineScope, TrackerObserver {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    protected lateinit var activityBundle: Bundle
    private var isFromOnCreate = true

    protected lateinit var thiss: BaseActivity

    var dailyComputation: DailyComputation? = null


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

    override fun onDestroy() {
        super.onDestroy()

        dailyComputation?.unregisterObserver()
    }

    private fun getAnimationType(): ActivityAnimation {
        val value = activityBundle.getInt(Extra.ANIMATION.key)
        return ActivityAnimation.getByValue(value)
    }

    /**
     * This computes the results and causes the refresh of the interface via the active data
     */
    protected fun computeResults() {
        val currentDateTime = TrackerManager.getInstance(this).currentDateTime
        val midnight = TimeUtils.midnightInMsecs(currentDateTime)
        val endOfDay = midnight + TimeUtils.ONE_DAY_MILLIS
        dailyComputation = DailyComputation(this, midnight, endOfDay)
        dailyComputation?.registerObserver(this)
        dailyComputation?.computeResultsAsync()
    }

    override fun onTrackerUpdate(type: TrackerObserverType, data: Any) {
        Logger.d("Tracker observer update $type")
    }

    override fun onDateSelected(selectedDateTime: Calendar) {
        TrackerManager.getInstance(this).currentDateTime = selectedDateTime.timeInMillis
        computeResults()
    }
}