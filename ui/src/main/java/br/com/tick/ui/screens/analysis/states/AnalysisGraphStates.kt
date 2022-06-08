package br.com.tick.ui.screens.analysis.states

import br.com.tick.sdk.domain.CategorizedExpense

sealed class AnalysisGraphStates {

    companion object {
        fun of(expenses: List<CategorizedExpense>): AnalysisGraphStates {
            return if (expenses.isEmpty()) Loading else AnalysisGraph(expenses)
        }
    }

    object Loading : AnalysisGraphStates()
    data class AnalysisGraph(val expenses: List<CategorizedExpense>) : AnalysisGraphStates()
}
