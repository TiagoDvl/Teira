package br.com.tick.teira.ui.datasource.repositories

import android.util.Log
import br.com.tick.teira.ui.datasource.databases.ExpenseDao
import br.com.tick.teira.ui.datasource.databases.entities.Expense
import javax.inject.Inject

class ExpensesRepositoryImpl @Inject constructor(private val expenseDao: ExpenseDao): ExpenseRepository {

    private val expenses: MutableList<String> = mutableListOf()

    override fun addExpense(name: String, value: String, category: String) {
        expenseDao.addExpense(Expense(name, value, category))
        expenses.add(name)
        Log.d("Tiago", expenses.toString())
    }

    override fun getExpenses(): List<Expense> = expenseDao.getExpenses()
}
