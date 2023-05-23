package br.com.tick.sdk.repositories.user

import br.com.tick.sdk.database.entities.User
import br.com.tick.sdk.domain.CurrencyFormat
import br.com.tick.sdk.domain.NotificationPeriodicity
import br.com.tick.sdk.domain.AccountingDate
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getUser(): Flow<User>

    suspend fun setInitialUser()

    suspend fun setMonthlyIncome(newValue: Double)

    suspend fun setNotificationPeriodicity(notificationPeriodicity: NotificationPeriodicity)

    suspend fun setCurrency(currencyFormat: CurrencyFormat)

    suspend fun setAccountingDate(accountingDate: AccountingDate)

    suspend fun toggleMonthlyIncomeVisibility()
}