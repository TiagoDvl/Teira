package br.com.tick.ui.screens.wallet.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.tick.sdk.dispatchers.DispatcherProvider
import br.com.tick.sdk.repositories.user.UserRepository
import br.com.tick.ui.screens.wallet.models.AvailableBalance
import br.com.tick.ui.screens.wallet.states.ExpensesGridStates
import br.com.tick.ui.screens.wallet.usecases.CreateExpensesCards
import br.com.tick.ui.screens.wallet.usecases.GetAvailableBalance
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
    private val getAvailableBalance: GetAvailableBalance,
    private val userRepository: UserRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    val getExpensesGridState: Flow<ExpensesGridStates>
        get() = flow {
            createExpensesCards().collect {
                emit(it)
            }
        }.flowOn(dispatcherProvider.io())

    val availableBalanceState: Flow<AvailableBalance?>
        get() = flow {
            getAvailableBalance().collect {
                emit(it)
            }
        }.flowOn(dispatcherProvider.io())

    fun removeCard(expenseId: Int) {
        viewModelScope.launch(dispatcherProvider.io()) {
            removeExpenseCard(expenseId)
        }
    }

    fun toggleAvailableBalanceVisibility() {
        viewModelScope.launch(dispatcherProvider.io()) {
            userRepository.toggleMonthlyIncomeVisibility()
        }
    }
}
