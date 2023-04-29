package br.com.tick.sdk.repositories.user

import br.com.tick.sdk.database.UserDao
import br.com.tick.sdk.database.entities.User
import br.com.tick.sdk.domain.CurrencyFormat
import br.com.tick.sdk.domain.NotificationPeriodicity
import br.com.tick.sdk.domain.AccountingDate
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    override fun getUser(): Flow<User> = userDao.getUniqueUser()

    override suspend fun setMonthlyIncome(newValue: Double) {
        userDao.setMonthlyIncome(newValue)
    }

    override suspend fun setNotificationPeriodicity(notificationPeriodicity: NotificationPeriodicity) {
        userDao.setNotificationPeriodicity(notificationPeriodicity)
    }

    override suspend fun setCurrency(currencyFormat: CurrencyFormat) {
        userDao.setCurrency(currencyFormat)
    }

    override suspend fun setAccountingDate(accountingDate: AccountingDate) {
        userDao.setAccountingDate(accountingDate)
    }
}