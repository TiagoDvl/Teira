package br.com.tick.sdk.repositories

import br.com.tick.sdk.database.entities.User
import br.com.tick.sdk.domain.AccountingDate
import br.com.tick.sdk.domain.CurrencyFormat
import br.com.tick.sdk.domain.NotificationPeriodicity
import br.com.tick.sdk.repositories.user.UserRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first

class FakeUserRepository: UserRepository {

    private val user: MutableSharedFlow<User> = MutableSharedFlow(replay = 1)

    init {
        user.tryEmit(User.initial())
    }

    override fun getUser() = user
    override suspend fun setInitialUser() {
        user.tryEmit(User.initial())
    }

    override suspend fun setMonthlyIncome(newValue: Double) {
        user.emit(getUser().first().copy(monthlyIncome = newValue))
    }

    override suspend fun setNotificationPeriodicity(notificationPeriodicity: NotificationPeriodicity) {
        user.tryEmit(getUser().first().copy(notificationPeriodicity = notificationPeriodicity))
    }

    override suspend fun setCurrency(currencyFormat: CurrencyFormat) {
        user.tryEmit(getUser().first().copy(currency = currencyFormat))
    }

    override suspend fun setAccountingDate(accountingDate: AccountingDate) {
        user.tryEmit(getUser().first().copy(accountingDate = accountingDate))
    }

    override suspend fun toggleMonthlyIncomeVisibility() {
        val currentUser = getUser().first()
        user.tryEmit(getUser().first().copy(monthlyIncomeVisibility = !currentUser.monthlyIncomeVisibility))
    }
}
