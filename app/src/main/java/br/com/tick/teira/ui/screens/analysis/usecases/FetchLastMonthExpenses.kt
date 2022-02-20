package br.com.tick.teira.ui.screens.analysis.usecases

import br.com.tick.teira.ui.datasource.repositories.ExpenseRepository
import br.com.tick.teira.ui.screens.analysis.states.AnalysisGraphStates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FetchLastMonthExpenses @Inject constructor(
    private val expenseRepository: ExpenseRepository
) {

    companion object {
        private const val A_MONTH = 30
    }

    suspend operator fun invoke(): Flow<AnalysisGraphStates> {
        return expenseRepository.getExpenses(A_MONTH).map { AnalysisGraphStates.of(it) }
    }
}