package br.com.tick.teira.ui.screens.wallet.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.tick.teira.ui.datasource.repositories.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuickExpenseBarViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    fun saveQuickExpense(name: String, value: Double, category: String, expenseDate: Long) {
        viewModelScope.launch {
            expenseRepository.addExpense(name, value, category, expenseDate)
        }
    }
}
