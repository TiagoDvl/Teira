package br.com.tick.sdk.repositories.categorizedexpense

import br.com.tick.sdk.database.CategoryColorDao
import br.com.tick.sdk.database.CategoryDao
import br.com.tick.sdk.database.ExpenseDao
import br.com.tick.sdk.database.UserDao
import br.com.tick.sdk.database.entities.Expense
import br.com.tick.sdk.domain.CategorizedExpense
import br.com.tick.sdk.domain.ExpenseCategory
import br.com.tick.sdk.domain.getAccountingDateDayOfMonth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class CategorizedExpensesRepositoryImpl @Inject constructor(
    private val expenseDao: ExpenseDao,
    private val categoryDao: CategoryDao,
    private val categoryColorDao: CategoryColorDao,
    private val userDao: UserDao
) : CategorizedExpenseRepository {

    override suspend fun addExpense(
        categoryId: Int,
        name: String,
        value: Double,
        expenseDate: LocalDate
    ) {
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
                categorize(expense)
            }
        }
    }

    override suspend fun getAccountingCycleExpenses(): Flow<List<CategorizedExpense>> {
        val userAccountingDayOfMonth = userDao.getUniqueUser()
            .filterNotNull()
            .first()
            .accountingDate
            .getAccountingDateDayOfMonth()

        val pivot = LocalDate.now()

        val nextAccountingDate = if (pivot.dayOfMonth > userAccountingDayOfMonth) {
            pivot.plusMonths(1).withDayOfMonth(userAccountingDayOfMonth)
        } else {
            pivot.withDayOfMonth(userAccountingDayOfMonth)
        }

        return expenseDao.getAllExpenses().map { expenses ->
            expenses.filter { expense ->
                val yearDiff = nextAccountingDate.year - expense.date.year
                val monthDiff = nextAccountingDate.month.value - expense.date.month.value

                if (yearDiff != 0) return@filter false

                return@filter when (monthDiff) {
                    0 -> expense.date.dayOfMonth < nextAccountingDate.dayOfMonth
                    1 -> expense.date.dayOfMonth > nextAccountingDate.dayOfMonth
                    else -> false
                }
            }.map { expense ->
                categorize(expense)
            }.sortedBy {
                it.date
            }
        }
    }

    private suspend fun categorize(expense: Expense): CategorizedExpense {
        val category = categoryDao.getCategoryById(expense.categoryId)
        val categoryColorId = category.categoryColorId

        val color = if (categoryColorId != null) {
            val categoryColor = categoryColorDao.getCategoryColors().first().first { it.id == categoryColorId }
            categoryColor.color
        } else {
            null
        }

        val expenseCategory = ExpenseCategory(category.categoryId, category.name, color)

        with(expense) {
            return CategorizedExpense(
                expenseId,
                name,
                value,
                date,
                expenseCategory
            )
        }
    }
}
