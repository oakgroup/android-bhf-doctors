package com.active.orbit.baseapp.core.routing.enums

import android.app.Activity

enum class ResultCode(var value: Int) {
    RESULT_OK(Activity.RESULT_OK),
    RESULT_CANCELED(Activity.RESULT_CANCELED),
    RESULT_DELETED(2)
}
