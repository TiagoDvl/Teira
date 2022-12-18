package br.com.tick.sdk.repositories.categorizedexpense

import br.com.tick.sdk.domain.CategorizedExpense
import kotlinx.coroutines.flow.Flow

interface CategorizedExpenseRepository {

    suspend fun addExpense(categoryId: Int, name: String, value: Double, expenseDate: Long)

    suspend fun removeExpense(expenseId: Int)

    suspend fun getCategorizedExpenses(numberOfExpenses: Int = -1): Flow<List<CategorizedExpense>>
}
