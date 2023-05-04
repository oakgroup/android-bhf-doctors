package com.active.orbit.baseapp.core.enums

import com.active.orbit.baseapp.R

enum class MainPanelType(var id: Int, var description: Int, var icon: Int, var buttonText: Int, var image: Int) {
    UNDEFINED(0, R.string.patient_registration_intro, R.drawable.ic_logo_secondary, R.string.register_patient, R.drawable.bg_tour_panel),
    REGISTER(1, R.string.patient_registration_intro, R.drawable.ic_logo_secondary, R.string.register_patient, R.drawable.bg_register_panel),
    TOUR(2, R.string.tour_description, R.drawable.ic_logo_secondary, R.string.take_tour, R.drawable.bg_register_panel),
    START_PROGRAMME(3, R.string.patient_start_programme, R.drawable.ic_logo_secondary, R.string.start_programme, R.drawable.bg_register_panel),
    START_PROGRAMME_WITH_NAME(4, R.string.patient_start_programme_with_name, R.drawable.ic_logo_secondary, R.string.start_programme, R.drawable.bg_register_panel),
    PRESCRIPTIONS_DOCTOR(5, R.string.add_patient_prescriptions, R.drawable.ic_logo_secondary, R.string.add_prescriptions, R.drawable.bg_register_panel),
    PRESCRIPTIONS_PATIENT(6, R.string.see_prescriptions, R.drawable.ic_logo_secondary, R.string.my_prescriptions, R.drawable.bg_register_panel),
    SYMPTOMS_PATIENT(7, R.string.report_symptom, R.drawable.ic_logo_secondary, R.string.symptoms, R.drawable.bg_register_panel),
    ACTIVITY_PATIENT(8, R.string.check_activity, R.drawable.ic_logo_secondary, R.string.activity, R.drawable.bg_register_panel);

    companion object {
        fun getById(id: Int): MainPanelType {
            for (item in values()) {
                if (item.id == id) return item
            }
            return UNDEFINED
        }
    }
}