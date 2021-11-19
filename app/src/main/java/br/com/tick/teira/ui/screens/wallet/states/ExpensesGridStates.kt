package br.com.tick.teira.ui.screens.wallet.states

import br.com.tick.teira.ui.datasource.databases.entities.Expense

sealed class ExpensesGridStates {

    companion object {

        fun of(expensesList: List<Expense>): ExpensesGridStates {
            if (expensesList.isNotEmpty()) {
                return Success(expensesList)
            }

            return Empty
        }
    }

    object Loading : ExpensesGridStates()
    object Empty : ExpensesGridStates()
    object Error : ExpensesGridStates()
    data class Success(val expensesList: List<Expense>) : ExpensesGridStates()

}
