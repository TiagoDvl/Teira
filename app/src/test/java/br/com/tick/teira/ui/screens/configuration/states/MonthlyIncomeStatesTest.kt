package br.com.tick.teira.ui.screens.configuration.states

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class MonthlyIncomeStatesTest {

    @Test
    fun `when monthly income is zero, a Loading state should be returned`() {
        val monthlyStateValue = 0.0
        val monthlyIncomeStates = MonthlyIncomeStates.of(monthlyStateValue)

        assertThat(monthlyIncomeStates).isInstanceOf(MonthlyIncomeStates.Loading::class.java)
    }

    @Test
    fun `when monthly income is more than zero, a Value state should be returned`() {
        val monthlyStateValue = Double.POSITIVE_INFINITY
        val monthlyIncomeStates = MonthlyIncomeStates.of(monthlyStateValue)

        assertThat(monthlyIncomeStates).isInstanceOf(MonthlyIncomeStates.Value::class.java)
    }

    @Test
    fun `when monthly income is less than zero, state should produce an exception`() {
        try {
            val monthlyStateValue = Double.NEGATIVE_INFINITY
            MonthlyIncomeStates.of(monthlyStateValue)
        } catch (iae: IllegalArgumentException) {
            assertThat(iae.message).isNotEmpty()
        }
    }
}