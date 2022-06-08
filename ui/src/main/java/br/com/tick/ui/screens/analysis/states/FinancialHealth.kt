package br.com.tick.ui.screens.analysis.states

import br.com.tick.sdk.domain.CategorizedExpense

sealed class FinancialHealth(val percentageOfCompromisedIncome: Float) {

    companion object {
        fun of(expenses: List<CategorizedExpense>, monthlyIncome: Double): FinancialHealth {
            if (expenses.isEmpty()) return Empty

            return Situation(calculateCompromisedIncomePercentage(expenses, monthlyIncome))
        }

        private fun calculateCompromisedIncomePercentage(
            expenses: List<CategorizedExpense>,
            monthlyIncome: Double
        ): Float {
            var compromisedIncomeInAMonth = 0
            expenses.forEach { compromisedIncomeInAMonth = +it.expenseValue.toInt() }
            return (compromisedIncomeInAMonth * 100 / monthlyIncome).toFloat()
        }
    }

    class Situation(percentageOfCompromisedIncome: Float) : FinancialHealth(percentageOfCompromisedIncome)

    object Empty : FinancialHealth(0f)
}
