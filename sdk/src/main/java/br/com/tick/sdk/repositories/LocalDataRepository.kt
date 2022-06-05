package br.com.tick.sdk.repositories

import kotlinx.coroutines.flow.Flow

interface LocalDataRepository {

    fun getMonthlyIncome(): Flow<Double>

    suspend fun saveMonthlyIncome(value: Double)
}
