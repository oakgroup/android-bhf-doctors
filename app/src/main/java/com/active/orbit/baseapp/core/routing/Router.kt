package com.active.orbit.baseapp.core.routing

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.database.engine.Database
import com.active.orbit.baseapp.core.preferences.engine.BasePreferences
import com.active.orbit.baseapp.core.preferences.engine.Preferences
import com.active.orbit.baseapp.core.routing.enums.Extra
import com.active.orbit.baseapp.core.utils.BaseException
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.core.utils.Utils
import com.active.orbit.baseapp.design.activities.Activities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.activities.engine.animations.ActivityAnimation
import com.active.orbit.baseapp.design.protocols.ActivityProvider
import java.util.*

/**
 * Utility class used to manage transitions between activities
 *
 * @author omar.brugna
 */
class Router {

    @Volatile
    private var isRouting: Boolean = false

    @Volatile
    private var mClearTop: Boolean = false

    @Volatile
    private var mNewTask: Boolean = false

    @Volatile
    private var mActivityAnimation = ActivityAnimation.DEFAULT

    companion object {

        @Volatile
        private var instance: Router? = null

        @Synchronized
        fun getInstance(): Router {
            if (instance == null) {
                synchronized(Router::class.java) {
                    // double check locking
                    if (instance == null)
                        instance = Router()
                }
            }
            instance!!.reset()
            return instance!!
        }
    }

    /**
     * Builder for clearTop flag
     */
    @Synchronized
    fun clearTop(clearTop: Boolean): Router {
        mClearTop = clearTop
        return instance!!
    }

    /**
     * Builder for newTask flag
     */
    @Synchronized
    fun newTask(newTask: Boolean): Router {
        mNewTask = newTask
        return instance!!
    }

    /**
     * Builder for activity animation
     */
    @Synchronized
    fun activityAnimation(activityAnimation: ActivityAnimation): Router {
        mActivityAnimation = activityAnimation
        return instance!!
    }

    @Synchronized
    private fun reset() {
        isRouting = false
        mClearTop = false
        mNewTask = false
        mActivityAnimation = ActivityAnimation.DEFAULT
    }

    /**
     * Start an activity
     *
     * @param context an instance of the [Context]
     * @param activity a [AppCompatActivity] to start
     * @param bundle a [Bundle] given to the started activity
     */
    @Synchronized
    private fun startActivity(context: Context, activity: Class<out AppCompatActivity>, bundle: Bundle) {
        val intent = getIntent(context, activity, bundle)
        context.startActivity(intent)
        reset()
    }

    /**
     * Start an activity for result
     *
     * @param parentActivity  an instance of the [BaseActivity]
     * @param activity a [AppCompatActivity] to start
     * @param bundle   a [Bundle] given to the started activity
     */
    @Synchronized
    private fun startActivityForResult(parentActivity: BaseActivity, activity: Class<out AppCompatActivity>, bundle: Bundle, requestCode: Int) {
        val intent = getIntent(parentActivity, activity, bundle)
        if (requestCode == Constants.INVALID) throw BaseException("Missing request code for activity result")
        parentActivity.startActivityForResult(intent, requestCode)
        reset()
    }

    private fun getIntent(context: Context, activity: Class<out AppCompatActivity>, bundle: Bundle): Intent {
        val intent = Intent(context, activity)
        intent.putExtras(bundle)
        if (mClearTop)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        if (mNewTask) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        return intent
    }

    /**
     * Start a [BaseActivity]
     *
     * @param context an instance of the [Context]
     * @param provider the target [ActivityProvider]
     */
    @Synchronized
    fun startBaseActivity(context: Context, provider: ActivityProvider) {
        startBaseActivity(context, provider, Bundle())
    }

    /**
     * Start a [BaseActivity]
     *
     * @param context an instance of the [Context]
     * @param provider the target [ActivityProvider]
     * @param bundle extra [Bundle] to merge
     */
    @Synchronized
    fun startBaseActivity(context: Context, provider: ActivityProvider, bundle: Bundle) {
        if (!isRouting) {
            isRouting = true
            bundle.putInt(Extra.ANIMATION.key, mActivityAnimation.value)
            startActivity(context, provider.getActivity(), bundle)
        }
    }

    /**
     * Start a [BaseActivity]
     *
     * @param parentActivity    an instance of the [BaseActivity]
     * @param provider the target [ActivityProvider]
     * @param bundle     extra [Bundle] to merge
     */
    @Synchronized
    fun startBaseActivityForResult(parentActivity: BaseActivity, provider: ActivityProvider, bundle: Bundle, requestCode: Int = Constants.INVALID) {
        if (!isRouting) {
            isRouting = true
            bundle.putInt(Extra.ANIMATION.key, mActivityAnimation.value)
            startActivityForResult(parentActivity, provider.getActivity(), bundle, requestCode)
        }
    }

    fun openUrl(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        context.startActivity(intent)
    }

    fun openPrivacyPolicy(context: Context) {
        val url = when (Locale.getDefault().language) {
            "en" -> context.resources.getString(R.string.privacy_policy_link_en)
            else -> context.resources.getString(R.string.privacy_policy_link_default)
        }
        openUrl(context, url)
    }

    fun openSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", Utils.getPackageName(context), null)
        intent.data = uri
        context.startActivity(intent)
    }

    fun homepage(context: Context) {
        clearTop(true)
        newTask(true)
        if (Preferences.user(context).isUserRegistered() && Preferences.user(context).programStarted) {
            startBaseActivity(context, Activities.MAIN)
        } else {
            startBaseActivity(context, Activities.MAIN)
        }
    }


    fun logout(context: Context) {
        BasePreferences.logout(context)
        Database.getInstance(context).logout()
        clearTop(true)
        newTask(true)
        activityAnimation(ActivityAnimation.FADE)
        startBaseActivity(context, Activities.SPLASH)
    }
}