package br.com.tick.ui.screens.settings.states

import br.com.tick.sdk.domain.AccountingDate

sealed class SettingsAccountingDateStates {

    object Loading : SettingsAccountingDateStates()

    class Content(val accountingDate: AccountingDate) : SettingsAccountingDateStates()
}