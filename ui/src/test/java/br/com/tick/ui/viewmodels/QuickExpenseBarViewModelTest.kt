package br.com.tick.ui.viewmodels

import app.cash.turbine.test
import br.com.tick.sdk.repositories.CategorizedExpenseRepository
import br.com.tick.sdk.repositories.ExpenseCategoryRepository
import br.com.tick.ui.repositories.FakeCategorizedExpenseRepository
import br.com.tick.ui.repositories.FakeExpenseCategoryRepository
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
        categoryRepository: ExpenseCategoryRepository = FakeExpenseCategoryRepository()
    ): QuickExpenseBarViewModel {
        return QuickExpenseBarViewModel(expenseRepository, categoryRepository)
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
        categoryRepository.addCategory(categoryName)

        quickExpenseBarViewModel.categories.test {
            assert(awaitItem()[0].name == categoryName)
            awaitComplete()
        }
    }
}