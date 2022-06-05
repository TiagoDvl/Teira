package br.com.tick.sdk.repositories

import br.com.tick.sdk.domain.ExpenseCategory
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    suspend fun addCategory(categoryName: String)

    suspend fun getCategories(): Flow<List<ExpenseCategory>>

    suspend fun getCategoryById(categoryId: Int): ExpenseCategory
}
