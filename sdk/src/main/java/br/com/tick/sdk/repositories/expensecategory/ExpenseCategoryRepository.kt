package br.com.tick.sdk.repositories.expensecategory

import br.com.tick.sdk.domain.ExpenseCategory
import kotlinx.coroutines.flow.Flow

interface ExpenseCategoryRepository {

    suspend fun addExpenseCategory(categoryName: String, color: Int?)

    suspend fun editExpenseCategory(expenseCategoryId: Int, updatedCategoryName: String, updatedCategoryColor: Int)

    fun getCategories(): Flow<List<ExpenseCategory>>
}
