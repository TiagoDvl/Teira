package br.com.tick.teira.ui.datasource.repositories

import br.com.tick.teira.ui.datasource.databases.entities.Expense

interface ExpenseRepository {

    fun addExpense(name: String, value: String, category: String)

    fun getExpenses(): List<Expense>
}