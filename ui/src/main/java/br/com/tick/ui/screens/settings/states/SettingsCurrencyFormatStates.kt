package br.com.tick.ui.screens.settings.states

import android.content.Context
import br.com.tick.sdk.domain.CurrencyFormat
import br.com.tick.ui.R
import br.com.tick.ui.extensions.getLabelResource

sealed class SettingsCurrencyFormatStates {

    object Loading : SettingsCurrencyFormatStates()

    class Content(val currencyFormat: CurrencyFormat) : SettingsCurrencyFormatStates()
}

fun SettingsCurrencyFormatStates.getCurrencyFormatStateLabel(context: Context): String {
    val resourceId = when (this) {
        is SettingsCurrencyFormatStates.Loading -> R.string.generic_loading
        is SettingsCurrencyFormatStates.Content -> currencyFormat.getLabelResource()
    }
    return context.getString(resourceId)
}
