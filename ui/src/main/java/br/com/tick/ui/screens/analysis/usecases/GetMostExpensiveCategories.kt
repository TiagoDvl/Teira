package br.com.tick.ui.screens.analysis.usecases

import androidx.compose.ui.graphics.Color
import br.com.tick.sdk.repositories.categorizedexpense.CategorizedExpenseRepository
import br.com.tick.ui.screens.analysis.models.MostExpensiveCategory
import br.com.tick.ui.screens.analysis.states.MostExpensiveCategoriesStates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMostExpensiveCategories @Inject constructor(private val expenseRepository: CategorizedExpenseRepository) {

    suspend operator fun invoke(): Flow<MostExpensiveCategoriesStates> =
        expenseRepository.getThirtyDaysCategorizedExpenses().map { categorizedExpenses ->
            val mostExpensiveCategories = mutableListOf<MostExpensiveCategory>()

            categorizedExpenses.forEach { categorizedExpense ->
                val mostExpensiveCategory = mostExpensiveCategories.find { it.categoryName == categorizedExpense.name }

                if (mostExpensiveCategory == null) {
                    mostExpensiveCategories.add(
                        MostExpensiveCategory(categorizedExpense.name, Color.Red, categorizedExpense.expenseValue)
                    )
                } else {
                    mostExpensiveCategory.amount = mostExpensiveCategory.amount + categorizedExpense.expenseValue
                }
            }

            MostExpensiveCategoriesStates.of(mostExpensiveCategories.sortedByDescending { it.amount })
        }
}
