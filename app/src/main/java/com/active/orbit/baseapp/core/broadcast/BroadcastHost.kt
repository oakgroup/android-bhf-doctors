package com.active.orbit.baseapp.core.broadcast

import android.content.Context

interface BroadcastHost {

    fun broadcastRegister(broadcast: BaseBroadcast) {}

    fun broadcastUnregister() {}

    fun broadcastIdentifier(): Int

    fun getContext(): Context?
}