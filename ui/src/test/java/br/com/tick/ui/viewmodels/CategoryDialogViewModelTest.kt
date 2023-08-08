package br.com.tick.ui.viewmodels

import app.cash.turbine.test
import br.com.tick.sdk.dispatchers.DispatcherProvider
import br.com.tick.sdk.repositories.FakeCategoryColorRepository
import br.com.tick.sdk.repositories.FakeExpenseCategoryRepository
import br.com.tick.sdk.repositories.categorycolor.CategoryColorRepository
import br.com.tick.sdk.repositories.expensecategory.ExpenseCategoryRepository
import br.com.tick.ui.screens.shared.viewmodels.CategoryDialogViewModel
import br.com.tick.ui.screens.wallet.usecases.GetCategoryColors
import br.com.tick.utils.FakeDispatcher
import br.com.tick.utils.MainDispatcherRule
import com.google.common.truth.Truth
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class CategoryDialogViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun getViewModel(
        expenseCategoryRepository: ExpenseCategoryRepository = FakeExpenseCategoryRepository(),
        categoryColorRepository: CategoryColorRepository = FakeCategoryColorRepository(),
        getCategoryColors: GetCategoryColors = GetCategoryColors(categoryColorRepository),
        dispatcherProvider: DispatcherProvider = FakeDispatcher()
    ): CategoryDialogViewModel {

        return CategoryDialogViewModel(
            expenseCategoryRepository,
            categoryColorRepository,
            getCategoryColors,
            dispatcherProvider
        )
    }

    @Test
    fun `After adding colors, they should be provided`() = runTest {
        val categoryDialogViewModel = getViewModel()

        categoryDialogViewModel.addNewColor(123)
        categoryDialogViewModel.categoryColors.test {
            val colors = awaitItem()
            Truth.assertThat(colors).containsExactly(123)
        }
    }
}