package br.com.tick.ui.screens.analysis.states

import java.time.LocalDate

sealed class AnalysisGraphStates {

    companion object {
        fun of(expenses: Map<LocalDate, Double>): AnalysisGraphStates {
            return if (expenses.isEmpty()) Loading else AnalysisGraph(expenses)
        }
    }

    object Loading : AnalysisGraphStates()
    data class AnalysisGraph(val expenses: Map<LocalDate, Double>) : AnalysisGraphStates()
}
