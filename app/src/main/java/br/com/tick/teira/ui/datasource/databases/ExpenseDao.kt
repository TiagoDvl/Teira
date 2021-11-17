package br.com.tick.teira.ui.datasource.databases

import br.com.tick.teira.ui.datasource.databases.entities.Expense

interface ExpenseDao {

    fun addExpense(expense: Expense)
    fun getExpenses(): List<Expense>
}