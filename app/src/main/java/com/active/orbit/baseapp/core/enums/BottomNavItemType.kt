package com.active.orbit.baseapp.core.enums

enum class BottomNavItemType(var key: Int) {
    MAIN(0),
    SYMPTOMS(1),
    HEALTH(2);

    companion object {

        fun getByKey(key: Int): BottomNavItemType {
            for (item in values()) {
                if (item.key == key) return item
            }
            return MAIN
        }
    }
}