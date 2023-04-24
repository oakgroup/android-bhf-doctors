package com.active.orbit.baseapp.core.managers

import com.active.orbit.baseapp.core.database.tables.TablePrograms
import com.active.orbit.baseapp.core.database.tables.TableSeverities
import com.active.orbit.baseapp.core.database.tables.TableSymptoms
import com.active.orbit.baseapp.core.deserialization.ProgramsMap
import com.active.orbit.baseapp.core.listeners.ResultListener
import com.active.orbit.baseapp.core.network.*
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.core.utils.ThreadHandler.backgroundThread
import com.active.orbit.baseapp.core.utils.ThreadHandler.mainThread
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken

object ProgrammesManager {

    fun downloadProgrammes(activity: BaseActivity, listener: ResultListener? = null) {
        val webService = WebService(activity, Api.RETRIEVE_PROGRAMMES)
        webService.method = Network.GET

        Connection(webService, object : ConnectionListener {
            override fun onConnectionSuccess(tag: Int, response: String) {
                var map: ProgramsMap? = null
                try {
                    map = Gson().fromJson<ProgramsMap>(response, object : TypeToken<ProgramsMap>() {}.type)
                } catch (e: JsonSyntaxException) {
                    Logger.e("Error parsing programmes json response")
                } catch (e: IllegalStateException) {
                    Logger.e("Error programmes json response")
                }

                if (map?.isValid() == true) {
                    Logger.d("Programmes downloaded from server ${map.data.size}")
                    backgroundThread {

                        // clear data from database
                        TablePrograms.truncate(activity)
                        TableSymptoms.truncate(activity)
                        TableSeverities.truncate(activity)

                        // save data to database
                        TablePrograms.upsert(activity, map.dbPrograms())
                        TableSymptoms.upsert(activity, map.dbSymptoms())
                        TableSeverities.upsert(activity, map.dbOptions())

                        mainThread {
                            listener?.onResult(true)
                        }
                    }
                } else {
                    Logger.e("Error downloading programmes from server")
                    listener?.onResult(false)
                }
            }

            override fun onConnectionError(tag: Int) {
                Logger.e("Error downloading programmes from server")
                listener?.onResult(false)
            }

        }).connect()
    }
}