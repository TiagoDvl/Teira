package br.com.tick.teira.ui.screens.analysis.states

import androidx.compose.ui.graphics.Color
import br.com.tick.teira.ui.datasource.databases.entities.Expense
import br.com.tick.teira.ui.screens.analysis.models.MostExpensiveCategory

sealed class MostExpensiveCategoriesStates {

    companion object {

        // This is a transformation. Theres no way this should be done so closely to VM.
        // This should be done on the repo but i couldn't get my head around it on the repo. Tried a million ways.
        fun of(expenses: List<Expense>): MostExpensiveCategoriesStates {
            val mostExpensiveCategories = mutableListOf<MostExpensiveCategory>()

            expenses.forEach { expense ->
                // This transformation is absolutely horrible. I need to revisit this ASAP.
                if (mostExpensiveCategories.find { it.name == expense.category } == null) {
                    mostExpensiveCategories.add(
                        MostExpensiveCategory(
                            expense.category,
                            Color.Red,
                            expense.value.toDouble()
                        )
                    )
                } else {
                    val mostExpensiveCategory = mostExpensiveCategories.find { it.name == expense.category }
                    mostExpensiveCategory?.amount = mostExpensiveCategory!!.amount + expense.value.toDouble()
                }
            }

            return if (mostExpensiveCategories.isEmpty()) Loading else Full(mostExpensiveCategories)
        }
    }

    object Loading : MostExpensiveCategoriesStates()

    data class Full(val mostExpensiveCategories: List<MostExpensiveCategory>) : MostExpensiveCategoriesStates()
}
