package br.com.tick.teira.ui.screens.configuration.viewmodels

import app.cash.turbine.test
import br.com.tick.teira.ui.datasource.repositories.CategoryRepository
import br.com.tick.teira.ui.datasource.repositories.ExpenseRepository
import br.com.tick.teira.ui.datasource.repositories.FakeCategoryRepository
import br.com.tick.teira.ui.datasource.repositories.FakeExpenseRepository
import br.com.tick.teira.ui.datasource.repositories.FakeLocalDataRepository
import br.com.tick.teira.ui.datasource.repositories.LocalDataRepository
import br.com.tick.teira.ui.screens.analysis.states.AnalysisGraphStates
import br.com.tick.teira.ui.screens.analysis.states.MostExpensiveCategoriesStates
import br.com.tick.teira.ui.screens.analysis.usecases.CalculateFinancialHealthSituation
import br.com.tick.teira.ui.screens.analysis.usecases.FetchLastMonthExpenses
import br.com.tick.teira.ui.screens.analysis.usecases.GetMostExpensiveCategories
import br.com.tick.teira.ui.screens.analysis.viewmodels.AnalysisScreenViewModel
import br.com.tick.teira.utils.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AnalysisScreenViewModelTest {

    @get:Rule
    val testRule = CoroutineTestRule()

    private fun getViewModel(
        expenseRepository: ExpenseRepository = FakeExpenseRepository(),
        categoryRepository: CategoryRepository = FakeCategoryRepository(),
        localDataRepository: LocalDataRepository = FakeLocalDataRepository(),
    ): AnalysisScreenViewModel {
        val fetchExpenses = FetchLastMonthExpenses(expenseRepository)
        val getMostExpensiveCategories = GetMostExpensiveCategories(expenseRepository, categoryRepository)
        val calculateFinancialHealthSituation =
            CalculateFinancialHealthSituation(expenseRepository, localDataRepository)

        return AnalysisScreenViewModel(
            fetchExpenses,
            getMostExpensiveCategories,
            calculateFinancialHealthSituation
        )
    }

    @Test
    fun `When adding a single expense, that one must be the most expensive one`() = runTest {
        val expenseRepository = FakeExpenseRepository()
        val categoryRepository = FakeCategoryRepository()
        val analysisScreenViewModel = getViewModel(
            expenseRepository = expenseRepository,
            categoryRepository = categoryRepository
        )

        val categoryName = "Name_1"
        categoryRepository.addCategory(categoryName)
        expenseRepository.addExpense(0, categoryName, 15.0, 1234)

        analysisScreenViewModel.mostExpenseCategoryList.test {
            val expensiveCategories = awaitItem()
            assert(expensiveCategories is MostExpensiveCategoriesStates.Full)

            val expensiveCategoriesFull = expensiveCategories as MostExpensiveCategoriesStates.Full
            assert(expensiveCategoriesFull.mostExpensiveCategories[0].categoryName == categoryName)

            awaitComplete()
        }
    }

    @Test
    fun `When adding two expenses in different categories, the most expensive must be the highest expense category`() =
        runTest {
            val expenseRepository = FakeExpenseRepository()
            val categoryRepository = FakeCategoryRepository()
            val analysisScreenViewModel = getViewModel(
                expenseRepository = expenseRepository,
                categoryRepository = categoryRepository
            )

            val categoryName = "Name_1"
            val highestCategoryExpense = 15.0
            categoryRepository.addCategory(categoryName)
            expenseRepository.addExpense(0, categoryName, highestCategoryExpense, 1234)

            val secondCategoryName = "Name_2"
            val lowestCategoryExpense = 14.0
            categoryRepository.addCategory(secondCategoryName)
            expenseRepository.addExpense(1, secondCategoryName, lowestCategoryExpense, 1234)

            assert(highestCategoryExpense > lowestCategoryExpense)
            assert(categoryName != secondCategoryName)
            analysisScreenViewModel.mostExpenseCategoryList.test {
                val expensiveCategoriesFull = awaitItem() as MostExpensiveCategoriesStates.Full
                assert(expensiveCategoriesFull.mostExpensiveCategories[0].categoryName == categoryName)
                awaitComplete()
            }
        }

    @Test
    fun `When adding multiple expenses in multiple categories, expense list should only have the top five`() = runTest {
        val expenseRepository = FakeExpenseRepository()
        val categoryRepository = FakeCategoryRepository()
        val analysisScreenViewModel = getViewModel(
            expenseRepository = expenseRepository,
            categoryRepository = categoryRepository
        )

        for (i in 0..5) categoryRepository.addCategory("Name_$i")
        for (i in 0..5) expenseRepository.addExpense(i, "Name_$i", 10.0 + i, 1234)

        analysisScreenViewModel.mostExpenseCategoryList.test {
            val expensiveCategoriesFull = awaitItem() as MostExpensiveCategoriesStates.Full
            for (i in 0..4) {
                assert(expensiveCategoriesFull.mostExpensiveCategories[i].categoryName == "Name_${5 - i}")
            }
            awaitComplete()
        }
    }

    @Test
    fun `When user made an expense, financial health should reflect this change`() = runTest {
        val expenseRepository = FakeExpenseRepository()
        val localDataRepository = FakeLocalDataRepository()
        val analysisScreenViewModel = getViewModel(
            expenseRepository = expenseRepository,
            localDataRepository = localDataRepository
        )

        localDataRepository.saveMonthlyIncome(1500.0)
        expenseRepository.addExpense(0, "Name_1", 10.0, 1234)

        analysisScreenViewModel.financialHealthSituation.test {
            assert(awaitItem().percentageOfCompromisedIncome > 0)
        }
    }

    @Test
    fun `When user has no expenses, financial health should be 0`() = runTest {
        val expenseRepository = FakeExpenseRepository()
        val localDataRepository = FakeLocalDataRepository()
        val analysisScreenViewModel = getViewModel(
            expenseRepository = expenseRepository,
            localDataRepository = localDataRepository
        )

        localDataRepository.saveMonthlyIncome(1500.0)

        analysisScreenViewModel.financialHealthSituation.test {
            assert(awaitItem().percentageOfCompromisedIncome == 0f)
        }
    }

    @Test
    fun `When user has an expense, it should show up on the graph`() = runTest {
        val expenseRepository = FakeExpenseRepository()
        val analysisScreenViewModel = getViewModel(
            expenseRepository = expenseRepository
        )

        expenseRepository.addExpense(0, "Name_1", 10.0, 1234)

        analysisScreenViewModel.graphStates.test {
            assert(awaitItem() is AnalysisGraphStates.AnalysisGraph)
            awaitComplete()
        }
    }
}