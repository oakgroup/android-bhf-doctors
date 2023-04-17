package com.active.orbit.baseapp.design.protocols

import com.active.orbit.baseapp.design.activities.engine.BaseActivity

interface ActivityProvider {

    fun getActivity(): Class<out BaseActivity>
}