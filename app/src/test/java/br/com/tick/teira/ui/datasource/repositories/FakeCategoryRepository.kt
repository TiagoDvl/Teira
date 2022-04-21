package br.com.tick.teira.ui.datasource.repositories

import br.com.tick.teira.ui.datasource.domain.ExpenseCategory
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