package br.com.tick.sdk.notifications

import android.app.PendingIntent

interface NotificationCenter {

    suspend fun sendNotification(
        title: String,
        text: String,
        icon: Int,
        channelId: String,
        pendingIntent: PendingIntent
    )
}
