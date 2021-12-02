package br.com.tick.teira.ui.screens.wallet.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.tick.teira.shared.dispatchers.DispatcherProvider
import br.com.tick.teira.ui.screens.wallet.states.ExpensesGridStates
import br.com.tick.teira.ui.screens.wallet.usecases.CreateExpensesCards
import br.com.tick.teira.ui.screens.wallet.usecases.RemoveExpenseCard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpensesGridViewModel @Inject constructor(
    private val createExpensesCards: CreateExpensesCards,
    private val removeExpenseCard: RemoveExpenseCard,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    // https://proandroiddev.com/better-handling-states-between-viewmodel-and-composable-7ca14af379cb
    fun getExpensesGridState(numberOfExpenses: Int): Flow<ExpensesGridStates> = flow {
        createExpensesCards(numberOfExpenses).collect {
            emit(ExpensesGridStates.of(it))
        }
    }

    fun removeCard(expenseId: Int) {
        viewModelScope.launch(dispatcherProvider.io()) {
            removeExpenseCard(expenseId)
        }
    }
}
