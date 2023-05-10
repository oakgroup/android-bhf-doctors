package com.active.orbit.baseapp.core.enums

import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.utils.Constants

enum class SecondaryPanelType(var id: Int, var title: Int, var isProgressView: Boolean, var image: Int, var detailsOne: Int, var detailsTwo: Int, var detailsThree: Int) {
    UNDEFINED(0, R.string.daily_activity_title, true, Constants.INVALID, R.string.daily_activity_details_one, R.string.daily_activity_details_two, R.string.daily_activity_details_three),
    ACTIVITY(0, R.string.daily_activity_title, true, Constants.INVALID, R.string.daily_activity_details_one, R.string.daily_activity_details_two, R.string.daily_activity_details_three),
    MOBILITY(0, R.string.mobility_title, false, R.drawable.bg_mobility, R.string.mobility_details_one, R.string.mobility_details_two, Constants.INVALID),
    PHYSIOLOGY(0, R.string.physiology_title, false, R.drawable.bg_physiology, R.string.physiology_details_one, R.string.physiology_details_two, Constants.INVALID);


    companion object {
        fun getById(id: Int): SecondaryPanelType {
            for (item in values()) {
                if (item.id == id) return item
            }
            return UNDEFINED
        }
    }
}