package br.com.tick.sdk.repositories

import br.com.tick.sdk.domain.ExpenseCategory
import br.com.tick.sdk.repositories.expensecategory.ExpenseCategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeExpenseCategoryRepository : ExpenseCategoryRepository {

    private val expenseCategories = mutableListOf<ExpenseCategory>()

    override suspend fun addCategory(categoryName: String, color: Int?) {
        expenseCategories.add(ExpenseCategory(expenseCategories.size, categoryName, color))
    }

    override fun getCategories(): Flow<List<ExpenseCategory>> {
        return flowOf(expenseCategories)
    }
}
