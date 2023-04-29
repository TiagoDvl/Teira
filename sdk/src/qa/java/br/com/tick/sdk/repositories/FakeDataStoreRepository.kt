package br.com.tick.sdk.repositories

import br.com.tick.sdk.repositories.localdata.LocalDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf

class FakeDataStoreRepository : LocalDataRepository {

    private val periodicNotificationIdDataStore: MutableSharedFlow<Int?> = MutableSharedFlow(replay = 1)

    override fun getPeriodicNotificationId() = periodicNotificationIdDataStore

    override suspend fun setPeriodicNotificationId(notificationId: Int) {
        periodicNotificationIdDataStore.emit(notificationId)
    }
}
