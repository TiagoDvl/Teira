package br.com.tick.sdk.repositories

import br.com.tick.sdk.domain.CurrencyFormat
import br.com.tick.sdk.domain.NotificationPeriodicity
import br.com.tick.sdk.domain.PersistedMonthlyIncome
import br.com.tick.sdk.repositories.localdata.LocalDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeDataStoreRepository : LocalDataRepository {

    private val monthlyIncomeDataStore: MutableSharedFlow<PersistedMonthlyIncome> = MutableSharedFlow(replay = 1)
    private val periodicNotificationIdDataStore: MutableSharedFlow<Int?> = MutableSharedFlow(replay = 1)
    private val notificationPeriodicityDataStore: MutableSharedFlow<NotificationPeriodicity?> = MutableSharedFlow(replay = 1)
    private val currencyFormatDataStore: MutableSharedFlow<CurrencyFormat?> = MutableSharedFlow(replay = 1)

    init {
        monthlyIncomeDataStore.tryEmit(PersistedMonthlyIncome(0.0))
        periodicNotificationIdDataStore.tryEmit(1)
        notificationPeriodicityDataStore.tryEmit(NotificationPeriodicity.DAILY)
        currencyFormatDataStore.tryEmit(CurrencyFormat.REAL)
    }

    override fun getMonthlyIncome(): Flow<PersistedMonthlyIncome> {
        return monthlyIncomeDataStore
    }
    override suspend fun saveMonthlyIncome(value: Double) {
        monthlyIncomeDataStore.emit(PersistedMonthlyIncome(value))
    }

    override fun getPeriodicNotificationId(): Flow<Int?> {
        return periodicNotificationIdDataStore
    }

    override suspend fun setPeriodicNotificationId(notificationId: Int) {
        periodicNotificationIdDataStore.emit(notificationId)
    }

    override fun getNotificationPeriodicity(): Flow<NotificationPeriodicity?> {
        return notificationPeriodicityDataStore
    }

    override suspend fun setNotificationPeriodicity(notificationPeriodicity: NotificationPeriodicity) {
        notificationPeriodicityDataStore.emit(notificationPeriodicity)
    }

    override fun getCurrencyFormat(): Flow<CurrencyFormat?> {
        return currencyFormatDataStore
    }

    override suspend fun setCurrencyFormat(currencyFormat: CurrencyFormat) {
        currencyFormatDataStore.emit(currencyFormat)
    }
}