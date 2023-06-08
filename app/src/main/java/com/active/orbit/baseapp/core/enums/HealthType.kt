package com.active.orbit.baseapp.core.enums

import android.content.Context
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.utils.Constants

enum class HealthType(var id: Int, var title: Int, var responseOne: List<Int>, var responseTwo: List<Int>, var responseThree: List<Int>, var responseFour: List<Int>, var responseFive: List<Int>) {
    UNDEFINED(0, R.string.mobility_title, listOf(Constants.HEALTH_RESPONSE_ONE_ID,R.string.health_mobility_response_one), listOf(Constants.HEALTH_RESPONSE_TWO_ID,R.string.health_mobility_response_two), listOf(Constants.HEALTH_RESPONSE_THREE_ID,R.string.health_mobility_response_three), listOf(Constants.HEALTH_RESPONSE_FOUR_ID,R.string.health_mobility_response_four), listOf(Constants.HEALTH_RESPONSE_FIVE_ID,R.string.health_mobility_response_five)),
    MOBILITY(1, R.string.mobility_title, listOf(Constants.HEALTH_RESPONSE_ONE_ID,R.string.health_mobility_response_one), listOf(Constants.HEALTH_RESPONSE_TWO_ID,R.string.health_mobility_response_two), listOf(Constants.HEALTH_RESPONSE_THREE_ID,R.string.health_mobility_response_three),listOf(Constants.HEALTH_RESPONSE_FOUR_ID,R.string.health_mobility_response_four), listOf(Constants.HEALTH_RESPONSE_FIVE_ID,R.string.health_mobility_response_five)),
    SELF_CARE(2, R.string.health_self_care, listOf(Constants.HEALTH_RESPONSE_ONE_ID,R.string.health_self_care_response_one), listOf(Constants.HEALTH_RESPONSE_TWO_ID,R.string.health_self_care_response_two), listOf(Constants.HEALTH_RESPONSE_THREE_ID,R.string.health_self_care_response_three),listOf(Constants.HEALTH_RESPONSE_FOUR_ID,R.string.health_self_care_response_four), listOf(Constants.HEALTH_RESPONSE_FIVE_ID,R.string.health_self_care_response_five)),
    USUAL_ACTIVITIES(3, R.string.health_usual_activities,listOf(Constants.HEALTH_RESPONSE_ONE_ID,R.string.health_usual_activities_response_one), listOf(Constants.HEALTH_RESPONSE_TWO_ID,R.string.health_usual_activities_response_two), listOf(Constants.HEALTH_RESPONSE_THREE_ID,R.string.health_usual_activities_response_three), listOf(Constants.HEALTH_RESPONSE_FOUR_ID,R.string.health_usual_activities_response_four), listOf(Constants.HEALTH_RESPONSE_FIVE_ID,R.string.health_usual_activities_response_five)),
    PAIN(4, R.string.health_pain,listOf(Constants.HEALTH_RESPONSE_ONE_ID,R.string.health_pain_response_one), listOf(Constants.HEALTH_RESPONSE_TWO_ID,R.string.health_pain_response_two), listOf(Constants.HEALTH_RESPONSE_THREE_ID,R.string.health_pain_response_three), listOf(Constants.HEALTH_RESPONSE_FOUR_ID,R.string.health_pain_response_four), listOf(Constants.HEALTH_RESPONSE_FIVE_ID,R.string.health_pain_response_five)),
    ANXIETY(5, R.string.health_anxiety,listOf(Constants.HEALTH_RESPONSE_ONE_ID,R.string.health_anxiety_response_one), listOf(Constants.HEALTH_RESPONSE_TWO_ID,R.string.health_anxiety_response_two), listOf(Constants.HEALTH_RESPONSE_THREE_ID,R.string.health_anxiety_response_three), listOf(Constants.HEALTH_RESPONSE_FOUR_ID,R.string.health_anxiety_response_four), listOf(Constants.HEALTH_RESPONSE_FIVE_ID,R.string.health_anxiety_response_five)),
    SCORE(6, R.string.health_score, listOf(), listOf(), listOf(), listOf(), listOf());


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

    fun getResponse(responseID: Int, context: Context): String {

        when(responseID) {
            Constants.HEALTH_RESPONSE_ONE_ID -> {
                return context.getString(responseOne[1])
            }
            Constants.HEALTH_RESPONSE_TWO_ID -> {
                return context.getString(responseTwo[1])
            }
            Constants.HEALTH_RESPONSE_THREE_ID -> {
                return context.getString(responseThree[1])
            }
            Constants.HEALTH_RESPONSE_FOUR_ID -> {
                return context.getString(responseFour[1])
            }
            Constants.HEALTH_RESPONSE_FIVE_ID -> {
                return context.getString(responseFive[1])
            }
        }
        return Constants.EMPTY
    }

}