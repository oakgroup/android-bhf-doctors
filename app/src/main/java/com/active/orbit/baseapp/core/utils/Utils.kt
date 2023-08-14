package com.active.orbit.baseapp.core.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.BatteryManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.active.orbit.baseapp.R
import java.net.Inet4Address
import java.net.NetworkInterface
import java.util.UUID
import kotlin.math.abs

/**
 * Utility class that provides some general useful methods
 *
 * @author omar.brugna
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
object Utils {

    fun getAppName(context: Context): String {
        return context.resources.getString(R.string.app_name)
    }

    fun getPackageName(context: Context): String {
        val packageManager = context.packageManager
        if (packageManager != null) {
            try {
                val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
                if (packageInfo != null) return packageInfo.packageName
                else Logger.e("Package info is null")
            } catch (e: Exception) {
                Logger.e("Exception getting package name")
            }
        }
        return Constants.EMPTY
    }

    fun getVersionName(context: Context): String {
        val packageManager = context.packageManager
        if (packageManager != null) {
            try {
                val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
                if (packageInfo != null) return packageInfo.versionName
                else Logger.e("Package info is null")
            } catch (e: Exception) {
                Logger.e("Exception getting version name")
            }
        }
        return Constants.EMPTY
    }

    fun getVersionCode(context: Context): Long {
        val packageManager = context.packageManager
        if (packageManager != null) {
            try {
                val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
                if (packageInfo != null) return VersionUtils.versionCode(packageInfo)
                else Logger.e("Package info is null")
            } catch (e: Exception) {
                Logger.e("Exception getting version code")
            }
        }
        return 0
    }

    fun getPhoneModel(): String {
        return Build.MODEL + " " + Build.PRODUCT + "" + Build.BOARD
    }

    fun getAndroidVersion(): String {
        return ("Android:" + Build.VERSION.RELEASE + " " + Build.VERSION.INCREMENTAL)
    }

    fun getAppVersion(context: Context): String {
        var versionName = Constants.EMPTY
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            versionName = "Android " + packageInfo.versionName
        } catch (e: java.lang.Exception) {
            Logger.e("Error retrieving app version" + (e.localizedMessage ?: Constants.EMPTY))
        }
        return versionName
    }

    fun delay(milliseconds: Long, runnable: Runnable) {
        Handler(Looper.getMainLooper()).postDelayed(runnable, milliseconds)
    }

    /**
     * Generate a random number between min and max (included)
     *
     * @param min minimum number
     * @param max maximum number
     * @return the generated random number in range
     */
    fun randomNumber(min: Int, max: Int): Int {
        return (min..max).random()
    }

    /**
     * Generate a random string
     * @return the generated random string
     */
    fun randomString(): String {
        return UUID.randomUUID().toString()
    }

    fun copyToClipboard(context: Context, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(context.getString(R.string.copy), text)
        clipboard.setPrimaryClip(clip)
    }

    fun showKeyboard(view: View?) {
        if (view != null && !isKeyboardOpen(view)) {
            val inputManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            inputManager?.showSoftInput(view, InputMethodManager.SHOW_FORCED)
        }
    }

    fun hideKeyboard(view: View?) {
        if (view != null && isKeyboardOpen(view)) {
            val inputManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            inputManager?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun isKeyboardOpen(view: View?): Boolean {
        if (view != null) {
            val inputManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            return inputManager?.isAcceptingText ?: false
        }
        return false
    }

    fun getBatteryPercentage(context: Context): Int {
        val bm = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager?
        return bm?.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY) ?: Constants.INVALID
    }

    fun isCharging(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val bm = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager?
            bm?.isCharging ?: false
        } else {
            false
        }
    }

    fun getLocalIPAddress(): String? {
        try {
            val en = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val networkInterface = en.nextElement()
                val enu = networkInterface.inetAddresses
                while (enu.hasMoreElements()) {
                    val inetAddress = enu.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                        return inetAddress.getHostAddress()
                    }
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return null
    }

    /**
     * Convert a literal string to a new integer with the corresponding sequence of integers found
     *
     * @param string the input [String]
     * @return the output [Int]
     */
    fun numbersFromString(string: String?): Int {
        if (TextUtils.isEmpty(string)) {
            Logger.e("Called numbersFromString method with an invalid string")
            return Constants.INVALID
        }
        val builder = StringBuilder()
        string!!.forEach { char ->
            if (Character.isDigit(char)) builder.append(char)
        }
        var identifier = builder.toString()
        if (identifier.length > 16) {
            // remove the prefix to avoid cast to long exceptions
            identifier = identifier.substring(identifier.length - 16)
        }
        return abs(identifier.toLong().toInt())
    }
}