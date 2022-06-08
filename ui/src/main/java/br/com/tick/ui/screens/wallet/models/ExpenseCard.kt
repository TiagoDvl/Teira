package br.com.tick.ui.screens.wallet.models

import br.com.tick.sdk.domain.ExpenseRisk

data class ExpenseCard(
    val id: Int,
    val name: String,
    val value: Double,
    val category: br.com.tick.sdk.domain.ExpenseCategory,
    val risk: ExpenseRisk
)
