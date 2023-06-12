package br.com.tick.sdk.repositories.categorizedexpense

import br.com.tick.sdk.domain.CategorizedExpense
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface CategorizedExpenseRepository {

    suspend fun addExpense(categoryId: Int, name: String, value: Double, expenseDate: LocalDate)

    suspend fun removeExpense(expenseId: Int)

    fun getCategorizedExpenses(): Flow<List<CategorizedExpense>>

    suspend fun getAccountingCycleExpenses(): Flow<List<CategorizedExpense>>
}
