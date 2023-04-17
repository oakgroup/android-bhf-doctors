package com.active.orbit.baseapp.core.listeners

import com.active.orbit.baseapp.core.deserialization.UserRegistrationMap

interface UserRegistrationListener {

    fun onSuccess(map: UserRegistrationMap)

    fun onError()
}