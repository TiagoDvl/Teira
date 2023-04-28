package br.com.tick.ui.viewmodels

import app.cash.turbine.test
import br.com.tick.sdk.dispatchers.FakeDispatcher
import br.com.tick.sdk.domain.CurrencyFormat
import br.com.tick.sdk.domain.NotificationPeriodicity
import br.com.tick.sdk.repositories.FakeDataStoreRepository
import br.com.tick.ui.screens.settings.states.SettingsCurrencyFormatStates
import br.com.tick.ui.screens.settings.states.SettingsNotificationPeriodicityStates
import br.com.tick.ui.screens.settings.viewmodels.SettingsScreenViewModel
import br.com.tick.utils.CoroutineTestRule
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SettingsScreenViewModelTest {

    @get:Rule
    val testRule = CoroutineTestRule()

    @Test
    fun `when the user saves its monthly income, local data store should reflect the value`() = runTest {
        val configurationViewModel = SettingsScreenViewModel(FakeDataStoreRepository(), FakeDispatcher())
        val expectedMonthlyIncome = 1500.0

        configurationViewModel.saveMonthlyIncome(expectedMonthlyIncome)
        configurationViewModel.monthlyIncomeFlow.test {
            assertEquals(expectedMonthlyIncome, awaitItem().value)
        }
    }

    @Test
    fun `when the user saves its notification periodicity, local data store should reflect the value`() = runTest {
        val configurationViewModel = SettingsScreenViewModel(FakeDataStoreRepository(), FakeDispatcher())
        val expectedNotificationPeriodicity = NotificationPeriodicity.WEEKLY

        configurationViewModel.setNotificationPeriodicity(expectedNotificationPeriodicity)
        configurationViewModel.notificationPeriodicity.test {
            val content = awaitItem() as SettingsNotificationPeriodicityStates.Content
            assertEquals(expectedNotificationPeriodicity, content.label)
        }
    }

    @Test
    fun `when the user saves its currency format, local data store should reflect the value`() = runTest {
        val configurationViewModel = SettingsScreenViewModel(FakeDataStoreRepository(), FakeDispatcher())
        val expectedCurrencyFormat = CurrencyFormat.REAL

        configurationViewModel.setCurrencyFormat(expectedCurrencyFormat)
        configurationViewModel.currencyFormat.test {
            val content = awaitItem() as SettingsCurrencyFormatStates.Content
            assertEquals(expectedCurrencyFormat, content.currencyFormat)
        }
    }
}
