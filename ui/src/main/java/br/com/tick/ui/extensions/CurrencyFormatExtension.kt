package br.com.tick.ui.extensions

import br.com.tick.sdk.domain.CurrencyFormat
import br.com.tick.ui.R

fun CurrencyFormat.getLabelResource(): Int {
    return when (this) {
        CurrencyFormat.REAL -> R.string.settings_currency_format_real
        CurrencyFormat.EURO -> R.string.settings_currency_format_euro
        CurrencyFormat.DOLLAR -> R.string.settings_currency_format_dollar
        CurrencyFormat.YEN -> R.string.settings_currency_format_yen
        CurrencyFormat.POUNDS -> R.string.settings_currency_format_pounds
    }
}
