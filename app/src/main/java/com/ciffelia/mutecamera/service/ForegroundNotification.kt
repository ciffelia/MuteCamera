package com.ciffelia.mutecamera.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import com.ciffelia.mutecamera.R


class ForegroundNotification(private val service: Service) {

    companion object {
        private const val channelId = "mutecamera_foreground"
        private const val channelName = "MuteCamera Foreground"
        private const val channelDescription = "Notify when MuteCamera is running"
        private const val channelImportance = NotificationManager.IMPORTANCE_NONE

        private const val notificationId = 1
        private const val notificationTitle = "MuteCamera"
        private const val notificationText = "Running"
    }

    private val notificationManager = service.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val foregroundNotification = run {
        Notification.Builder(service, channelId).apply {
            setContentTitle(notificationTitle)
            setContentText(notificationText)
            setCategory(Notification.CATEGORY_SERVICE)
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setOngoing(true)
        }.build()
    }

    init {
        createChannelIfNeeded()
    }


    fun startForeground() = service.startForeground(notificationId, foregroundNotification)

    private fun createChannelIfNeeded() {
        if (notificationChannelExists()) return

        val channel = NotificationChannel(channelId, channelName, channelImportance)
        channel.apply {
            description = channelDescription
            enableLights(false)
            enableVibration(false)
            setShowBadge(false)
        }
        notificationManager.createNotificationChannel(channel)
    }

    private fun notificationChannelExists() = notificationManager.getNotificationChannel(channelId) != null
}
