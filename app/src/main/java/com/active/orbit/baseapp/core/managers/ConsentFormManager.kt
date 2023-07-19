package com.active.orbit.baseapp.core.managers

import androidx.core.text.HtmlCompat
import com.active.orbit.baseapp.core.database.tables.TableConsentQuestions
import com.active.orbit.baseapp.core.deserialization.RetrieveConsentFormMap
import com.active.orbit.baseapp.core.listeners.ResultListener
import com.active.orbit.baseapp.core.network.Api
import com.active.orbit.baseapp.core.network.Connection
import com.active.orbit.baseapp.core.network.ConnectionListener
import com.active.orbit.baseapp.core.network.Network
import com.active.orbit.baseapp.core.network.WebService
import com.active.orbit.baseapp.core.preferences.engine.Preferences
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

object ConsentFormManager : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    fun retrieveConsentForm(activity: BaseActivity, listener: ResultListener? = null) {


        val webService = WebService(activity, Api.RETRIEVE_CONSENT_FORM)
        webService.urlString = "http://52.56.150.239/v2/user_consent_form"
        webService.method = Network.GET


        Connection(webService, object : ConnectionListener {
            override fun onConnectionSuccess(tag: Int, response: String) {
                val responseFix = response.removePrefix("[").removeSuffix("]")
                var map: RetrieveConsentFormMap? = null
                try {
                    map = Gson().fromJson<RetrieveConsentFormMap>(responseFix, object : TypeToken<RetrieveConsentFormMap>() {}.type)
                } catch (e: JsonSyntaxException) {
                    Logger.e("Error parsing consent form json response")
                    listener?.onResult(false)
                } catch (e: IllegalStateException) {
                    Logger.e("Error consent form json response")
                    listener?.onResult(false)
                }

                if (map?.isValid() == true) {
                    Logger.d("Consent form downloaded from server $map")

                    background {
                        TableConsentQuestions.truncate(activity)
                        TableConsentQuestions.upsert(activity, map.dbQuestions())
                        main {
                            //TODO check for version. If version has changed show a dialog that they need to consent to the new consent form
                            Preferences.user(activity).consentFormText = HtmlCompat.fromHtml(map.consentText!!, HtmlCompat.FROM_HTML_MODE_COMPACT).toString()
                            Preferences.user(activity).consentVersion = map.version!!
                            listener?.onResult(true)
                        }
                    }
                } else {
                    Logger.d("Error retrieving consent form from server")
                    listener?.onResult(false)

                }
            }

            override fun onConnectionError(tag: Int) {
                Logger.d("Error retrieving consent form from server")
                listener?.onResult(false)
            }

        }).connect()
    }
}
