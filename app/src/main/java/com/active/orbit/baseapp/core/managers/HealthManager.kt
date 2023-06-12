package com.active.orbit.baseapp.core.managers

import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.database.models.DBHealth
import com.active.orbit.baseapp.core.database.tables.TableHealth
import com.active.orbit.baseapp.core.deserialization.UploadHealthMap
import com.active.orbit.baseapp.core.deserialization.UploadSymptomsMap
import com.active.orbit.baseapp.core.listeners.ResultListener
import com.active.orbit.baseapp.core.network.Api
import com.active.orbit.baseapp.core.network.Connection
import com.active.orbit.baseapp.core.network.ConnectionListener
import com.active.orbit.baseapp.core.network.WebService
import com.active.orbit.baseapp.core.preferences.engine.Preferences
import com.active.orbit.baseapp.core.routing.Router
import com.active.orbit.baseapp.core.routing.enums.ResultCode
import com.active.orbit.baseapp.core.serialization.UploadHealthRequest
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.core.utils.ThreadHandler.backgroundThread
import com.active.orbit.baseapp.core.utils.ThreadHandler.mainThread
import com.active.orbit.baseapp.design.activities.engine.Activities
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.utils.UiUtils
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import kotlin.math.acos

object HealthManager {

    fun uploadHealth(activity: BaseActivity, health: DBHealth, listener: ResultListener? = null) {

        if (!health.isValid()) {
            Logger.d("No health response to upload to server")
            listener?.onResult(false)
            return
        }

        val request = UploadHealthRequest()
        request.userId = Preferences.user(activity).idUser

        val healthRequest = UploadHealthRequest.UploadHealthResponse()
        healthRequest.healthID = health.healthID
        healthRequest.timestamp = health.healthTimestamp
        healthRequest.mobility = health.healthMobility
        healthRequest.selfCare = health.healthSelfCare
        healthRequest.usualActivities = health.healthActivities
        healthRequest.pain = health.healthPain
        healthRequest.anxiety = health.healthAnxiety
        healthRequest.score = health.healthSelfCare

        request.healthResponse = healthRequest


        val webService = WebService(activity, Api.INSERT_HEALTH)
        webService.params = Gson().toJson(request)

        webService.urlString = "http://52.56.150.239/v2/user_health_questionnaire"

        Connection(webService, object : ConnectionListener {
            override fun onConnectionSuccess(tag: Int, response: String) {
                var map: UploadHealthMap? = null
                try {
                    map = Gson().fromJson<UploadHealthMap>(response, object : TypeToken<UploadHealthMap>() {}.type)
                } catch (e: JsonSyntaxException) {
                    Logger.e("Error parsing health json response")
                } catch (e: IllegalStateException) {
                    Logger.e("Error health json response")
                }

                if (map?.isValid() == true) {
                    if (map.userId!! == Preferences.user(activity).idUser) {
                        health.uploaded = true
                        TableHealth.upsert(activity, health)

                        Logger.d("Health uploaded to server for user id ${map.userId} success")
                        listener?.onResult(true)
                    } else {
                        Logger.d("Health uploaded to server error for user id ${map.userId}")
                        listener?.onResult(false)
                    }
                } else {
                    Logger.e("Health uploaded to server invalid")
                    listener?.onResult(false)
                }
            }

            override fun onConnectionError(tag: Int) {
                Logger.e("Error uploading health to server")
                listener?.onResult(false)
            }

        }).connect()
    }

    fun checkForNotUploaded(activity: BaseActivity) {
        var modelsNotUploaded: List<DBHealth>
        backgroundThread{
            modelsNotUploaded= TableHealth.getNotUploaded(activity, false)

            mainThread{
                for (model in modelsNotUploaded) {
                    uploadHealth(activity, model)
                }
            }
        }

    }

}