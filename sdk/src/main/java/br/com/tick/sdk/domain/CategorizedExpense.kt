package br.com.tick.sdk.domain

import android.net.Uri
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDate

data class CategorizedExpense(
    val expenseId: Int,
    val name: String,
    val expenseValue: Double,
    val date: LocalDate,
    val category: ExpenseCategory,
    val location: LatLng?,
    val picture: Uri?
)
