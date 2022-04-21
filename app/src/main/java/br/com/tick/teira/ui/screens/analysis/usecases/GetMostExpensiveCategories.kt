package br.com.tick.teira.ui.screens.analysis.usecases

import androidx.compose.ui.graphics.Color
import br.com.tick.teira.ui.datasource.repositories.CategoryRepository
import br.com.tick.teira.ui.datasource.repositories.ExpenseRepository
import br.com.tick.teira.ui.screens.analysis.models.MostExpensiveCategory
import br.com.tick.teira.ui.screens.analysis.states.MostExpensiveCategoriesStates
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
