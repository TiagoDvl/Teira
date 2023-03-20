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
        return expenseRepository.getThirtyDaysCategorizedExpenses().map {
            AnalysisGraphStates.of(mergeAndSumExpenses(it))
        }
    }

    private fun mergeAndSumExpenses(categorizedExpensesList: List<CategorizedExpense>): Map<LocalDate, Double> {
        val lastMonthExpenses = mutableMapOf<LocalDate, Double>()

        categorizedExpensesList.forEach { categorizedExpense ->
            if (lastMonthExpenses.containsKey(categorizedExpense.date)) {
                lastMonthExpenses[categorizedExpense.date] =
                    lastMonthExpenses[categorizedExpense.date]!!.plus(categorizedExpense.expenseValue)
            } else {
                lastMonthExpenses[categorizedExpense.date] = categorizedExpense.expenseValue
            }
        }

        return lastMonthExpenses
    }
}