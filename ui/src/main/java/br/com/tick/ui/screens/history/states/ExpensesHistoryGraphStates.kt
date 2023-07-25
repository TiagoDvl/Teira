package br.com.tick.ui.screens.history.states

import br.com.tick.ui.screens.history.models.MonthlyMergedExpense

sealed class ExpensesHistoryGraphStates {

    object Empty: ExpensesHistoryGraphStates()

    data class Content(val monthlyMergedExpenses: List<MonthlyMergedExpense>): ExpensesHistoryGraphStates()
}