package br.com.tick.teira.ui.screens.wallet.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.tick.teira.shared.dispatchers.DispatcherProvider
import br.com.tick.teira.ui.screens.wallet.usecases.CreateExpensesCards
import br.com.tick.teira.ui.screens.wallet.usecases.RemoveExpenseCard
import dagger.hilt.android.lifecycle.HiltViewModel
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

    val getExpensesGridState = flow {
        createExpensesCards().collect {
            emit(it)
        }
    }

    fun removeCard(expenseId: Int) {
        viewModelScope.launch(dispatcherProvider.io()) {
            removeExpenseCard(expenseId)
        }
    }
}
