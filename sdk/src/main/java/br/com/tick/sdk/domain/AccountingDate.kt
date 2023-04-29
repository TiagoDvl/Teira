package br.com.tick.sdk.domain

enum class AccountingDate {
    DAY_ONE,
    DAY_FIFTEEN,
    DAY_TWENTY_FIVE
}

fun AccountingDate.getAccountingDateDayOfMonth(): Int {
    return when (this) {
        AccountingDate.DAY_ONE -> 1
        AccountingDate.DAY_FIFTEEN -> 15
        AccountingDate.DAY_TWENTY_FIVE -> 25
    }
}
