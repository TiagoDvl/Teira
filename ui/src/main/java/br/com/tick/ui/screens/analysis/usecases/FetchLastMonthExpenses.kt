package br.com.tick.ui.screens.analysis.usecases

import br.com.tick.sdk.domain.CategorizedExpense
import br.com.tick.sdk.repositories.categorizedexpense.CategorizedExpenseRepository
import br.com.tick.ui.screens.analysis.states.AnalysisGraphStates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class FetchLastMonthExpenses @Inject constructor(private val expenseRepository: CategorizedExpenseRepository) {

    suspend operator fun invoke(): Flow<AnalysisGraphStates> {
        return expenseRepository.getAccountingCycleExpenses().map {
            AnalysisGraphStates.of(mergeAndSumExpenses(it))
        }
    }

    private fun mergeAndSumExpenses(categorizedExpensesList: List<CategorizedExpense>): Map<LocalDate, Double> {
        val lastMonthExpenses = mutableMapOf<LocalDate, Double>()

        categorizedExpensesList.forEach { categorizedExpense ->
            val dateAsKey = categorizedExpense.date
            lastMonthExpenses[dateAsKey] = if (lastMonthExpenses.containsKey(dateAsKey)) {
                lastMonthExpenses[dateAsKey]!!.plus(categorizedExpense.expenseValue)
            } else {
                 categorizedExpense.expenseValue
            }
        }

        return lastMonthExpenses
    }
}
