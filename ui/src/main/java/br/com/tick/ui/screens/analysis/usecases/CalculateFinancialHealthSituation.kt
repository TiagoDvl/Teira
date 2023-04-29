package br.com.tick.ui.screens.analysis.usecases

import br.com.tick.sdk.repositories.categorizedexpense.CategorizedExpenseRepository
import br.com.tick.sdk.repositories.user.UserRepository
import br.com.tick.ui.screens.analysis.states.FinancialHealth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class CalculateFinancialHealthSituation @Inject constructor(
    private val expenseRepository: CategorizedExpenseRepository,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(): Flow<FinancialHealth> {
        val expenses = expenseRepository.getAccountingCycleExpenses()

        return expenses.combine(userRepository.getUser()) { _expensesList, _user ->
            FinancialHealth.of(_expensesList, _user.monthlyIncome)
        }
    }
}
