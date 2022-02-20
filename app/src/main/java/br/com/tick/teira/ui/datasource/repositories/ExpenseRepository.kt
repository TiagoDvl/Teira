package br.com.tick.teira.ui.datasource.repositories

import br.com.tick.teira.ui.datasource.databases.entities.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {

    suspend fun addExpense(categoryId: Int, name: String, value: Double, expenseDate: Long)

    suspend fun removeExpense(expenseId: Int)

    suspend fun getExpenses(numberOfExpenses: Int = -1): Flow<List<Expense>>
}
