package br.com.tick.teira.ui.datasource.repositories

import br.com.tick.teira.ui.datasource.databases.entities.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {

    suspend fun addExpense(name: String, value: Double, category: String, expenseDate: Long)

    suspend fun removeExpense(expenseId: Int)

    suspend fun getAllExpenses(): Flow<List<Expense>>

    suspend fun getExpenses(numberOfExpenses: Int = -1): Flow<List<Expense>>
}
