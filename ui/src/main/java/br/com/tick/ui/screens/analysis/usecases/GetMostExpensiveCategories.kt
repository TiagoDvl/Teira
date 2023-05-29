package br.com.tick.ui.screens.analysis.usecases

import br.com.tick.sdk.repositories.categorizedexpense.CategorizedExpenseRepository
import br.com.tick.ui.screens.analysis.models.MostExpensiveCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMostExpensiveCategories @Inject constructor(private val expenseRepository: CategorizedExpenseRepository) {

    suspend operator fun invoke(): Flow<List<MostExpensiveCategory>> =
        expenseRepository.getAccountingCycleExpenses().map { accountingCycleExpenses->
            val mostExpensiveCategories = mutableListOf<MostExpensiveCategory>()

            accountingCycleExpenses.forEach { categorizedExpense ->
                val mostExpensiveCategory = mostExpensiveCategories.find {
                    it.categoryName == categorizedExpense.category.name
                }

                if (mostExpensiveCategory == null) {
                    mostExpensiveCategories.add(
                        MostExpensiveCategory(
                            categorizedExpense.category.name,
                            categorizedExpense.category.color,
                            categorizedExpense.expenseValue
                        )
                    )
                } else {
                    mostExpensiveCategory.amount = mostExpensiveCategory.amount + categorizedExpense.expenseValue
                }
            }

            mostExpensiveCategories
        }
}
