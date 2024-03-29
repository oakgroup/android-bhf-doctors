package com.active.orbit.baseapp.core.managers

import com.active.orbit.baseapp.core.database.models.DBHealth
import com.active.orbit.baseapp.core.database.tables.TableHealth
import com.active.orbit.baseapp.core.deserialization.UploadHealthMap
import com.active.orbit.baseapp.core.listeners.ResultListener
import com.active.orbit.baseapp.core.network.Api
import com.active.orbit.baseapp.core.network.Connection
import com.active.orbit.baseapp.core.network.ConnectionListener
import com.active.orbit.baseapp.core.network.WebService
import com.active.orbit.baseapp.core.preferences.engine.Preferences
import com.active.orbit.baseapp.core.serialization.UploadHealthRequest
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import uk.ac.shef.tracker.core.utils.background
import uk.ac.shef.tracker.core.utils.main
import kotlin.coroutines.CoroutineContext

object HealthManager: CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

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
        healthRequest.score = health.healthScore

        request.healthResponse = healthRequest

        val webService = WebService(activity, Api.INSERT_HEALTH)
        webService.params = Gson().toJson(request)

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
                    if (map.result == "OK") {
                        background {
                            health.uploaded = true
                            TableHealth.upsert(activity, health)
                            main {
                                Logger.d("Health uploaded to server for user id ${Preferences.user(activity).idUser} success")
                                listener?.onResult(true)
                            }
                        }
                    } else {
                        Logger.d("Health uploaded to server error for user id ${Preferences.user(activity).idUser}")
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
        background {
            modelsNotUploaded = TableHealth.getNotUploaded(activity, false)
            main {
                for (model in modelsNotUploaded) {
                    uploadHealth(activity, model)
                    Logger.d("Upload Health: ${model.description()}")
                }
            }
        }
    }
}