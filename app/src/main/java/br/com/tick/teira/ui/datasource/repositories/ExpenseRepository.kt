package br.com.tick.teira.ui.datasource.repositories

import br.com.tick.teira.ui.datasource.databases.entities.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {

    suspend fun addExpense(name: String, value: String, category: String, expenseDate: Long)

    suspend fun getExpenses(numberOfExpenses: Int): Flow<List<Expense>>
}
