package br.com.tick.ui.screens.analysis.usecases

import br.com.tick.sdk.repositories.categorizedexpense.CategorizedExpenseRepository
import br.com.tick.ui.screens.analysis.states.AnalysisGraphStates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FetchLastMonthExpenses @Inject constructor(private val expenseRepository: CategorizedExpenseRepository) {

    companion object {
        private const val A_MONTH = 30
    }

    suspend operator fun invoke(): Flow<AnalysisGraphStates> {
        return expenseRepository.getCategorizedExpenses(A_MONTH).map { AnalysisGraphStates.of(it) }
    }
}