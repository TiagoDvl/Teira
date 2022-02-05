package br.com.tick.teira.ui.screens.analysis.usecases

import androidx.compose.ui.graphics.Color
import br.com.tick.teira.ui.datasource.repositories.ExpenseRepository
import br.com.tick.teira.ui.screens.analysis.models.MostExpensiveCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMostExpensiveCategories @Inject constructor(private val expenseRepository: ExpenseRepository) {

    companion object {
        private const val A_MONTH = 30
    }

    suspend operator fun invoke(): Flow<List<MostExpensiveCategory>> {
        return flow {
            expenseRepository.getExpenses(A_MONTH).collect { expenses ->
                val mostExpensiveCategories = mutableListOf<MostExpensiveCategory>()

                expenses.forEach { expense ->
                    val expenseCategory = expenseRepository.getExpenseCategoryById(expense.categoryId)
                    val mostExpensiveCategory = mostExpensiveCategories.find { it.categoryName == expenseCategory.name }

                    if (mostExpensiveCategory == null) {
                        mostExpensiveCategories.add(
                            MostExpensiveCategory(expenseCategory.name, Color.Red, expense.value)
                        )
                    } else {
                        mostExpensiveCategory.amount = mostExpensiveCategory.amount + expense.value
                    }

                    emit(mostExpensiveCategories)
                }
            }
        }
    }
}
