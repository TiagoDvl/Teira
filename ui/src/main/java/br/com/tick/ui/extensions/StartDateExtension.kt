package br.com.tick.ui.extensions

import br.com.tick.sdk.domain.AccountingDate
import br.com.tick.ui.R

fun AccountingDate.getLabelResource(): Int {
    return when (this) {
        AccountingDate.DAY_ONE -> R.string.settings_accounting_date_one
        AccountingDate.DAY_FIFTEEN -> R.string.settings_accounting_date_fifteen
        AccountingDate.DAY_TWENTY_FIVE -> R.string.settings_accounting_date_twenty_five
    }
}
