package br.com.tick.ui.viewmodels

import app.cash.turbine.test
import br.com.tick.ui.di.DispatcherProvider
import br.com.tick.ui.dispatchers.FakeDispatcher
import br.com.tick.ui.repositories.FakeDataStoreRepository
import br.com.tick.ui.screens.configuration.viewmodels.SettingsScreenViewModel
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
}