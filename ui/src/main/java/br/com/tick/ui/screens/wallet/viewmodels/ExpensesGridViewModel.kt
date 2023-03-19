package br.com.tick.ui.screens.wallet.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.tick.sdk.dispatchers.DispatcherProvider
import br.com.tick.ui.screens.wallet.states.ExpensesGridStates
import br.com.tick.ui.screens.wallet.usecases.CreateExpensesCards
import br.com.tick.ui.screens.wallet.usecases.RemoveExpenseCard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpensesGridViewModel @Inject constructor(
    private val createExpensesCards: CreateExpensesCards,
    private val removeExpenseCard: RemoveExpenseCard,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    val getExpensesGridState: Flow<ExpensesGridStates>
        get() = flow {
            createExpensesCards().collect {
                emit(it)
            }
        }.flowOn(dispatcherProvider.io())

    fun removeCard(expenseId: Int) {
        viewModelScope.launch(dispatcherProvider.io()) {
            removeExpenseCard(expenseId)
        }
    }
}
