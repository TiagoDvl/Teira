package br.com.tick.sdk.repositories.localdata

import kotlinx.coroutines.flow.Flow

interface LocalDataRepository {

    fun getPeriodicNotificationId(): Flow<Int?>

    suspend fun setPeriodicNotificationId(notificationId: Int)
}
