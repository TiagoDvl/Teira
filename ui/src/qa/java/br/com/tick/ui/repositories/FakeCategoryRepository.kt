package br.com.tick.ui.repositories

import br.com.tick.sdk.domain.ExpenseCategory
import br.com.tick.sdk.repositories.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeCategoryRepository : CategoryRepository {

    private val expenseCategories = mutableListOf<ExpenseCategory>()

    override suspend fun addCategory(categoryName: String) {
        expenseCategories.add(ExpenseCategory(expenseCategories.size, categoryName))
    }

    override suspend fun getCategories(): Flow<List<ExpenseCategory>> {
        return flowOf(expenseCategories)
    }

    override suspend fun getCategoryById(categoryId: Int): ExpenseCategory = expenseCategories[categoryId]
}