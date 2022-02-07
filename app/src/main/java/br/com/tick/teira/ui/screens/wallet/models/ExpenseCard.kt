package br.com.tick.teira.ui.screens.wallet.models

import br.com.tick.teira.ui.datasource.domain.ExpenseCategory

data class ExpenseCard(
    val id: Int,
    val name: String,
    val value: Double,
    val category: ExpenseCategory,
    val risk: ExpenseRisk
)
