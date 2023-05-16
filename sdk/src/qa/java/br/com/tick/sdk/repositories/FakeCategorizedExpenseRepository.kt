package br.com.tick.sdk.repositories

import br.com.tick.sdk.domain.CategorizedExpense
import br.com.tick.sdk.domain.ExpenseCategory
import br.com.tick.sdk.repositories.categorizedexpense.CategorizedExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate

class FakeCategorizedExpenseRepository : CategorizedExpenseRepository {

    private val expenses = mutableListOf<CategorizedExpense>()

    override suspend fun addExpense(categoryId: Int, name: String, value: Double, expenseDate: LocalDate) {
        expenses.add(CategorizedExpense(expenses.size, name, value, expenseDate, ExpenseCategory(categoryId, "", 0)))
    }

    override suspend fun removeExpense(expenseId: Int) {
        expenses.removeAt(expenseId)
    }

    override suspend fun getCategorizedExpenses(numberOfExpenses: Int): Flow<List<CategorizedExpense>> {
        return flowOf(expenses.take(numberOfExpenses))
    }

    override suspend fun getAccountingCycleExpenses(): Flow<List<CategorizedExpense>> {
        return flowOf(expenses.take(30))
    }
}
