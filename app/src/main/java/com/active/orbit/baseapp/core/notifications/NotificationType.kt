package com.active.orbit.baseapp.core.notifications

import androidx.core.app.NotificationManagerCompat
import com.active.orbit.baseapp.R
import com.active.orbit.baseapp.core.utils.Constants

enum class NotificationType(
    var id: Int,
    var channelName: String,
    var channelImportance: Int,
    var title: String,
    var body: String,
    var smallIcon: Int,
    var largeIcon: Int,
    var color: Int
) {
    UNDEFINED(
        0,
        Constants.EMPTY,
        NotificationManagerCompat.IMPORTANCE_NONE,
        Constants.EMPTY,
        Constants.EMPTY,
        Constants.INVALID,
        Constants.INVALID,
        Constants.INVALID
    ),

    //TODO add correct notification logo
    HEALTH(
        1,
        "Health",
        NotificationManagerCompat.IMPORTANCE_HIGH,
        "How is your health today?",
        "Complete the questionnaire to let us know how you feel today?",
        R.drawable.ic_activity,
        R.drawable.ic_activity,
        R.color.colorPrimary
    );



    companion object {
        fun getById(id: Int): NotificationType {
            for (item in values()) {
                if (item.id == id) return item
            }
            return UNDEFINED
        }


        fun getAll(): ArrayList<NotificationType> {
            val notificationTypes = ArrayList<NotificationType>()
            for (item in values()) {
                if (item != UNDEFINED)
                    notificationTypes.add(item)
            }

            return notificationTypes
        }

        fun getRandomNotification(scheduledNotification: NotificationType): NotificationType {
            val randomNotification = values().random()

            if (randomNotification == UNDEFINED || randomNotification == scheduledNotification) {
                return getRandomNotification(scheduledNotification)
            }
            return randomNotification
        }
    }

}