package br.com.tick.ui.screens.wallet.models

import br.com.tick.sdk.domain.CurrencyFormat

data class AvailableBalance(val currencyFormat: CurrencyFormat, val amount: Double, val isVisible: Boolean)
