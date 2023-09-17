package br.com.tick.ui

import br.com.tick.ui.NavigationItem.Routes.ANALYSIS
import br.com.tick.ui.NavigationItem.Routes.EXPENSE
import br.com.tick.ui.NavigationItem.Routes.EXPENSE_ID_TAG
import br.com.tick.ui.NavigationItem.Routes.HISTORY
import br.com.tick.ui.NavigationItem.Routes.HOME
import br.com.tick.ui.NavigationItem.Routes.SETTINGS
import br.com.tick.ui.NavigationItem.Routes.WALLET

sealed class NavigationItem(var route: String, var iconResource: Int, var titleResource: Int) {

    private object Routes {
        const val HOME = "home"
        const val SETTINGS = "configuration"
        const val WALLET = "wallet"
        const val ANALYSIS = "analysis"
        const val HISTORY = "history"
        const val EXPENSE_ID_TAG = "{expenseId}"
        const val EXPENSE = "editExpense?$EXPENSE_ID_TAG"
    }

    object Home : NavigationItem(HOME, R.drawable.ic_wallet, R.string.navigation_item_wallet)
    object Settings : NavigationItem(SETTINGS, R.drawable.ic_settings, R.string.navigation_item_settings)
    object Wallet : NavigationItem(WALLET, R.drawable.ic_wallet, R.string.navigation_item_wallet)
    object Analysis : NavigationItem(ANALYSIS, R.drawable.ic_analysis, R.string.navigation_item_analysis)
    object History : NavigationItem(HISTORY, R.drawable.ic_history, R.string.navigation_item_history)
    object Expense : NavigationItem(EXPENSE, R.drawable.ic_expense, R.string.navigation_item_expense) {

        const val NAVIGATION_EXPENSE_ID_TAG = "expenseId"
        fun show(expenseId: Int?): String {
            return EXPENSE.replace(EXPENSE_ID_TAG, expenseId.toString())
        }
    }
}
