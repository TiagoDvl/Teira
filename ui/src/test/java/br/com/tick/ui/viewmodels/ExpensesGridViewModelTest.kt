package br.com.tick.ui.viewmodels

import app.cash.turbine.test
import br.com.tick.sdk.dispatchers.FakeDispatcher
import br.com.tick.sdk.repositories.FakeCategorizedExpenseRepository
import br.com.tick.sdk.repositories.FakeUserRepository
import br.com.tick.sdk.repositories.categorizedexpense.CategorizedExpenseRepository
import br.com.tick.sdk.repositories.user.UserRepository
import br.com.tick.ui.screens.wallet.states.ExpensesGridStates
import br.com.tick.ui.screens.wallet.usecases.CreateExpensesCards
import br.com.tick.ui.screens.wallet.usecases.GetAvailableBalance
import br.com.tick.ui.screens.wallet.usecases.RemoveExpenseCard
import br.com.tick.ui.screens.wallet.viewmodels.ExpensesGridViewModel
import br.com.tick.utils.CoroutineTestRule
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate


@ExperimentalCoroutinesApi
class ExpensesGridViewModelTest {

    @get:Rule
    val testRule = CoroutineTestRule()

    private fun getViewModel(
        categorizedExpenseRepository: CategorizedExpenseRepository = FakeCategorizedExpenseRepository(),
        userRepository: UserRepository = FakeUserRepository()
    ): ExpensesGridViewModel {
        val createExpensesCards = CreateExpensesCards(categorizedExpenseRepository, userRepository)
        val removeExpenseCard = RemoveExpenseCard(categorizedExpenseRepository)
        val getAvailableBalance = GetAvailableBalance(categorizedExpenseRepository, userRepository)

        return ExpensesGridViewModel(
            createExpensesCards,
            removeExpenseCard,
            getAvailableBalance,
            userRepository,
            FakeDispatcher()
        )
    }

    @Test
    fun `When user has no expenses, expenses grid should be empty`() = runTest {
        val expensesGridViewModel = getViewModel()

        expensesGridViewModel.getExpensesGridState.test {
            assert(awaitItem() is ExpensesGridStates.Empty)
        }
    }

    @Test
    fun `When user has one expense, expenses grid should only have one expense card`() = runTest {
        val categorizedExpenseRepository = FakeCategorizedExpenseRepository()
        val userRepository: UserRepository = FakeUserRepository()

        val expensesGridViewModel = getViewModel(
            categorizedExpenseRepository = categorizedExpenseRepository,
            userRepository = userRepository
        )

        userRepository.setMonthlyIncome(1500.0)
        categorizedExpenseRepository.addExpense(0, "Name_1", 15.0, LocalDate.now())

        expensesGridViewModel.getExpensesGridState.test {
            assert((awaitItem() as ExpensesGridStates.Success).expensesList.size == 1)
        }
    }

    @Test
    fun `When user has multiple expenses, expenses grid should only have 30`() = runTest {
        val categorizedExpenseRepository = FakeCategorizedExpenseRepository()
        val userRepository: UserRepository = FakeUserRepository()

        val expensesGridViewModel = getViewModel(
            categorizedExpenseRepository = categorizedExpenseRepository,
            userRepository = userRepository
        )

        userRepository.setMonthlyIncome(1500.0)
        for (i in 0..50) categorizedExpenseRepository.addExpense(0, "Name_1", 15.0, LocalDate.now())

        expensesGridViewModel.getExpensesGridState.test {
            val expensesGridState = awaitItem()
            assert(expensesGridState is ExpensesGridStates.Success)
            assert((expensesGridState as ExpensesGridStates.Success).expensesList.size == 30)
        }
    }

    @Test
    fun `When user removes one expense, expenses grid should have one less`() = runTest {
        val categorizedExpenseRepository = FakeCategorizedExpenseRepository()
        categorizedExpenseRepository.addExpense(1, "Name One", 10.0, LocalDate.now())
        categorizedExpenseRepository.addExpense(2, "Name Two", 20.0, LocalDate.now())
        categorizedExpenseRepository.addExpense(3, "Name Three", 30.0, LocalDate.now())

        val expensesGridViewModel = getViewModel(categorizedExpenseRepository)

        expensesGridViewModel.removeCard(1)

        expensesGridViewModel.getExpensesGridState.test {
            val expensesGridState = awaitItem()
            val numberOfItems = (expensesGridState as ExpensesGridStates.Success).expensesList.size
            Truth.assertThat(numberOfItems == 2)
        }
    }

    @Test
    fun `When user toggles available balance visibility, it should receive changed value`() = runTest {
        val userRepository = FakeUserRepository()
        userRepository.setInitialUser()

        val expensesGridViewModel = getViewModel(userRepository = userRepository)

        expensesGridViewModel.toggleAvailableBalanceVisibility()

        expensesGridViewModel.availableBalanceState.test {
            Truth.assertThat(awaitItem()?.isVisible == false)
        }
    }
}
