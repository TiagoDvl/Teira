package br.com.tick.sdk.extensions

import br.com.tick.sdk.domain.NotificationPeriodicity
import java.time.LocalDateTime
import java.time.ZoneOffset

fun LocalDateTime.getPeriodicityTimeDiff(notificationPeriodicity: NotificationPeriodicity): Long {
    val periodicity = when (notificationPeriodicity) {
        NotificationPeriodicity.DAILY -> 1
        NotificationPeriodicity.WEEKLY -> 7
        NotificationPeriodicity.CANCELED -> 0
    }

    val currentDate = LocalDateTime.now()
    val dueDate = currentDate.plusDays(periodicity.toLong()).withHour(22)

    return dueDate.toEpochSecond(ZoneOffset.MIN) - currentDate.toEpochSecond(ZoneOffset.MIN)
}
