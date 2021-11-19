package br.com.tick.teira.ui.screens.wallet.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.tick.teira.ui.datasource.repositories.ExpenseRepository
import br.com.tick.teira.ui.screens.wallet.states.ExpensesGridStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpensesGridViewModel @Inject constructor(
    expenseRepository: ExpenseRepository
): ViewModel() {

    private val _expensesList = MutableStateFlow<ExpensesGridStates>(ExpensesGridStates.Loading)
    val expensesList = _expensesList.asStateFlow()

    // Try this tomorrow
    /*
    val expensesList = flow<ExpensesGridStates> {
        expenseRepository.getExpenses().collect { ExpensesGridStates.of(it) }
    }
     */
    init {
        viewModelScope.launch {
            expenseRepository.getExpenses().collect {
                _expensesList.value = ExpensesGridStates.of(it)
            }
        }
    }
}