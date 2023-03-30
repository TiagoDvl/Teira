package br.com.tick.sdk.di

import br.com.tick.sdk.notifications.NotificationCenter
import br.com.tick.sdk.notifications.NotificationRegister
import br.com.tick.sdk.notifications.TeiraNotificationCenter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationModule {

    @Binds
    @Singleton
    abstract fun bindsNotificationCenter(notificationCenter: TeiraNotificationCenter): NotificationCenter

    @Binds
    @Singleton
    abstract fun bindsNotificationRegister(notificationCenter: TeiraNotificationCenter): NotificationRegister
}