package br.com.tick.ui

import br.com.tick.R
import br.com.tick.ui.NavigationItem.Routes.ANALYSIS
import br.com.tick.ui.NavigationItem.Routes.SETTINGS
import br.com.tick.ui.NavigationItem.Routes.WALLET

sealed class NavigationItem(var route: String, var iconResource: Int, var titleResource: Int) {

    private object Routes {
        const val SETTINGS = "configuration"
        const val WALLET = "wallet"
        const val ANALYSIS = "analysis"
    }


    object Settings : NavigationItem(SETTINGS, R.drawable.ic_settings, R.string.navigation_item_settings)
    object Wallet : NavigationItem(WALLET, R.drawable.ic_wallet, R.string.navigation_item_wallet)
    object Analysis : NavigationItem(ANALYSIS, R.drawable.ic_analysis, R.string.navigation_item_analysis)
}
