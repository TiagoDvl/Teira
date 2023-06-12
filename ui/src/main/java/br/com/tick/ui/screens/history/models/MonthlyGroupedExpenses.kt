package br.com.tick.ui.screens.history.models

import java.time.LocalDate

data class MonthlyGroupedExpenses(
    val month: String,
    val groupedExpenses: List<Expense>,
    val isExpanded: Boolean
)

data class Expense(
    val date: LocalDate,
    val name: String,
    val value: Double,
    val categoryName: String,
    val categoryColor: Int
)
