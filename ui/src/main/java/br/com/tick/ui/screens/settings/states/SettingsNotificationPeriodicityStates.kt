package br.com.tick.ui.screens.settings.states

import android.content.Context
import br.com.tick.sdk.domain.NotificationPeriodicity
import br.com.tick.ui.R

sealed class SettingsNotificationPeriodicityStates {

    companion object {

        fun of(notificationPeriodicity: NotificationPeriodicity?): SettingsNotificationPeriodicityStates {
            return when (notificationPeriodicity) {
                null -> Loading
                else -> Content(notificationPeriodicity)
            }
        }
    }

    object Loading : SettingsNotificationPeriodicityStates()

    class Content(val label: NotificationPeriodicity) : SettingsNotificationPeriodicityStates()
}

fun SettingsNotificationPeriodicityStates.getNotificationPeriodicityLabel(context: Context): String {
    val resourceId = when (this) {
        is SettingsNotificationPeriodicityStates.Loading -> R.string.generic_loading
        is SettingsNotificationPeriodicityStates.Content -> {
            when (this.label) {
                NotificationPeriodicity.DAILY -> R.string.settings_notifications_periodicity_daily
                NotificationPeriodicity.WEEKLY -> R.string.settings_notifications_periodicity_weekly
                NotificationPeriodicity.CANCELED -> R.string.settings_notifications_periodicity_canceled
            }
        }

    }
    return context.getString(resourceId)
}