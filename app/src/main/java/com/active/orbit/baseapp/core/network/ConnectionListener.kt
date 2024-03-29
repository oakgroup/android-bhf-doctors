package com.active.orbit.baseapp.core.network

/**
 * Utility interface to get a connection result
 */
interface ConnectionListener {

    fun onConnectionStarted(tag: Int) {}

    fun onConnectionSuccess(tag: Int, response: String) {}

    fun onConnectionError(tag: Int) {}

    fun onConnectionCompleted(tag: Int) {}
}