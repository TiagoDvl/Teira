package br.com.tick.sdk.repositories

import android.net.Uri
import br.com.tick.sdk.domain.CategorizedExpense
import br.com.tick.sdk.domain.CurrencyFormat
import br.com.tick.sdk.domain.ExpenseCategory
import br.com.tick.sdk.repositories.categorizedexpense.CategorizedExpenseRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate

class FakeCategorizedExpenseRepository : CategorizedExpenseRepository {

    private val expenses = mutableListOf<CategorizedExpense>()

    override suspend fun addExpense(
        categoryId: Int,
        name: String,
        value: Double,
        expenseDate: LocalDate,
        location: LatLng?,
        photoUri: Uri?
    ) {
        expenses.add(
            CategorizedExpense(
                expenses.size,
                name,
                value,
                CurrencyFormat.EURO,
                expenseDate,
                ExpenseCategory(categoryId, "", 0),
                LatLng(0.0, 0.0),
                Uri.EMPTY
            )
        )
    }

    override suspend fun updateExpense(
        expenseId: Int,
        categoryId: Int,
        name: String,
        value: Double,
        expenseDate: LocalDate,
        location: LatLng?,
        photoUri: Uri?
    ) {
        expenses[expenseId] = expenses[expenseId].copy(
            expenseId = expenseId,
            name = name,
            expenseValue = value,
            date = expenseDate,
            location = location,
            picture = photoUri
        )
    }

    override suspend fun removeExpense(expenseId: Int) {
        expenses.removeAt(expenseId)
    }

    override fun getCategorizedExpenses(): Flow<List<CategorizedExpense>> {
        return flowOf(expenses)
    }

    override suspend fun getAccountingCycleExpenses(): Flow<List<CategorizedExpense>> {
        return flowOf(expenses.take(30))
    }

    override fun getCategorizedExpense(expenseId: Int): Flow<CategorizedExpense> {
        return flowOf(expenses[expenseId])
    }
}
