package br.com.tick.ui.usecases

import br.com.tick.sdk.repositories.ExpenseRepository
import javax.inject.Inject

class RemoveExpenseCard @Inject constructor(
    private val expenseRepository: br.com.tick.sdk.repositories.ExpenseRepository
) {

    suspend operator fun invoke(expenseId: Int) {
        expenseRepository.removeExpense(expenseId)
    }
}