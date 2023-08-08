package br.com.tick.ui.screens.wallet.usecases

import br.com.tick.sdk.domain.ExpenseRisk
import br.com.tick.sdk.repositories.categorizedexpense.CategorizedExpenseRepository
import br.com.tick.sdk.repositories.user.UserRepository
import br.com.tick.ui.screens.wallet.models.ExpenseCard
import br.com.tick.ui.screens.wallet.states.ExpensesGridStates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class CreateExpensesCards @Inject constructor(
    private val categorizedExpenseRepository: CategorizedExpenseRepository,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(): Flow<ExpensesGridStates> {
        val expensesList = categorizedExpenseRepository.getAccountingCycleExpenses()

        return expensesList.combine(userRepository.getUser()) { _expensesList, _user ->

            val expensesCards = _expensesList.map {
                ExpenseCard(
                    id = it.expenseId,
                    name = it.name,
                    value = it.expenseValue,
                    currency = _user.currency,
                    category = it.category,
                    risk = ExpenseRisk.getRiskFromValue(
                        monthlyIncome = _user.monthlyIncome,
                        expenseValue = it.expenseValue
                    )
                )
            }

            ExpensesGridStates.of(expensesCards)
        }
    }
}
