package br.com.tick.sdk.domain

data class CategorizedExpense(
    val expenseId: Int,
    val name: String,
    val expenseValue: Double,
    val date: Long,
    val category: ExpenseCategory
)
