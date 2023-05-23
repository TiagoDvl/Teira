package br.com.tick.ui.screens.wallet

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
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
import br.com.tick.ui.R
import br.com.tick.ui.core.QuickExpenseCard
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
    Body(modifier, expensesListState, expensesGridViewModel)
}

@Composable
fun Body(
    modifier: Modifier,
    expensesListState: ExpensesGridStates,
    expensesGridViewModel: ExpensesGridViewModel
) {
    when (expensesListState) {
        ExpensesGridStates.Empty,
        ExpensesGridStates.Error,
        ExpensesGridStates.Loading -> LoadingGrid(modifier = modifier.fillMaxSize())

        is ExpensesGridStates.Success -> BodyGrid(modifier, expensesListState.expensesList, expensesGridViewModel)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BodyGrid(
    modifier: Modifier = Modifier,
    expensesList: List<ExpenseCard>,
    expensesGridViewModel: ExpensesGridViewModel
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
                    .padding(MaterialTheme.spacing.extraSmall)
            ) { expenseId ->
                expensesGridViewModel.removeCard(expenseId)
            }
        }
    }
}

@Composable
fun LoadingGrid(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(MaterialTheme.spacing.extraSmall),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
            Text(text = stringResource(id = R.string.generic_loading))
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
