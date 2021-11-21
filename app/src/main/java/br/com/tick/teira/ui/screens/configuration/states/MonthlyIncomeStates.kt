package br.com.tick.teira.ui.screens.configuration.states

sealed class MonthlyIncomeStates(val value: Double) {

    object Loading: MonthlyIncomeStates(0.0)

    class Value(value: Double): MonthlyIncomeStates(value)
}
