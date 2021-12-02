package br.com.tick.teira.ui.screens.wallet.usecases

import br.com.tick.teira.ui.datasource.repositories.ExpenseRepository
import br.com.tick.teira.ui.datasource.repositories.LocalDataRepository
import br.com.tick.teira.ui.screens.wallet.models.ExpenseCard
import br.com.tick.teira.ui.screens.wallet.models.ExpenseCategory
import br.com.tick.teira.ui.screens.wallet.models.ExpenseRisk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.util.Date
import javax.inject.Inject

class CreateExpensesCards @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val dataStoreRepository: LocalDataRepository
) {

    suspend operator fun invoke(numberOfExpenses: Int): Flow<List<ExpenseCard>> {
        val expensesList = expenseRepository.getExpenses(numberOfExpenses)
        val monthlyIncome = dataStoreRepository.getMonthlyIncome()

        return expensesList.combine(monthlyIncome) { _expensesList, _monthlyIncome ->
            // Remove this after I properly do a fucking migration
            if (_expensesList.isEmpty()) {
                expenseRepository.addExpense("Almoço", "23.5", "Alimentação", Date().time)
                expenseRepository.addExpense("Saidinha", "23.5", "Lazer", Date().time)
                expenseRepository.addExpense("Drogas", "23.5", "Lazer", Date().time)
                expenseRepository.addExpense("Motel", "23.5", "Lazer", Date().time)
                expenseRepository.addExpense("Luz", "23.5", "Casa", Date().time)
                expenseRepository.addExpense("Água", "23.5", "Casa", Date().time)
            }

            _expensesList.map {
                ExpenseCard(
                    id = it.uid,
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
