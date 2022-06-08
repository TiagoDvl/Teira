package br.com.tick.ui.screens.wallet.usecases

import br.com.tick.sdk.domain.ExpenseRisk
import br.com.tick.sdk.repositories.CategorizedExpenseRepository
import br.com.tick.sdk.repositories.LocalDataRepository
import br.com.tick.ui.screens.wallet.models.ExpenseCard
import br.com.tick.ui.screens.wallet.states.ExpensesGridStates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class CreateExpensesCards @Inject constructor(
    private val categorizedExpenseRepository: CategorizedExpenseRepository,
    private val dataStoreRepository: LocalDataRepository
) {

    companion object {
        private const val CENTER_GRID_EXPENSES = 30
    }

    suspend operator fun invoke(): Flow<ExpensesGridStates> {
        val expensesList = categorizedExpenseRepository.getCategorizedExpenses(CENTER_GRID_EXPENSES)
        val monthlyIncome = dataStoreRepository.getMonthlyIncome()

        return expensesList.combine(monthlyIncome) { _expensesList, _monthlyIncome ->

            val expensesCards = _expensesList.map {
                ExpenseCard(
                    id = it.expenseId,
                    name = it.name,
                    value = it.expenseValue,
                    category = it.category,
                    risk = ExpenseRisk.getRiskFromValue(
                        monthlyIncome = _monthlyIncome.monthlyIncomeValue,
                        expenseValue = it.expenseValue
                    )
                )
            }

            ExpensesGridStates.of(expensesCards)
        }
    }
}
