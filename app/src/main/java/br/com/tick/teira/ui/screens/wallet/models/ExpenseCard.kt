package br.com.tick.teira.ui.screens.wallet.models

data class ExpenseCard(
    val id: Int,
    val name: String,
    val value: Double,
    val category: ExpenseCategory,
    val risk: ExpenseRisk
)
