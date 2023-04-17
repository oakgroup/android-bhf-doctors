package com.active.orbit.baseapp.core.network

import android.content.Context
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.utils.BaseException

/**
 * Utility enum to declare api urls
 *
 * @author omar.brugna
 */
enum class Api(private var apiUrl: Int) {
    EMPTY(R.string.api_empty),
    IMAGES_GET(R.string.api_images_get), // DEMO_CODE!
    USER_REGISTRATION(R.string.api_user_registration);

    private var params = ArrayList<String>()

    fun clearParams() {
        params.clear()
    }

    fun addParam(param: String) {
        params.add(param)
    }

    fun getUrl(context: Context): String {
        if (params.isEmpty()) return context.getString(apiUrl)
        if (params.size == 1) return context.getString(apiUrl, params[0])
        if (params.size == 2) return context.getString(apiUrl, params[0], params[1])
        if (params.size == 3) return context.getString(apiUrl, params[0], params[1], params[2])
        if (params.size == 4) return context.getString(apiUrl, params[0], params[1], params[2], params[3])
        if (params.size == 5) return context.getString(apiUrl, params[0], params[1], params[2], params[3], params[4])
        throw BaseException("Current implementation actually supports at least 5 parameters, please add the new cases")
    }
}