package br.com.tick.ui.viewmodels

import app.cash.turbine.test
import br.com.tick.utils.FakeDispatcher
import br.com.tick.sdk.repositories.FakeCategorizedExpenseRepository
import br.com.tick.sdk.repositories.FakeUserRepository
import br.com.tick.sdk.repositories.categorizedexpense.CategorizedExpenseRepository
import br.com.tick.sdk.repositories.user.UserRepository
import br.com.tick.ui.screens.analysis.states.AnalysisGraphStates
import br.com.tick.ui.screens.analysis.states.FinancialHealth
import br.com.tick.ui.screens.analysis.usecases.CalculateFinancialHealthSituation
import br.com.tick.ui.screens.analysis.usecases.FetchLastMonthExpenses
import br.com.tick.ui.screens.analysis.usecases.GetMostExpensiveCategories
import br.com.tick.ui.screens.analysis.viewmodels.AnalysisScreenViewModel
import br.com.tick.utils.CoroutineTestRule
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

@ExperimentalCoroutinesApi
class AnalysisScreenViewModelTest {

    @get:Rule
    val testRule = CoroutineTestRule()

    private fun getViewModel(
        categorizedExpenseRepository: CategorizedExpenseRepository = FakeCategorizedExpenseRepository(),
        userRepository: UserRepository = FakeUserRepository()
    ): AnalysisScreenViewModel {
        val fetchLastMonthExpenses = FetchLastMonthExpenses(categorizedExpenseRepository)
        val getMostExpensiveCategories = GetMostExpensiveCategories(categorizedExpenseRepository)
        val calculateFinancialHealthSituation = CalculateFinancialHealthSituation(
            categorizedExpenseRepository,
            userRepository
        )

        return AnalysisScreenViewModel(
            fetchLastMonthExpenses,
            getMostExpensiveCategories,
            calculateFinancialHealthSituation,
            userRepository,
            FakeDispatcher()
        )
    }

    @Test
    fun `When user made an expense, financial health should reflect this change`() = runTest {
        val expenseRepository = FakeCategorizedExpenseRepository()
        val userRepository: UserRepository = FakeUserRepository()

        userRepository.setMonthlyIncome(1500.0)
        expenseRepository.addExpense(
            categoryId = 1,
            name = "Something",
            value = 15.0,
            expenseDate = LocalDate.now(),
            location = null,
            photoUri = null
        )

        val analysisScreenViewModel = getViewModel(
            categorizedExpenseRepository = expenseRepository,
            userRepository = userRepository
        )

        analysisScreenViewModel.financialHealthSituation.test {
            Truth.assertThat(awaitItem()).isInstanceOf(FinancialHealth.Situation::class.java)
        }
    }

    @Test
    fun `When user has no expenses, financial health should have no available data`() = runTest {
        val expenseRepository = FakeCategorizedExpenseRepository()
        val userRepository: UserRepository = FakeUserRepository()

        val analysisScreenViewModel = getViewModel(
            categorizedExpenseRepository = expenseRepository,
            userRepository = userRepository
        )

        analysisScreenViewModel.financialHealthSituation.test {
            Truth.assertThat(awaitItem()).isInstanceOf(FinancialHealth.NoDataAvailable::class.java)
        }
    }

    @Test
    fun `When user has an expense, it should show up on the graph`() = runTest {
        val expenseRepository = FakeCategorizedExpenseRepository()
        val analysisScreenViewModel = getViewModel(
            categorizedExpenseRepository = expenseRepository
        )

        expenseRepository.addExpense(0, "Name_1", 10.0, LocalDate.now())

        analysisScreenViewModel.graphStates.test {
            assert(awaitItem() is AnalysisGraphStates.AnalysisGraph)
            awaitComplete()
        }
    }
}
