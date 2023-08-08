package br.com.tick.ui.viewmodels

import app.cash.turbine.test
import br.com.tick.sdk.dispatchers.DispatcherProvider
import br.com.tick.sdk.repositories.FakeCategorizedExpenseRepository
import br.com.tick.sdk.repositories.FakeExpenseCategoryRepository
import br.com.tick.sdk.repositories.FakeUserRepository
import br.com.tick.sdk.repositories.categorizedexpense.CategorizedExpenseRepository
import br.com.tick.sdk.repositories.expensecategory.ExpenseCategoryRepository
import br.com.tick.sdk.repositories.user.UserRepository
import br.com.tick.ui.screens.wallet.viewmodels.QuickExpenseBarViewModel
import br.com.tick.utils.FakeDispatcher
import br.com.tick.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class QuickExpenseBarViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun getViewModel(
        expenseRepository: CategorizedExpenseRepository = FakeCategorizedExpenseRepository(),
        categoryRepository: ExpenseCategoryRepository = FakeExpenseCategoryRepository(),
        userRepository: UserRepository = FakeUserRepository(),
        dispatcherProvider: DispatcherProvider = FakeDispatcher()
    ): QuickExpenseBarViewModel {
        return QuickExpenseBarViewModel(
            expenseRepository,
            categoryRepository,
            userRepository,
            dispatcherProvider
        )
    }

    @Test
    fun `When user has no categories created, it should have no categories to choose from`() = runTest {
        val quickExpenseBarViewModel = getViewModel()
        quickExpenseBarViewModel.categories.test {
            assert(awaitItem().isEmpty())
        }
    }

    @Test
    fun `When user has a category created, it should have at least one to choose from`() = runTest {
        val categoryRepository = FakeExpenseCategoryRepository()
        val quickExpenseBarViewModel = getViewModel(categoryRepository = categoryRepository)

        val categoryName = "Name_1"
        categoryRepository.addExpenseCategory(categoryName, 0)

        quickExpenseBarViewModel.categories.test {
            assert(awaitItem()[0].name == categoryName)
        }
    }
}
