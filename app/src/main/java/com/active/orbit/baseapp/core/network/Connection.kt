package com.active.orbit.baseapp.core.network

import android.text.TextUtils
import androidx.annotation.WorkerThread
import com.active.orbit.baseapp.BuildConfig
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.core.utils.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.io.*
import java.net.HttpURLConnection
import uk.ac.shef.tracker.core.utils.background
import uk.ac.shef.tracker.core.utils.main
import kotlin.coroutines.CoroutineContext

/**
 * Class used to build a connection using an instance of [WebService]
 *
 * @author omar.brugna
 */
class Connection(private val webService: WebService, private val listener: ConnectionListener) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    private var mInputStream: InputStream? = null
    private var mInputStreamReader: InputStreamReader? = null
    private var mBufferedReader: BufferedReader? = null

    var tag = Constants.INVALID

    private var timeoutExtended = false
    fun timeoutExtended(boolean: Boolean): Connection {
        timeoutExtended = boolean
        return this
    }

    fun connect() {
        background {
            initConnection()
        }
    }

    @WorkerThread
    private fun initConnection() {
        main {
            listener.onConnectionStarted(tag)
        }

        var response: String? = null
        try {
            response = startConnection()
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e("Exception on connection ${webService.urlString} ${e.localizedMessage}")
        }

        if (response != null) {
            Logger.i("Connection response [${webService.urlString}]: $response")
            main {
                listener.onConnectionSuccess(tag, response)
            }
        } else {
            Logger.w("Connection response [${webService.urlString}] is null")
            main {
                listener.onConnectionError(tag)
            }
        }

        main {
            // always notify completed request
            listener.onConnectionCompleted(tag)
        }
    }

    @Throws(IOException::class)
    private fun startConnection(): String? {

        if (BuildConfig.DEBUG) {
            val params = StringBuilder()
            if (!TextUtils.isEmpty(webService.params)) {
                val divider = "**********\n"
                params.append(divider)
                params.append(webService.params!!)
                params.append("\n")
                params.append(divider)
            }
            Logger.d(
                "Connection " +
                        "\nApi: " + webService.urlString +
                        "\nMethod: " + webService.method +
                        "\nTimeout: " + if (timeoutExtended) "Extended" else "Normal" +
                        "\nParams: " + params.toString()
            )
        }

        val connection = webService.url.openConnection() as HttpURLConnection
        connection.connectTimeout = if (timeoutExtended) Network.CONNECTION_TIMEOUT_EXTENDED else Network.CONNECTION_TIMEOUT
        connection.readTimeout = if (timeoutExtended) Network.SOCKET_TIMEOUT_EXTENDED else Network.SOCKET_TIMEOUT
        connection.useCaches = false
        connection.requestMethod = webService.method
        if (!TextUtils.isEmpty(webService.contentType)) connection.setRequestProperty(Network.CONTENT_TYPE, webService.contentType)
        if (!TextUtils.isEmpty(webService.connection)) connection.setRequestProperty(Network.CONNECTION, webService.connection)
        if (!TextUtils.isEmpty(webService.cacheControl)) connection.setRequestProperty(Network.CACHE_CONTROL, webService.cacheControl)
        if (webService.headers.isNotEmpty()) {
            for (header in webService.headers)
                connection.setRequestProperty(header.key, header.value)
        }

        if (!TextUtils.isEmpty(webService.params)) {
            val outputStream = BufferedOutputStream(connection.outputStream)
            try {
                if (!TextUtils.isEmpty(webService.params)) {
                    outputStream.write(webService.params!!.toByteArray(charset(webService.encoding)))
                } else {
                    Logger.w("Empty parameters on do output connection")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Logger.e("Exception writing params")
            } finally {
                try {
                    outputStream.flush()
                    outputStream.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Logger.e("Exception closing params writer")
                }
            }
        }

        if (connection.responseCode != HttpURLConnection.HTTP_OK) {
            Logger.e("Unexpected HTTP response: " + connection.responseCode + " " + connection.responseMessage)
            return null
        }

        try {
            val inputStream = connection.inputStream

            // read the response
            mInputStream = BufferedInputStream(inputStream)
            @Suppress("KotlinConstantConditions")
            if (mInputStream != null) {
                mInputStreamReader = InputStreamReader(mInputStream!!, Network.ENCODING_UTF8)
                mBufferedReader = BufferedReader(mInputStreamReader!!)
                val stringBuilder = StringBuilder()
                var line: String?
                do {
                    line = mBufferedReader!!.readLine()
                    if (line == null) break
                    stringBuilder.append(line).append("\n")
                } while (true)

                return stringBuilder.toString().trim { it <= ' ' }

            } else Logger.e("Input stream is null")

        } catch (e: IOException) {
            Logger.w("Connection error: " + e.localizedMessage)
            e.printStackTrace()
        } finally {
            // clean up
            connection.disconnect()
            try {
                if (mInputStream != null)
                    mInputStream!!.close()
                if (mInputStreamReader != null)
                    mInputStreamReader!!.close()
                if (mBufferedReader != null)
                    mBufferedReader!!.close()
            } catch (ignored: IOException) {

            }
        }
        return null
    }
}