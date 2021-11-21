package br.com.tick.teira.ui.screens.wallet.usecases

import br.com.tick.teira.ui.datasource.repositories.ExpenseRepository
import br.com.tick.teira.ui.datasource.repositories.LocalDataRepository
import br.com.tick.teira.ui.screens.wallet.models.ExpenseCard
import br.com.tick.teira.ui.screens.wallet.models.ExpenseCategory
import br.com.tick.teira.ui.screens.wallet.models.ExpenseRisk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class CreateExpensesCards @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val dataStoreRepository: LocalDataRepository
) {

    suspend operator fun invoke(numberOfExpenses: Int): Flow<List<ExpenseCard>> {
        val expensesList = expenseRepository.getExpenses(numberOfExpenses)
        val monthlyIncome = dataStoreRepository.getMonthlyIncome()

        return expensesList.combine(monthlyIncome) { _expensesList, _monthlyIncome ->
            _expensesList.map {
                ExpenseCard(
                    name = it.name,
                    value = it.value.toDouble(),
                    category = ExpenseCategory("TODO"),
                    risk = ExpenseRisk.getRiskFromValue(
                        monthlyIncome = _monthlyIncome,
                        expenseValue = it.value.toDouble()
                    )
                )
            }
        }
    }
}