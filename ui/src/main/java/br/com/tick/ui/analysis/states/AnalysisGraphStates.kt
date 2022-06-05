package br.com.tick.ui.analysis.states

import br.com.tick.sdk.database.entities.Expense

sealed class AnalysisGraphStates {

    companion object {
        fun of(expenses: List<Expense>): AnalysisGraphStates {
            return if (expenses.isEmpty()) Loading else AnalysisGraph(expenses)
        }
    }

    object Loading : AnalysisGraphStates()
    data class AnalysisGraph(val expenses: List<Expense>) : AnalysisGraphStates()
}
