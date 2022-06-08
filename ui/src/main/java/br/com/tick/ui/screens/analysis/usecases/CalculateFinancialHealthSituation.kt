package br.com.tick.ui.screens.analysis.usecases

import br.com.tick.sdk.repositories.CategorizedExpenseRepository
import br.com.tick.sdk.repositories.LocalDataRepository
import br.com.tick.ui.screens.analysis.states.FinancialHealth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class CalculateFinancialHealthSituation @Inject constructor(
    private val expenseRepository: CategorizedExpenseRepository,
    private val dataStoreRepository: LocalDataRepository
) {

    companion object {
        private const val A_MONTH = 30
    }

    suspend operator fun invoke(): Flow<FinancialHealth> {
        val expenses = expenseRepository.getCategorizedExpenses(A_MONTH)
        val monthlyIncome = dataStoreRepository.getMonthlyIncome()

        return expenses.combine(monthlyIncome) { _expensesList, _monthlyIncome ->
            FinancialHealth.of(_expensesList, _monthlyIncome.monthlyIncomeValue)
        }
    }
}
