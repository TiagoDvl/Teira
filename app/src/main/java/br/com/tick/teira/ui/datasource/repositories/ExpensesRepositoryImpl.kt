package br.com.tick.teira.ui.datasource.repositories

import br.com.tick.teira.ui.datasource.databases.ExpenseDao
import br.com.tick.teira.ui.datasource.databases.entities.Expense
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

class ExpensesRepositoryImpl @Inject constructor(private val expenseDao: ExpenseDao): ExpenseRepository {

    override val expenses = MutableSharedFlow<List<Expense>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override suspend fun addExpense(name: String, value: String, category: String) {
        expenseDao.addExpense(
            Expense(
                name = name,
                value = value,
                category = category
            )
        )

        expenses.tryEmit(expenseDao.getExpenses())
    }

    override suspend fun getExpenses(): List<Expense> = expenseDao.getExpenses()
}
