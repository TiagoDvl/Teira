package br.com.tick.ui.screens.settings.states

sealed class MonthlyIncomeStates {

    object Loading : MonthlyIncomeStates()

    class Value(val value: Double, val showMonthlyIncome: Boolean) : MonthlyIncomeStates()
}
