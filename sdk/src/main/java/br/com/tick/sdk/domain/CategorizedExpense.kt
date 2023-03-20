package br.com.tick.sdk.domain

import java.time.LocalDate

data class CategorizedExpense(
    val expenseId: Int,
    val name: String,
    val expenseValue: Double,
    val date: LocalDate,
    val category: ExpenseCategory
)
