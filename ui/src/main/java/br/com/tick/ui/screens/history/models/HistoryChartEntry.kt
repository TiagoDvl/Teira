package br.com.tick.ui.screens.history.models

import com.patrykandpatrick.vico.core.entry.ChartEntry

class HistoryChartEntry(
    override val x: Float,
    override val y: Float
) : ChartEntry {

    override fun withY(y: Float) = HistoryChartEntry(x, y)

    override fun toString(): String {
        return "(x,y) -> ($x/$y)"
    }
}