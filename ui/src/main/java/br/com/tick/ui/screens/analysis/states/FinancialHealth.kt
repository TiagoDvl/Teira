package br.com.tick.ui.screens.analysis.states

import br.com.tick.sdk.domain.CategorizedExpense

sealed class FinancialHealth {

    companion object {
        fun of(expenses: List<CategorizedExpense>, monthlyIncome: Double): FinancialHealth {
            if (expenses.isEmpty()) return NoDataAvailable

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
            val sumOfExpenses = expenses.sumOf { it.expenseValue }
            return (sumOfExpenses * 100 / monthlyIncome).toFloat()
        }
    }

    sealed class Situation(val percentageOfCompromisedIncome: Float) : FinancialHealth() {

        class Safe(percentageOfCompromisedIncome: Float): Situation(percentageOfCompromisedIncome)
        class Caution(percentageOfCompromisedIncome: Float): Situation(percentageOfCompromisedIncome)
        class Dangerous(percentageOfCompromisedIncome: Float): Situation(percentageOfCompromisedIncome)
    }

    object NoDataAvailable : FinancialHealth()
}
