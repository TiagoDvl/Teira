package br.com.tick.teira.ui.screens.analysis.states

import br.com.tick.teira.ui.datasource.databases.entities.Expense

sealed class FinancialHealth(val percentageOfCompromisedIncome: Float) {

    companion object {
        fun of(expenses: List<Expense>, monthlyIncome: Double): FinancialHealth {
            if (expenses.isEmpty()) return Empty

            return Situation(calculateCompromisedIncomePercentage(expenses, monthlyIncome))
        }

        private fun calculateCompromisedIncomePercentage(expenses: List<Expense>, monthlyIncome: Double): Float {
            var compromisedIncomeInAMonth = 0
            expenses.forEach { compromisedIncomeInAMonth =+ it.value.toInt() }
            return (compromisedIncomeInAMonth * 100 / monthlyIncome).toFloat()
        }
    }

    class Situation(percentageOfCompromisedIncome: Float) : FinancialHealth(percentageOfCompromisedIncome)

    object Empty : FinancialHealth(0f)
}
