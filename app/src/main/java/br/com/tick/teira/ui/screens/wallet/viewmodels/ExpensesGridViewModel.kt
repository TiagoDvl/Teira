package br.com.tick.teira.ui.screens.wallet.viewmodels

import androidx.lifecycle.ViewModel
import br.com.tick.teira.ui.screens.wallet.states.ExpensesGridStates
import br.com.tick.teira.ui.screens.wallet.usecases.CreateExpensesCards
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class ExpensesGridViewModel @Inject constructor(
    val createExpensesCards: CreateExpensesCards
) : ViewModel() {

    // https://proandroiddev.com/better-handling-states-between-viewmodel-and-composable-7ca14af379cb
    fun getExpensesGridState(numberOfExpenses: Int): Flow<ExpensesGridStates> = flow {
        createExpensesCards(numberOfExpenses).collect {
            emit(ExpensesGridStates.of(it))
        }
    }
}
