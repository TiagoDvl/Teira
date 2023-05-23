package br.com.tick.ui.screens.wallet.usecases

import br.com.tick.sdk.repositories.categorizedexpense.CategorizedExpenseRepository
import br.com.tick.sdk.repositories.user.UserRepository
import br.com.tick.ui.screens.wallet.models.AvailableBalance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetAvailableBalance @Inject constructor(
    private val categorizedExpenseRepository: CategorizedExpenseRepository,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(): Flow<AvailableBalance> {
        val user = userRepository.getUser()
        val currentCycleExpenses = categorizedExpenseRepository.getAccountingCycleExpenses()

        return user.combine(currentCycleExpenses) { _user, _cycleExpenses ->
            val monthlyIncome = _user.monthlyIncome
            val expensesSum = _cycleExpenses.sumOf { it.expenseValue }

            val diff = monthlyIncome - expensesSum
            AvailableBalance(_user.currency, diff, _user.monthlyIncomeVisibility)
        }
    }
}
