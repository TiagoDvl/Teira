package br.com.tick.sdk.repositories.expensecategory

import br.com.tick.sdk.database.entities.CategoryColor
import br.com.tick.sdk.domain.ExpenseCategory
import kotlinx.coroutines.flow.Flow

interface ExpenseCategoryRepository {

    suspend fun addCategory(categoryName: String, color: CategoryColor)

    fun getCategories(): Flow<List<ExpenseCategory>>
}
