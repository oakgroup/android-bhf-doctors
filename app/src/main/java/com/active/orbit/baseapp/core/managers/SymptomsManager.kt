package com.active.orbit.baseapp.core.managers

import com.active.orbit.baseapp.core.database.models.DBSymptom
import com.active.orbit.baseapp.core.deserialization.UploadSymptomsMap
import com.active.orbit.baseapp.core.listeners.ResultListener
import com.active.orbit.baseapp.core.network.Api
import com.active.orbit.baseapp.core.network.Connection
import com.active.orbit.baseapp.core.network.ConnectionListener
import com.active.orbit.baseapp.core.network.WebService
import com.active.orbit.baseapp.core.preferences.engine.Preferences
import com.active.orbit.baseapp.core.serialization.UploadSymptomsRequest
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken

object SymptomsManager {

    fun uploadSymptoms(activity: BaseActivity, symptoms: ArrayList<DBSymptom>, listener: ResultListener? = null) {

        if (symptoms.isEmpty()) {
            Logger.d("No symptoms to upload to server")
            listener?.onResult(false)
            return
        }

        val request = UploadSymptomsRequest()
        request.userId = Preferences.user(activity).idUser

        for (symptom in symptoms) {
            val symptomRequest = UploadSymptomsRequest.UploadSymptomRequest()
            symptomRequest.timeInMsecs = symptom.symptomDateTime
            symptomRequest.symptomId = symptom.idSymptom
            symptomRequest.symptomValue = symptom.severity?.symptomValue?.toIntOrNull() ?: 0
            symptomRequest.symptomResponse = symptom.severity?.symptomResponse
            symptomRequest.description = symptom.symptomDetails
            request.symptoms.add(symptomRequest)
        }

        val webService = WebService(activity, Api.INSERT_SYMPTOMS)
        webService.params = Gson().toJson(request)

        Connection(webService, object : ConnectionListener {
            override fun onConnectionSuccess(tag: Int, response: String) {
                var map: UploadSymptomsMap? = null
                try {
                    map = Gson().fromJson<UploadSymptomsMap>(response, object : TypeToken<UploadSymptomsMap>() {}.type)
                } catch (e: JsonSyntaxException) {
                    Logger.e("Error parsing symptoms json response")
                } catch (e: IllegalStateException) {
                    Logger.e("Error symptoms json response")
                }

                if (map?.isValid() == true) {
                    if (map.inserted!! >= symptoms.size) {
                        Logger.d("Symptoms uploaded to server ${map.inserted} success")
                        listener?.onResult(true)
                    } else {
                        Logger.d("Symptoms uploaded to server error ${map.inserted} success")
                        listener?.onResult(false)
                    }
                } else {
                    Logger.e("Symptoms uploaded to server invalid")
                    listener?.onResult(false)
                }
            }

            override fun onConnectionError(tag: Int) {
                Logger.e("Error uploading symptoms to server")
                listener?.onResult(false)
            }

        }).connect()
    }
}