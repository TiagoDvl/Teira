package br.com.tick.ui.screens.analysis.states

import br.com.tick.ui.screens.analysis.models.MostExpensiveCategory

sealed class MostExpensiveCategoriesStates {

    companion object {
        fun of(mostExpensiveCategories: List<MostExpensiveCategory>): MostExpensiveCategoriesStates {

            return if (mostExpensiveCategories.isEmpty()) Loading else Full(mostExpensiveCategories.take(5))
        }
    }

    object Loading : MostExpensiveCategoriesStates()

    data class Full(val mostExpensiveCategories: List<MostExpensiveCategory>) : MostExpensiveCategoriesStates()
}
