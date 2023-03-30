package br.com.tick.sdk.notifications

interface NotificationRegister {

    fun register(name: String, descriptionText: String, channelId: String)
}