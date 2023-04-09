package br.com.tick.ui.screens.analysis.states

import br.com.tick.sdk.domain.CategorizedExpense

sealed class FinancialHealth(val percentageOfCompromisedIncome: Float) {

    companion object {
        fun of(expenses: List<CategorizedExpense>, monthlyIncome: Double): FinancialHealth {
            if (expenses.isEmpty()) return Empty

            val compromisedIncomePercentage = calculateCompromisedIncomePercentage(expenses, monthlyIncome)

            return when {
                compromisedIncomePercentage <= 30 -> Situation.Safe(compromisedIncomePercentage)
                compromisedIncomePercentage <= 70 -> Situation.Caution(compromisedIncomePercentage)
                else -> Situation.Dangerous(compromisedIncomePercentage)
            }
        }

        private fun calculateCompromisedIncomePercentage(
            expenses: List<CategorizedExpense>,
            monthlyIncome: Double
        ): Float {
            var sumOfExpenses = 0.0
            expenses.forEach {
                sumOfExpenses += it.expenseValue
            }
            return (sumOfExpenses * 100 / monthlyIncome).toFloat()
        }
    }

    sealed class Situation(percentageOfCompromisedIncome: Float) : FinancialHealth(percentageOfCompromisedIncome) {

        class Safe(percentageOfCompromisedIncome: Float): Situation(percentageOfCompromisedIncome)
        class Caution(percentageOfCompromisedIncome: Float): Situation(percentageOfCompromisedIncome)
        class Dangerous(percentageOfCompromisedIncome: Float): Situation(percentageOfCompromisedIncome)
    }

    object Empty : FinancialHealth(0f)
}
