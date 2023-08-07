package com.active.orbit.baseapp.core.enums

import com.active.orbit.baseapp.R

enum class SuccessMessageType(var id: Int, var message: Int) {
    UNDEFINED(0, R.string.success),
    REGISTRATION(4, R.string.success_registration),
    SYMPTOM_REPORTED(5, R.string.success_symptom_report),
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