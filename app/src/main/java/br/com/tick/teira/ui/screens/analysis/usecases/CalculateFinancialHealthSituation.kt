package br.com.tick.teira.ui.screens.analysis.usecases

import br.com.tick.teira.ui.datasource.databases.entities.Expense
import br.com.tick.teira.ui.datasource.repositories.ExpenseRepository
import br.com.tick.teira.ui.datasource.repositories.LocalDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class CalculateFinancialHealthSituation @Inject constructor(
    private val repository: ExpenseRepository,
    private val dataStoreRepository: LocalDataRepository
) {

    companion object {
        private const val A_MONTH = 30
    }

    suspend operator fun invoke(): Flow<Pair<List<Expense>, Double>> {
        val expenses = repository.getExpenses(A_MONTH)
        val monthlyIncome = dataStoreRepository.getMonthlyIncome()

        return expenses.combine(monthlyIncome) { _expensesList, _monthlyIncome ->
            Pair(_expensesList, _monthlyIncome)
        }
    }
}
