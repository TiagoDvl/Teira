package br.com.tick.teira.ui.datasource.repositories

import br.com.tick.teira.ui.datasource.databases.ExpenseDao
import br.com.tick.teira.ui.datasource.databases.entities.Expense
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExpensesRepositoryImpl @Inject constructor(private val expenseDao: ExpenseDao) : ExpenseRepository {

    override suspend fun addExpense(name: String, value: String, category: String, expenseDate: Long) {
        expenseDao.addExpense(
            Expense(
                name = name,
                value = value,
                category = category,
                date = expenseDate
            )
        )
    }

    override suspend fun removeExpense(expenseId: Int) {
        expenseDao.removeExpenseById(expenseId)
    }

    override suspend fun getExpenses(numberOfExpenses: Int): Flow<List<Expense>> {
        return expenseDao.getExpenses(numberOfExpenses)
    }
}
