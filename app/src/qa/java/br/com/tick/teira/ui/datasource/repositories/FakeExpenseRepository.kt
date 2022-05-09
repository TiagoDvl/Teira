package br.com.tick.teira.ui.datasource.repositories

import br.com.tick.teira.ui.datasource.databases.entities.Expense
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeExpenseRepository : ExpenseRepository {

    private val expenses = mutableListOf<Expense>()

    override suspend fun addExpense(categoryId: Int, name: String, value: Double, expenseDate: Long) {
        expenses.add(Expense(expenses.size, categoryId, name, value, expenseDate))
    }

    override suspend fun removeExpense(expenseId: Int) {
        expenses.removeAt(expenseId)
    }

    override suspend fun getExpenses(numberOfExpenses: Int): Flow<List<Expense>> {
        return flowOf(expenses.take(numberOfExpenses))
    }
}
