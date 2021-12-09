package br.com.tick.teira.ui.screens.analysis.states

import br.com.tick.teira.ui.datasource.databases.entities.Expense

sealed class AnalysisGraphStates {

    companion object {
        fun of(expenses: List<Expense>): AnalysisGraphStates {
            return if (expenses.isEmpty()) Loading else AnalysisGraph(expenses)
        }
    }

    object Loading : AnalysisGraphStates()
    data class AnalysisGraph(val expenses: List<Expense>) : AnalysisGraphStates()
}
