package br.com.tick.ui.wallet.models

data class ExpenseCard(
    val id: Int,
    val name: String,
    val value: Double,
    val category: br.com.tick.sdk.domain.ExpenseCategory,
    val risk: ExpenseRisk
)
