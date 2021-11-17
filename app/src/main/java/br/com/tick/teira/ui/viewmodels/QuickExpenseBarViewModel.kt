package br.com.tick.teira.ui.viewmodels

import androidx.lifecycle.ViewModel
import br.com.tick.teira.ui.datasource.repositories.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuickExpenseBarViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
): ViewModel() {

    fun saveQuickExpense(name: String, value: String, category: String) {
        expenseRepository.addExpense(name, value, category)
    }
}