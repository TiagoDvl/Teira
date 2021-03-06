package br.com.tick.ui.screens.configuration.states

sealed class MonthlyIncomeStates(val value: Double) {

    companion object {
        fun of(value: Double): MonthlyIncomeStates {
            require(value >= 0.0) { "Negative values should not produce MonthlyIncomeStates" }
            if (value == 0.0) return Loading

            return Value(value)
        }
    }

    object Loading : MonthlyIncomeStates(0.0)

    class Value(value: Double) : MonthlyIncomeStates(value)
}
