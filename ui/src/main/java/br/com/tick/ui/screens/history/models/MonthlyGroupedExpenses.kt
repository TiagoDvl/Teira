package br.com.tick.ui.screens.history.models

import br.com.tick.sdk.domain.CurrencyFormat
import java.time.LocalDate

data class MonthlyGroupedExpenses(
    val month: String,
    val groupedExpenses: List<GroupedExpense>,
    val isExpanded: Boolean
)

data class GroupedExpense(
    val date: LocalDate,
    val name: String,
    val value: Double,
    val categoryName: String,
    val categoryColor: Int?,
    val currency: CurrencyFormat
)
