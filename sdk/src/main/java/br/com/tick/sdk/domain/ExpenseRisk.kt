package br.com.tick.sdk.domain

/**
 * This class holds business logic to determine weather an expense value is categorized as certain threshold.
 */
enum class ExpenseRisk(private val percentageThreshold: Double) {
    HIGHEST(20.0),
    HIGH(10.0),
    MEDIUM(2.0),
    LOW(0.5),
    LOWEST(0.2);

    companion object {
        fun getRiskFromValue(monthlyIncome: Double, expenseValue: Double): ExpenseRisk {
            require(expenseValue > 0) { "There's no way to calculate risks for a negative value. What is that anyway?" }

            return when ((expenseValue * 100) / monthlyIncome) {
                in 0.0..LOWEST.percentageThreshold -> LOWEST
                in LOWEST.percentageThreshold..LOW.percentageThreshold -> LOW
                in LOW.percentageThreshold..MEDIUM.percentageThreshold -> MEDIUM
                in MEDIUM.percentageThreshold..HIGH.percentageThreshold -> HIGH
                else -> HIGHEST
            }
        }
    }
}
