package br.com.tick.teira.ui.screens.wallet.usecases

import br.com.tick.teira.ui.datasource.repositories.ExpenseRepository
import javax.inject.Inject

class RemoveExpenseCard @Inject constructor(
    private val expenseRepository: ExpenseRepository
) {

    suspend operator fun invoke(expenseId: Int) {
        expenseRepository.removeExpense(expenseId)
    }
}