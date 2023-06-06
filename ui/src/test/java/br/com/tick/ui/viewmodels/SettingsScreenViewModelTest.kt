package br.com.tick.ui.viewmodels

import app.cash.turbine.test
import br.com.tick.sdk.dispatchers.DispatcherProvider
import br.com.tick.sdk.domain.CurrencyFormat
import br.com.tick.sdk.domain.NotificationPeriodicity
import br.com.tick.sdk.repositories.FakeCategoryColorRepository
import br.com.tick.sdk.repositories.FakeExpenseCategoryRepository
import br.com.tick.sdk.repositories.FakeUserRepository
import br.com.tick.sdk.repositories.categorycolor.CategoryColorRepository
import br.com.tick.sdk.repositories.expensecategory.ExpenseCategoryRepository
import br.com.tick.sdk.repositories.user.UserRepository
import br.com.tick.ui.screens.settings.viewmodels.SettingsScreenViewModel
import br.com.tick.utils.FakeDispatcher
import br.com.tick.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SettingsScreenViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun getViewModel(
        userRepository: UserRepository = FakeUserRepository(),
        expenseCategoryRepository: ExpenseCategoryRepository = FakeExpenseCategoryRepository(),
        categoryColorRepository: CategoryColorRepository = FakeCategoryColorRepository(),
        dispatcherProvider: DispatcherProvider = FakeDispatcher()
    ): SettingsScreenViewModel {
        return SettingsScreenViewModel(
            userRepository,
            expenseCategoryRepository,
            categoryColorRepository,
            dispatcherProvider
        )
    }

    @Test
    fun `when the user saves its monthly income, local data store should reflect the value`() = runTest {
        val configurationViewModel = getViewModel()
        val expectedMonthlyIncome = 1500.0

        configurationViewModel.saveMonthlyIncome(expectedMonthlyIncome)
        configurationViewModel.monthlyIncomeFlow.test {
            assertEquals(expectedMonthlyIncome, awaitItem().value, expectedMonthlyIncome)
        }
    }

    @Test
    fun `when the user saves its notification periodicity, local data store should reflect the value`() = runTest {
        val configurationViewModel = getViewModel()
        val expectedNotificationPeriodicity = NotificationPeriodicity.WEEKLY

        configurationViewModel.setNotificationPeriodicity(expectedNotificationPeriodicity)
        configurationViewModel.notificationPeriodicity.test {
            val content = awaitItem()
            assertEquals(expectedNotificationPeriodicity, content.label)
        }
    }

    @Test
    fun `when the user saves its currency format, local data store should reflect the value`() = runTest {
        val configurationViewModel = getViewModel()
        val expectedCurrencyFormat = CurrencyFormat.REAL

        configurationViewModel.setCurrencyFormat(expectedCurrencyFormat)
        configurationViewModel.currencyFormat.test {
            val content = awaitItem()
            assertEquals(expectedCurrencyFormat, content.currencyFormat)
        }
    }
}
