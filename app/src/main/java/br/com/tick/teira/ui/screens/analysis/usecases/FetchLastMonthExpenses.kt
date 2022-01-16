package br.com.tick.teira.ui.screens.analysis.usecases

import br.com.tick.teira.ui.datasource.databases.entities.Expense
import br.com.tick.teira.ui.datasource.repositories.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchLastMonthExpenses @Inject constructor(
    private val expenseRepository: ExpenseRepository
) {

    companion object {
        private const val A_MONTH = 30
    }

    suspend operator fun invoke(): Flow<List<Expense>> {
        return expenseRepository.getExpenses(A_MONTH)
    }
}