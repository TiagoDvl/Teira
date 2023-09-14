package br.com.tick.ui.screens.wallet

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import br.com.tick.ui.NavigationItem
import br.com.tick.ui.R
import br.com.tick.ui.core.TeiraEmptyState
import br.com.tick.ui.core.TeiraErrorState
import br.com.tick.ui.core.QuickExpenseCard
import br.com.tick.ui.core.TeiraLoadingState
import br.com.tick.ui.extensions.getLabelResource
import br.com.tick.ui.screens.wallet.models.AvailableBalance
import br.com.tick.ui.screens.wallet.models.ExpenseCard
import br.com.tick.ui.screens.wallet.states.ExpensesGridStates
import br.com.tick.ui.screens.wallet.viewmodels.ExpensesGridViewModel
import br.com.tick.ui.theme.spacing
import br.com.tick.ui.theme.textStyle

@Composable
fun ExpensesGrid(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    expensesGridViewModel: ExpensesGridViewModel = hiltViewModel()
) {
    val expensesListState by remember {
        expensesGridViewModel.getExpensesGridState
    }.collectAsState(ExpensesGridStates.Loading)

    val availableBalance by remember {
        expensesGridViewModel.availableBalanceState
    }.collectAsState(null)

    AvailableBalanceIndicator(availableBalance) {
        expensesGridViewModel.toggleAvailableBalanceVisibility()
    }
    Body(modifier, navHostController, expensesListState, expensesGridViewModel)
}

@Composable
fun Body(
    modifier: Modifier,
    navHostController: NavHostController,
    expensesListState: ExpensesGridStates,
    expensesGridViewModel: ExpensesGridViewModel
) {
    when (expensesListState) {
        is ExpensesGridStates.Empty -> TeiraEmptyState(modifier = modifier.fillMaxSize())
        is ExpensesGridStates.Error -> TeiraErrorState(modifier.fillMaxSize())
        is ExpensesGridStates.Loading -> TeiraLoadingState(modifier = modifier.fillMaxSize())
        is ExpensesGridStates.Success -> BodyGrid(
            modifier = modifier,
            expensesList = expensesListState.expensesList,
            editExpense = { navHostController.navigate(NavigationItem.EditExpense.with(it)) },
            removeExpense = { expensesGridViewModel.removeCard(it) }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BodyGrid(
    modifier: Modifier = Modifier,
    expensesList: List<ExpenseCard>,
    editExpense: (expenseId: Int) -> Unit,
    removeExpense: (expenseId: Int) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier.padding(),
        columns = GridCells.Fixed(2)
    ) {
        items(expensesList) { expense ->
            QuickExpenseCard(
                expenseCard = expense,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.extraSmall),
                onQuickActionEdit = editExpense,
                onQuickActionDelete = removeExpense
            )
        }
    }
}

@Composable
fun AvailableBalanceIndicator(
    availableBalance: AvailableBalance?,
    onToggleAvailableBalanceVisibility: () -> Unit
) {
    if (availableBalance != null) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            val formattedAmount = if (availableBalance.isVisible) {
                stringResource(
                    id = R.string.wallet_available_balance_title,
                    stringResource(id = availableBalance.currencyFormat.getLabelResource()),
                    availableBalance.amount
                )
            } else {
                stringResource(id = R.string.wallet_available_balance_hidden_title)
            }

            Text(
                modifier = Modifier.clickable { onToggleAvailableBalanceVisibility() },
                text = formattedAmount,
                style = MaterialTheme.textStyle.h3small,
                textDecoration = TextDecoration.Underline
            )
        }
    }
}
