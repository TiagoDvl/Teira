package br.com.tick.teira.ui.datasource.repositories

import br.com.tick.teira.ui.datasource.databases.ExpenseDao
import br.com.tick.teira.ui.datasource.databases.entities.Expense
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExpensesRepositoryImpl @Inject constructor(private val expenseDao: ExpenseDao) : ExpenseRepository {

    override suspend fun addExpense(name: String, value: Double, category: String, expenseDate: Long) {
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

    override suspend fun getAllExpenses(): Flow<List<Expense>> {
        return expenseDao.getAllExpenses()
    }

    override suspend fun getExpenses(numberOfExpenses: Int): Flow<List<Expense>> {
        with(expenseDao) {
            return if (numberOfExpenses < 0) {
                getAllExpenses()
            } else {
                getExpenses(numberOfExpenses)
            }
        }
    }
}
