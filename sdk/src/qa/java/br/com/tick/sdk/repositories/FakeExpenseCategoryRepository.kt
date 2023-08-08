package br.com.tick.sdk.repositories

import br.com.tick.sdk.domain.ExpenseCategory
import br.com.tick.sdk.repositories.expensecategory.ExpenseCategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeExpenseCategoryRepository : ExpenseCategoryRepository {

    private val expenseCategories = mutableListOf<ExpenseCategory>()
    override suspend fun addExpenseCategory(categoryName: String, color: Int?) {
        expenseCategories.add(ExpenseCategory(expenseCategories.size, categoryName, color))
    }

    override suspend fun editExpenseCategory(
        expenseCategoryId: Int,
        updatedCategoryName: String,
        updatedCategoryColor: Int
    ) {
        expenseCategories[expenseCategoryId] = expenseCategories[expenseCategoryId].copy(
            name = updatedCategoryName,
            color = updatedCategoryColor
        )
    }

    override fun getCategories(): Flow<List<ExpenseCategory>> {
        return flowOf(expenseCategories)
    }
}
