package br.com.tick.ui.screens.history.models

import java.time.Month

data class MonthlyMergedExpense(
    val month: Month,
    val mergedValues: List<HistoryChartEntry>
)