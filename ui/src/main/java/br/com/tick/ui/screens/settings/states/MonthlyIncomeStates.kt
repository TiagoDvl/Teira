package br.com.tick.ui.screens.settings.states

sealed class MonthlyIncomeStates {

    companion object {
        fun of(value: Double, monthlyIncomeVisibility: Boolean): MonthlyIncomeStates {
            require(value >= 0.0) { "Negative values should not produce MonthlyIncomeStates" }
            if (value == 0.0) return Loading

            return Value(value, monthlyIncomeVisibility)
        }
    }

    object Loading : MonthlyIncomeStates()

    class Value(val value: Double, val showMonthlyIncome: Boolean) : MonthlyIncomeStates()
}
