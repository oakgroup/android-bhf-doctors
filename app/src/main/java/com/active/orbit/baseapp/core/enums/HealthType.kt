package com.active.orbit.baseapp.core.enums

import android.content.Context
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.utils.Constants

enum class HealthType(var id: Int, var title: Int, var responseOne: Int, var responseTwo: Int, var responseThree: Int) {
    UNDEFINED(0, R.string.mobility_title, R.string.health_mobility_response_one, R.string.health_mobility_response_two, R.string.health_mobility_response_three),
    MOBILITY(1, R.string.mobility_title, R.string.health_mobility_response_one, R.string.health_mobility_response_two, R.string.health_mobility_response_three),
    SELF_CARE(2, R.string.health_self_care, R.string.health_self_care_response_one, R.string.health_self_care_response_two, R.string.health_self_care_response_three),
    USUAL_ACTIVITIES(3, R.string.mobility_title, R.string.health_usual_activities_response_one, R.string.health_usual_activities_response_two, R.string.health_usual_activities_response_three),
    PAIN(4, R.string.health_pain, R.string.health_pain_response_one, R.string.health_pain_response_two, R.string.health_pain_response_three),
    ANXIETY(5, R.string.health_anxiety, R.string.health_anxiety_response_one, R.string.health_anxiety_response_two, R.string.health_anxiety_response_three),
    SCORE(6, R.string.health_score, Constants.INVALID, Constants.INVALID, Constants.INVALID);


    companion object {
        fun getById(id: Int): HealthType {
            for (item in values()) {
                if (item.id == id) return item
            }
            return UNDEFINED
        }


    }

    fun getDescription(context: Context): String {
        if (id == USUAL_ACTIVITIES.id) {
            return context.getString(R.string.health_description, context.getString(title)) + "\n" + context.getString(R.string.health_usual_activities_example)
        } else if (id == SCORE.id) {
            return context.getString(R.string.health_score_description)
        }
        return context.getString(R.string.health_description, context.getString(title))
    }

}