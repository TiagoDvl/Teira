package br.com.tick.ui.screens.wallet.usecases

import br.com.tick.sdk.repositories.CategorizedExpenseRepository
import javax.inject.Inject

class RemoveExpenseCard @Inject constructor(private val expenseRepository: CategorizedExpenseRepository) {

    suspend operator fun invoke(expenseId: Int) {
        expenseRepository.removeExpense(expenseId)
    }
}
