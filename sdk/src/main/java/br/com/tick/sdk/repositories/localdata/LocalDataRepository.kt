package br.com.tick.sdk.repositories.localdata

import br.com.tick.sdk.domain.NotificationPeriodicity
import br.com.tick.sdk.domain.PersistedMonthlyIncome
import kotlinx.coroutines.flow.Flow

interface LocalDataRepository {

    fun getMonthlyIncome(): Flow<PersistedMonthlyIncome>

    suspend fun saveMonthlyIncome(value: Double)

    fun getPeriodicNotificationId(): Flow<Int?>

    suspend fun setPeriodicNotificationId(notificationId: Int)

    fun getNotificationPeriodicity(): Flow<NotificationPeriodicity?>

    suspend fun setNotificationPeriodicity(notificationPeriodicity: NotificationPeriodicity)
}
