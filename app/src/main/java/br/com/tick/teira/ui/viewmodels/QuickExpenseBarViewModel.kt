package br.com.tick.teira.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.tick.teira.ui.datasource.repositories.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuickExpenseBarViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
): ViewModel() {

    init {
        viewModelScope.launch {
            expenseRepository.expenses.collect {
                Log.d("Tiago", it.toString())
            }
        }
    }

    fun saveQuickExpense(name: String, value: String, category: String) {
        viewModelScope.launch {
            expenseRepository.addExpense(name, value, category)
        }
    }
}