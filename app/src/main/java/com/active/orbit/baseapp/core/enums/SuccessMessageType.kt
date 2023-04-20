package com.active.orbit.baseapp.core.enums

import com.active.orbit.baseapp.R

enum class SuccessMessageType(var id: Int, var message: Int) {
    UNDEFINED(0, R.string.success),
    LOCATION_PERMISSION(1, R.string.success_location_permissions),
    BATTERY_PERMISSION(2, R.string.success_battery_settings),
    UNUSED_RESTRICTIONS(3, R.string.success_disable_restrictions),
    REGISTRATION(4, R.string.success_registration),
    SYMPTOM_REPORTED(5, R.string.success_symptom_report),
    ON_BOARDING_COMPLETED(6, R.string.success_on_boarding),
    MEDICINE_REPORTED(7, R.string.success_medicine_reported),
    DISMISS_PATIENT(8, R.string.success_dismiss_patient);


    companion object {
        fun getById(id: Int): SuccessMessageType {
            for (item in values()) {
                if (item.id == id) return item
            }
            return UNDEFINED
        }
    }
}