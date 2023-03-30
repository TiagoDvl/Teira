package br.com.tick.sdk.notifications

interface NotificationCenter {

    suspend fun sendNotification(title: String, text: String, icon: Int, channelId: String)
}