package br.com.tick.teira.ui.screens.wallet.usecases

import br.com.tick.teira.ui.datasource.domain.ExpenseCategory
import br.com.tick.teira.ui.datasource.repositories.CategoryRepository
import br.com.tick.teira.ui.datasource.repositories.ExpenseRepository
import br.com.tick.teira.ui.datasource.repositories.LocalDataRepository
import br.com.tick.teira.ui.screens.wallet.models.ExpenseCard
import br.com.tick.teira.ui.screens.wallet.models.ExpenseRisk
import br.com.tick.teira.ui.screens.wallet.states.ExpensesGridStates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class CreateExpensesCards @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val categoryRepository: CategoryRepository,
    private val dataStoreRepository: LocalDataRepository
) {

    companion object {
        private const val CENTER_GRID_EXPENSES = 30
    }

    suspend operator fun invoke(): Flow<ExpensesGridStates> {
        val expensesList = expenseRepository.getExpenses(CENTER_GRID_EXPENSES)
        val monthlyIncome = dataStoreRepository.getMonthlyIncome()

        return expensesList.combine(monthlyIncome) { _expensesList, _monthlyIncome ->

            val expensesCards = _expensesList.map {
                val expenseCategory = categoryRepository.getCategoryById(it.categoryId)
                ExpenseCard(
                    id = it.expenseId,
                    name = it.name,
                    value = it.value,
                    category = ExpenseCategory(expenseCategory.categoryId, expenseCategory.name),
                    risk = ExpenseRisk.getRiskFromValue(
                        monthlyIncome = _monthlyIncome,
                        expenseValue = it.value
                    )
                )
            }

            ExpensesGridStates.of(expensesCards)
        }
    }
}
