package br.com.tick.ui.viewmodels

import app.cash.turbine.test
import br.com.tick.sdk.repositories.categorizedexpense.CategorizedExpenseRepository
import br.com.tick.sdk.repositories.localdata.LocalDataRepository
import br.com.tick.sdk.dispatchers.FakeDispatcher
import br.com.tick.sdk.repositories.FakeCategorizedExpenseRepository
import br.com.tick.sdk.repositories.FakeDataStoreRepository
import br.com.tick.ui.screens.wallet.states.ExpensesGridStates
import br.com.tick.ui.screens.wallet.usecases.CreateExpensesCards
import br.com.tick.ui.screens.wallet.usecases.RemoveExpenseCard
import br.com.tick.ui.screens.wallet.viewmodels.ExpensesGridViewModel
import br.com.tick.utils.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class ExpensesGridViewModelTest {

    @get:Rule
    val testRule = CoroutineTestRule()

    private fun getViewModel(
        categorizedExpenseRepository: CategorizedExpenseRepository = FakeCategorizedExpenseRepository(),
        localDataRepository: LocalDataRepository = FakeDataStoreRepository()
    ): ExpensesGridViewModel {
        val createExpensesCards = CreateExpensesCards(categorizedExpenseRepository, localDataRepository)
        val removeExpenseCard = RemoveExpenseCard(categorizedExpenseRepository)

        return ExpensesGridViewModel(createExpensesCards, removeExpenseCard, FakeDispatcher())
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
        val localDataRepository = FakeDataStoreRepository()

        val expensesGridViewModel = getViewModel(
            categorizedExpenseRepository = categorizedExpenseRepository,
            localDataRepository = localDataRepository
        )

        localDataRepository.saveMonthlyIncome(1500.0)
        categorizedExpenseRepository.addExpense(0, "Name_1", 15.0, 1234)

        expensesGridViewModel.getExpensesGridState.test {
            assert((awaitItem() as ExpensesGridStates.Success).expensesList.size == 1)
        }
    }

    @Test
    fun `When user has multiple expenses, expenses grid should only have 30`() = runTest {
        val categorizedExpenseRepository = FakeCategorizedExpenseRepository()
        val localDataRepository = FakeDataStoreRepository()

        val expensesGridViewModel = getViewModel(
            categorizedExpenseRepository = categorizedExpenseRepository,
            localDataRepository = localDataRepository
        )

        localDataRepository.saveMonthlyIncome(1500.0)
        for (i in 0..50) categorizedExpenseRepository.addExpense(0, "Name_1", 15.0, 1234)

        expensesGridViewModel.getExpensesGridState.test {
            val expensesGridState = awaitItem()
            assert(expensesGridState is ExpensesGridStates.Success)
            assert((expensesGridState as ExpensesGridStates.Success).expensesList.size == 30)
        }
    }
}