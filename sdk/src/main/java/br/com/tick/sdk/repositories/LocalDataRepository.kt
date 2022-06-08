package br.com.tick.sdk.repositories

import br.com.tick.sdk.domain.PersistedMonthlyIncome
import kotlinx.coroutines.flow.Flow

interface LocalDataRepository {

    fun getMonthlyIncome(): Flow<PersistedMonthlyIncome>

    suspend fun saveMonthlyIncome(value: Double)
}
