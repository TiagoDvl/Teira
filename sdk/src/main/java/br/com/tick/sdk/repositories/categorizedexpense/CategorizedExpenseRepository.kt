package br.com.tick.sdk.repositories.categorizedexpense

import android.net.Uri
import br.com.tick.sdk.domain.CategorizedExpense
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface CategorizedExpenseRepository {

    suspend fun addExpense(
        categoryId: Int,
        name: String,
        value: Double,
        expenseDate: LocalDate,
        location: LatLng? = null,
        photoUri: Uri? = null
    )

    suspend fun updateExpense(
        expenseId: Int,
        categoryId: Int,
        name: String,
        value: Double,
        expenseDate: LocalDate,
        location: LatLng? = null,
        photoUri: Uri? = null
    )

    suspend fun removeExpense(expenseId: Int)

    fun getCategorizedExpenses(): Flow<List<CategorizedExpense>>

    suspend fun getAccountingCycleExpenses(): Flow<List<CategorizedExpense>>

    fun getCategorizedExpense(expenseId: Int): Flow<CategorizedExpense>
}
