package br.com.tick.ui.screens.history.states

import br.com.tick.ui.screens.history.models.MonthlyGroupedExpenses

sealed class ExpensesHistoryStates {

    object Empty: ExpensesHistoryStates()

    data class Content(val expenseHistory: List<MonthlyGroupedExpenses>): ExpensesHistoryStates()
}