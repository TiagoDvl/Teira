package br.com.tick.teira.ui.screens.wallet.viewmodels

import androidx.lifecycle.ViewModel
import br.com.tick.teira.ui.datasource.repositories.ExpenseRepository
import br.com.tick.teira.ui.screens.wallet.states.ExpensesGridStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class ExpensesGridViewModel @Inject constructor(
    expenseRepository: ExpenseRepository
) : ViewModel() {

    // https://proandroiddev.com/better-handling-states-between-viewmodel-and-composable-7ca14af379cb
    val expensesGridState: Flow<ExpensesGridStates> = flow {
        expenseRepository.getExpenses().collect {
            emit(ExpensesGridStates.of(it))
        }
    }
}