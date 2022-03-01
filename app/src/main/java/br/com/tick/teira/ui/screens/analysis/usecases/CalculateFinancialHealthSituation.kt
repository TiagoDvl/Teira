package br.com.tick.teira.ui.screens.analysis.usecases

import br.com.tick.teira.ui.datasource.repositories.ExpenseRepository
import br.com.tick.teira.ui.datasource.repositories.LocalDataRepository
import br.com.tick.teira.ui.screens.analysis.states.FinancialHealth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class CalculateFinancialHealthSituation @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val dataStoreRepository: LocalDataRepository
) {

    companion object {
        private const val A_MONTH = 30
    }

    suspend operator fun invoke(): Flow<FinancialHealth> {
        val expenses = expenseRepository.getExpenses(A_MONTH)
        val monthlyIncome = dataStoreRepository.getMonthlyIncome()

        return expenses.combine(monthlyIncome) { _expensesList, _monthlyIncome ->
            FinancialHealth.of(_expensesList, _monthlyIncome)
        }
    }
}
