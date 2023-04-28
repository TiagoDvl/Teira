package br.com.tick.ui.screens.wallet.usecases

import br.com.tick.sdk.domain.CurrencyFormat
import br.com.tick.sdk.domain.ExpenseRisk
import br.com.tick.sdk.repositories.categorizedexpense.CategorizedExpenseRepository
import br.com.tick.sdk.repositories.localdata.LocalDataRepository
import br.com.tick.ui.screens.wallet.models.ExpenseCard
import br.com.tick.ui.screens.wallet.states.ExpensesGridStates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
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
        val currency = dataStoreRepository.getCurrencyFormat().first() ?: CurrencyFormat.REAL

        return expensesList.combine(monthlyIncome) { _expensesList, _monthlyIncome ->

            val expensesCards = _expensesList.map {
                ExpenseCard(
                    id = it.expenseId,
                    name = it.name,
                    value = it.expenseValue,
                    currency = currency,
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
