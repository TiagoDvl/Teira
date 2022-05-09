package br.com.tick.teira.ui.datasource.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map

class FakeLocalDataRepository : LocalDataRepository {

    private val dataStore: MutableSharedFlow<Double> = MutableSharedFlow(replay = 1)

    init {
        dataStore.tryEmit(0.0)
    }

    override fun getMonthlyIncome(): Flow<Double> {
        return dataStore.map { it }
    }

    override suspend fun saveMonthlyIncome(value: Double) {
        dataStore.emit(value)
    }
}