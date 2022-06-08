package br.com.tick.sdk.domain

data class PersistedMonthlyIncome(val monthlyIncomeValue: Double) {

    companion object {
        const val MONTHLY_INCOME_KEY = "MONTHLY_INCOME_KEY"
    }
}
