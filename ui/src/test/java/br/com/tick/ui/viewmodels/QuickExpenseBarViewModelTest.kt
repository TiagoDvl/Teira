package br.com.tick.ui.viewmodels

import app.cash.turbine.test
import br.com.tick.sdk.database.entities.CategoryColor
import br.com.tick.sdk.dispatchers.DispatcherProvider
import br.com.tick.sdk.dispatchers.FakeDispatcher
import br.com.tick.sdk.repositories.FakeCategorizedExpenseRepository
import br.com.tick.sdk.repositories.FakeCategoryColorRepository
import br.com.tick.sdk.repositories.FakeExpenseCategoryRepository
import br.com.tick.sdk.repositories.FakeUserRepository
import br.com.tick.sdk.repositories.categorizedexpense.CategorizedExpenseRepository
import br.com.tick.sdk.repositories.categorycolor.CategoryColorRepository
import br.com.tick.sdk.repositories.expensecategory.ExpenseCategoryRepository
import br.com.tick.sdk.repositories.user.UserRepository
import br.com.tick.ui.screens.wallet.viewmodels.QuickExpenseBarViewModel
import br.com.tick.utils.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class QuickExpenseBarViewModelTest {

    @get:Rule
    val testRule = CoroutineTestRule()

    private fun getViewModel(
        expenseRepository: CategorizedExpenseRepository = FakeCategorizedExpenseRepository(),
        categoryRepository: ExpenseCategoryRepository = FakeExpenseCategoryRepository(),
        categoryColorRepository: CategoryColorRepository = FakeCategoryColorRepository(),
        userRepository: UserRepository = FakeUserRepository(),
        dispatcherProvider: DispatcherProvider = FakeDispatcher()
    ): QuickExpenseBarViewModel {
        return QuickExpenseBarViewModel(
            expenseRepository,
            categoryRepository,
            categoryColorRepository,
            userRepository,
            dispatcherProvider
        )
    }

    @Test
    fun `When user has no categories created, it should have no categories to choose from`() = runTest {
        val quickExpenseBarViewModel = getViewModel()
        quickExpenseBarViewModel.categories.test {
            assert(awaitItem().isEmpty())
            awaitComplete()
        }
    }

    @Test
    fun `When user has a category created, it should have at least one to choose from`() = runTest {
        val categoryRepository = FakeExpenseCategoryRepository()
        val quickExpenseBarViewModel = getViewModel(categoryRepository = categoryRepository)

        val categoryName = "Name_1"
        categoryRepository.addCategory(categoryName, CategoryColor(color = 0))

        quickExpenseBarViewModel.categories.test {
            assert(awaitItem()[0].name == categoryName)
            awaitComplete()
        }
    }
}
