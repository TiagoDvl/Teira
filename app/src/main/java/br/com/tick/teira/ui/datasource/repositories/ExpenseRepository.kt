package br.com.tick.teira.ui.datasource.repositories

import br.com.tick.teira.ui.datasource.databases.entities.Expense
import br.com.tick.teira.ui.datasource.domain.ExpenseCategory
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {

    suspend fun addExpense(categoryId: Int, name: String, value: Double, expenseDate: Long)

    suspend fun removeExpense(expenseId: Int)

    suspend fun getExpenses(numberOfExpenses: Int = -1): Flow<List<Expense>>

    suspend fun addCategory(categoryName: String): Boolean

    suspend fun getExpenseCategories(): Flow<List<ExpenseCategory>>

    suspend fun getExpenseCategoryById(categoryId: Int): ExpenseCategory
}
