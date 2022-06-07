package br.com.tick.ui.repositories

import br.com.tick.sdk.database.ExpenseDao
import br.com.tick.sdk.database.entities.Expense
import br.com.tick.sdk.repositories.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ExpensesRepositoryImpl @Inject constructor(private val expenseDao: ExpenseDao) : ExpenseRepository {

    override suspend fun addExpense(categoryId: Int, name: String, value: Double, expenseDate: Long) {
        expenseDao.addExpense(
            Expense(
                categoryId = categoryId,
                name = name,
                value = value,
                date = expenseDate
            )
        )
    }

    override suspend fun removeExpense(expenseId: Int) {
        expenseDao.removeExpenseById(expenseId)
    }

    override suspend fun getExpenses(numberOfExpenses: Int): Flow<List<Expense>> {
        return if (numberOfExpenses < 0) flowOf(listOf()) else expenseDao.getExpenses(numberOfExpenses)
    }
}
