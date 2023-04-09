package br.com.tick.sdk.extensions

import br.com.tick.sdk.domain.NotificationPeriodicity
import java.time.Duration
import java.time.LocalDateTime

fun LocalDateTime.getPeriodicityTimeDiff(notificationPeriodicity: NotificationPeriodicity): Long {
    val periodicity = when (notificationPeriodicity) {
        NotificationPeriodicity.DAILY -> 1
        NotificationPeriodicity.WEEKLY -> 7
        NotificationPeriodicity.CANCELED -> 0
    }.toLong()

    val currentDate = LocalDateTime.now()
    val dueDate = currentDate.plusDays(periodicity).withHour(22).withMinute(0)

    return Duration.between(currentDate, dueDate).toMinutes()
}
