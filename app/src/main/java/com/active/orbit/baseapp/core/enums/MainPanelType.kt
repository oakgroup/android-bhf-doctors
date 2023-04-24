package com.active.orbit.baseapp.core.enums

import com.active.orbit.baseapp.R

enum class MainPanelType(var id: Int, var description: Int, var icon: Int, var buttonText: Int) {
    UNDEFINED(0, R.string.patient_registration_intro, R.drawable.ic_register, R.string.register_patient),
    REGISTER(1, R.string.patient_registration_intro, R.drawable.ic_register, R.string.register_patient),
    TOUR(2, R.string.tour_description, R.drawable.ic_info, R.string.take_tour),
    START_PROGRAMME(3, R.string.patient_start_programme, R.drawable.ic_start_programme, R.string.start_programme),
    START_PROGRAMME_WITH_NAME(4, R.string.patient_start_programme_with_name, R.drawable.ic_start_programme, R.string.start_programme),
    PRESCRIPTIONS_DOCTOR(5, R.string.add_patient_prescriptions, R.drawable.ic_medicines, R.string.add_prescriptions),
    PRESCRIPTIONS_PATIENT(6, R.string.see_prescriptions, R.drawable.ic_medicines, R.string.my_prescriptions),
    SYMPTOMS_PATIENT(7, R.string.report_symptom, R.drawable.ic_symptoms, R.string.symptoms),
    ACTIVITY_PATIENT(8, R.string.check_activity, R.drawable.ic_activity, R.string.activity);

    companion object {
        fun getById(id: Int): MainPanelType {
            for (item in values()) {
                if (item.id == id) return item
            }
            return UNDEFINED
        }
    }
}