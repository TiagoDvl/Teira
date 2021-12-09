package br.com.tick.teira.ui.screens.wallet.usecases

import br.com.tick.teira.ui.datasource.repositories.ExpenseRepository
import br.com.tick.teira.ui.datasource.repositories.LocalDataRepository
import br.com.tick.teira.ui.screens.wallet.models.ExpenseCard
import br.com.tick.teira.ui.screens.wallet.models.ExpenseCategory
import br.com.tick.teira.ui.screens.wallet.models.ExpenseRisk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import kotlin.random.Random

class CreateExpensesCards @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val dataStoreRepository: LocalDataRepository
) {

    companion object {
        private const val CENTER_GRID_EXPENSES = 30
    }

    suspend operator fun invoke(): Flow<List<ExpenseCard>> {
        val expensesList = expenseRepository.getExpenses(CENTER_GRID_EXPENSES)
        val monthlyIncome = dataStoreRepository.getMonthlyIncome()

        return expensesList.combine(monthlyIncome) { _expensesList, _monthlyIncome ->
            // Remove this after I properly do a fucking migration
            if (_expensesList.isEmpty()) {
                val categoryFactory = listOf(
                    "Alimentação",
                    "Lazer",
                    "Casa",
                    "Supermercado",
                    "Saúde",
                    "Presentes",
                    "Roupas",
                    "Outros"
                )

                for (i in 0..50) {
                    val category = categoryFactory[Random.nextInt(0, categoryFactory.size - 1)]
                    val value = Random.nextDouble(0.1, 100.0)
                    val aDay = 100000000
                    val date = 1638563028893 - (aDay * i) // Update with epoch of today
                    expenseRepository.addExpense("$i", String.format("%.2f", value), category, date)
                }
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
