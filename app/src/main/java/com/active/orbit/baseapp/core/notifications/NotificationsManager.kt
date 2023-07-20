package com.active.orbit.baseapp.core.notifications

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.os.SystemClock
import android.provider.Settings
import androidx.annotation.WorkerThread
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.core.utils.Logger
import com.active.orbit.baseapp.core.utils.TimeUtils
import com.active.orbit.baseapp.design.activities.engine.Activities

class NotificationsManager : BroadcastReceiver() {

    companion object {

        internal const val EXTRA_NOTIFICATION_ID = "notification_id"
        private val VIBRATION_PATTERN = longArrayOf(0, 1000, 1000, 1000, 1000, 1000)


        fun scheduleNotification(context: Context, delay: Long, notificationType: NotificationType) {

            cancelNotification(context, notificationType, true)

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
            if (alarmManager != null) {
                val notificationIntent = Intent(context, NotificationsManager::class.java)
                notificationIntent.putExtra(EXTRA_NOTIFICATION_ID, notificationType.id)
                val pendingIntent = PendingIntent.getBroadcast(context, notificationType.id, notificationIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
                val futureInMillis = TimeUtils.getCurrent().timeInMillis + delay

                alarmManager.setAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent)
                Logger.d("Notification with id ${notificationType.id} scheduled at " + TimeUtils.format(TimeUtils.getCurrent(futureInMillis), Constants.DATE_FORMAT_FULL))

            } else {
                Logger.e("Alarm manager is null on schedule notification")
            }

        }


        private fun createNotificationChannel(context: Context, notificationType: NotificationType) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                val channelId = "${context.packageName}-${notificationType.name}"
                val channel = NotificationChannel(channelId, notificationType.channelName, notificationType.channelImportance)
                channel.description = notificationType.channelName
                channel.vibrationPattern = VIBRATION_PATTERN
                channel.enableLights(true)
                channel.setSound(
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                        .build()
                )

                val notificationManager = context.getSystemService(NotificationManager::class.java)
                notificationManager.createNotificationChannel(channel)
            }
        }


        fun createSimpleNotification(context: Context, notificationType: NotificationType) {

            val channelId = "${context.packageName}-${notificationType.name}"

            //activity to open when tapping the notification
            val notificationIntent = Intent(context, Activities.HEALTH.getActivity())
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val stackBuilder = TaskStackBuilder.create(context)
            stackBuilder.addNextIntentWithParentStack(notificationIntent)
            val pendingIntent = stackBuilder.getPendingIntent(notificationType.id, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT)

            //build the notification and show it
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder = NotificationCompat.Builder(context, channelId)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, notificationType.largeIcon))
                .setContentTitle(notificationType.title)
                .setContentText(notificationType.body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setOnlyAlertOnce(true)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setVibrate(VIBRATION_PATTERN)
                .setContentIntent(pendingIntent)
                .setSmallIcon(notificationType.smallIcon)
                .setColor(context.getColor(notificationType.color))
                .setStyle(NotificationCompat.BigTextStyle().bigText(notificationType.body))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            val notification = notificationBuilder.build()
            notification.flags = notification.flags or Notification.FLAG_AUTO_CANCEL

            val notificationManager = context.getSystemService(NotificationManager::class.java)

            createNotificationChannel(context, notificationType)
            notificationManager.notify(notificationType.id, notification)
        }

        @WorkerThread
        fun cancelAllNotifications(context: Context) {
            val notifications = NotificationType.getAll()
            for (notification in notifications) {
                cancelNotification(context, notification, hideSingleNotifications = true, hideAllNotifications = true)
            }
        }

        @WorkerThread
        fun cancelNotification(context: Context, notificationType: NotificationType, hideSingleNotifications: Boolean, hideAllNotifications: Boolean = false) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
            if (alarmManager != null) {

                val intent = Intent(context, NotificationsManager::class.java)
                val pendingIntent = PendingIntent.getBroadcast(context, notificationType.id, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT)
                // check if the alarm exists and cancel it
                val alarmExists = PendingIntent.getBroadcast(context, notificationType.id, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE) != null
                if (alarmExists) alarmManager.cancel(pendingIntent)
                Logger.d("Notification with id ${notificationType.id} cancelled")

                if (hideSingleNotifications) {
                    notificationManager?.cancel(notificationType.id)
                }

            } else {
                Logger.e("Alarm manager is null on cancel all notifications")
            }

            if (hideAllNotifications) {
                notificationManager?.cancelAll()
            }
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, 0)
        Logger.d("Received alarm, show notification with id $notificationId")

        val notificationType = NotificationType.getById(notificationId)
        createSimpleNotification(context, notificationType)
    }


}

