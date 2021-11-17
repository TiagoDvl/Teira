package br.com.tick.teira.ui.datasource.repositories

import br.com.tick.teira.ui.datasource.databases.entities.Expense
import kotlinx.coroutines.flow.SharedFlow

interface ExpenseRepository {

    val expenses: SharedFlow<List<Expense>>

    suspend fun addExpense(name: String, value: String, category: String)

    suspend fun getExpenses(): List<Expense>
}