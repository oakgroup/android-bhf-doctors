package com.active.orbit.baseapp.core.managers

import com.active.orbit.baseapp.core.deserialization.UserRegistrationMap
import com.active.orbit.baseapp.core.listeners.UserRegistrationListener
import com.active.orbit.baseapp.core.network.Api
import com.active.orbit.baseapp.core.network.Connection
import com.active.orbit.baseapp.core.network.ConnectionListener
import com.active.orbit.baseapp.core.network.WebService
import com.active.orbit.baseapp.core.serialization.UserRegistrationRequest
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken

object UserManager {

    fun registerUser(activity: BaseActivity, request: UserRegistrationRequest, listener: UserRegistrationListener? = null) {
        if (!request.isValid()) {
            Logger.e("Invalid user registration request")
            listener?.onError()
            return
        }

        val webService = WebService(activity, Api.USER_REGISTRATION)
        // TODO remove when dev base url will work again
        webService.urlString = "http://52.56.150.239/v2/user_registration"
        webService.params = Gson().toJson(request)

        Connection(webService, object : ConnectionListener {
            override fun onConnectionSuccess(tag: Int, response: String) {
                try {
                    val map = Gson().fromJson<UserRegistrationMap>(response, object : TypeToken<UserRegistrationMap>() {}.type)
                    if (map.isValid()) {
                        listener?.onSuccess(map)
                    } else {
                        Logger.d("Error registering user ${map.dataItem.userId.id}")
                        listener?.onError()
                    }
                } catch (e: JsonSyntaxException) {
                    Logger.e("Error registering user json response")
                    listener?.onError()
                } catch (e: IllegalStateException) {
                    Logger.e("Error registering user json response")
                    listener?.onError()
                }
            }

            override fun onConnectionError(tag: Int) {
                Logger.e("Registering user to server error")
                listener?.onError()
            }

        }).connect()
    }
}