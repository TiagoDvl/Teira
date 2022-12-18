package br.com.tick.sdk.repositories

import br.com.tick.sdk.domain.PersistedMonthlyIncome
import br.com.tick.sdk.repositories.localdata.LocalDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map

class FakeDataStoreRepository : LocalDataRepository {

    private val dataStore: MutableSharedFlow<PersistedMonthlyIncome> = MutableSharedFlow(replay = 1)

    init {
        dataStore.tryEmit(PersistedMonthlyIncome(0.0))
    }

    override fun getMonthlyIncome(): Flow<PersistedMonthlyIncome> {
        return dataStore.map { it }
    }

    override suspend fun saveMonthlyIncome(value: Double) {
        dataStore.emit(PersistedMonthlyIncome(value))
    }
}