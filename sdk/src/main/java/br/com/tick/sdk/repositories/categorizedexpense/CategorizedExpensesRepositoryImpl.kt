package br.com.tick.sdk.repositories.categorizedexpense

import br.com.tick.sdk.database.CategoryDao
import br.com.tick.sdk.database.ExpenseDao
import br.com.tick.sdk.database.entities.Expense
import br.com.tick.sdk.domain.CategorizedExpense
import br.com.tick.sdk.domain.ExpenseCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategorizedExpensesRepositoryImpl @Inject constructor(
    private val expenseDao: ExpenseDao,
    private val categoryDao: CategoryDao
) : CategorizedExpenseRepository {

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

    override suspend fun getCategorizedExpenses(numberOfExpenses: Int): Flow<List<CategorizedExpense>> {
        return expenseDao.getExpenses(numberOfExpenses).map { expenses ->
            expenses.map { expense ->
                val category = categoryDao.getCategoryById(expense.categoryId)
                val expenseCategory = ExpenseCategory(category.categoryId, category.name)

                with(expense) {
                    CategorizedExpense(
                        expenseId,
                        name,
                        value,
                        date,
                        expenseCategory
                    )
                }
            }
        }
    }
}
