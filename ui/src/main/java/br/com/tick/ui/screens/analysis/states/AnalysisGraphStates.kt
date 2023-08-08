package br.com.tick.ui.screens.analysis.states

import java.time.LocalDate

sealed class AnalysisGraphStates {

    companion object {
        fun of(expenses: Map<LocalDate, Double>): AnalysisGraphStates {
            return if (expenses.isEmpty()) NoDataAvailable else AnalysisGraph(expenses)
        }
    }

    object NoDataAvailable : AnalysisGraphStates()
    data class AnalysisGraph(val expenses: Map<LocalDate, Double>) : AnalysisGraphStates()
}
