package com.active.orbit.baseapp.core.enums

enum class WearableMessageType(var key: String) {
    UNDEFINED("UNDEFINED"),
    SYNC("sync"),
    DISMISS("dismiss"),
    DISMISS_CANCEL("dismiss_cancel"),
    UPLOAD("upload");

    companion object {

        fun getByKey(key: String): WearableMessageType {
            for (item in values()) {
                if (item.key == key) return item
            }
            return UNDEFINED
        }
    }
}