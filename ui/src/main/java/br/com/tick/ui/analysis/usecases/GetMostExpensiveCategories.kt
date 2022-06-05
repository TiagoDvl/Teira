package br.com.tick.ui.analysis.usecases

import androidx.compose.ui.graphics.Color
import br.com.tick.sdk.repositories.CategoryRepository
import br.com.tick.sdk.repositories.ExpenseRepository
import br.com.tick.ui.analysis.models.MostExpensiveCategory
import br.com.tick.ui.analysis.states.MostExpensiveCategoriesStates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMostExpensiveCategories @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val categoryRepository: CategoryRepository,
) {

    companion object {
        private const val A_MONTH = 30
    }

    suspend operator fun invoke(): Flow<MostExpensiveCategoriesStates> = expenseRepository.getExpenses(A_MONTH).map {
        val mostExpensiveCategories = mutableListOf<MostExpensiveCategory>()

        it.forEach { expense ->
            val expenseCategory = categoryRepository.getCategoryById(expense.categoryId)
            val mostExpensiveCategory = mostExpensiveCategories.find { it.categoryName == expenseCategory.name }

            if (mostExpensiveCategory == null) {
                mostExpensiveCategories.add(
                    MostExpensiveCategory(expenseCategory.name, Color.Red, expense.value)
                )
            } else {
                mostExpensiveCategory.amount = mostExpensiveCategory.amount + expense.value
            }
        }

        MostExpensiveCategoriesStates.of(mostExpensiveCategories.sortedByDescending { it.amount })
    }
}
