package br.com.tick.ui.screens.wallet.models

import br.com.tick.sdk.domain.CurrencyFormat
import br.com.tick.sdk.domain.ExpenseCategory
import br.com.tick.sdk.domain.ExpenseRisk

data class ExpenseCard(
    val id: Int,
    val name: String,
    val value: Double,
    val currency: CurrencyFormat,
    val category: ExpenseCategory,
    val risk: ExpenseRisk
)
