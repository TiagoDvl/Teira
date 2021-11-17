package br.com.tick.teira.ui.datasource.databases

import android.util.Log
import br.com.tick.teira.ui.datasource.databases.entities.Expense
import javax.inject.Inject

class ExpenseDaoImpl @Inject constructor(): ExpenseDao {

    override fun addExpense(expense: Expense) {
        Log.d("Tiago", "Whiskas")
    }

    override fun getExpenses(): List<Expense> {
        Log.d("Tiago", "Sachê")
        return listOf()
    }

}