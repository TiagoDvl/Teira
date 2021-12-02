package br.com.tick.teira.ui.screens.wallet.states

import br.com.tick.teira.ui.screens.wallet.models.ExpenseCard

sealed class ExpensesGridStates {

    companion object {

        fun of(expensesCards: List<ExpenseCard>): ExpensesGridStates {
            return if (expensesCards.isNotEmpty()) Success(expensesCards) else Empty
        }
    }

    object Loading : ExpensesGridStates()
    object Empty : ExpensesGridStates()
    object Error : ExpensesGridStates()
    data class Success(val expensesList: List<ExpenseCard>) : ExpensesGridStates()

}
