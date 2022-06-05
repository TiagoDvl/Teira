package br.com.tick.ui.viewmodels

import app.cash.turbine.test
import br.com.tick.sdk.repositories.CategoryRepository
import br.com.tick.sdk.repositories.ExpenseRepository
import br.com.tick.sdk.repositories.LocalDataRepository
import br.com.tick.ui.dispatchers.FakeDispatcher
import br.com.tick.ui.repositories.FakeCategoryRepository
import br.com.tick.ui.repositories.FakeExpenseRepository
import br.com.tick.ui.repositories.FakeLocalDataRepository
import br.com.tick.ui.usecases.CreateExpensesCards
import br.com.tick.ui.usecases.RemoveExpenseCard
import br.com.tick.ui.wallet.states.ExpensesGridStates
import br.com.tick.ui.wallet.viewmodels.ExpensesGridViewModel
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
        expenseRepository: ExpenseRepository = FakeExpenseRepository(),
        categoryRepository: CategoryRepository = FakeCategoryRepository(),
        localDataRepository: LocalDataRepository = FakeLocalDataRepository()
    ): ExpensesGridViewModel {
        val createExpensesCards = CreateExpensesCards(expenseRepository, categoryRepository, localDataRepository)
        val removeExpenseCard = RemoveExpenseCard(expenseRepository)

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
        val expenseRepository = FakeExpenseRepository()
        val categoryRepository = FakeCategoryRepository()
        val localDataRepository = FakeLocalDataRepository()

        val expensesGridViewModel = getViewModel(
            expenseRepository = expenseRepository,
            categoryRepository = categoryRepository,
            localDataRepository = localDataRepository
        )

        localDataRepository.saveMonthlyIncome(1500.0)
        categoryRepository.addCategory("Name_1")
        expenseRepository.addExpense(0, "Name_1", 15.0, 1234)

        expensesGridViewModel.getExpensesGridState.test {
            assert((awaitItem() as ExpensesGridStates.Success).expensesList.size == 1)
        }
    }

    @Test
    fun `When user has multiple expenses, expenses grid should only have 30`() = runTest {
        val expenseRepository = FakeExpenseRepository()
        val categoryRepository = FakeCategoryRepository()
        val localDataRepository = FakeLocalDataRepository()

        val expensesGridViewModel = getViewModel(
            expenseRepository = expenseRepository,
            categoryRepository = categoryRepository,
            localDataRepository = localDataRepository
        )

        localDataRepository.saveMonthlyIncome(1500.0)
        categoryRepository.addCategory("Name_1")
        for (i in 0..50) expenseRepository.addExpense(0, "Name_1", 15.0, 1234)

        expensesGridViewModel.getExpensesGridState.test {
            val expensesGridState = awaitItem()
            assert(expensesGridState is ExpensesGridStates.Success)
            assert((expensesGridState as ExpensesGridStates.Success).expensesList.size == 30)
        }
    }
}