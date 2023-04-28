package br.com.tick.ui.screens.wallet

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.tick.ui.core.QuickExpenseCard
import br.com.tick.ui.screens.wallet.models.ExpenseCard
import br.com.tick.ui.screens.wallet.states.ExpensesGridStates
import br.com.tick.ui.screens.wallet.viewmodels.ExpensesGridViewModel
import br.com.tick.ui.theme.spacing

@Composable
fun ExpensesGrid(
    modifier: Modifier = Modifier,
    expensesGridViewModel: ExpensesGridViewModel = hiltViewModel()
) {
    val expensesListState by remember {
        expensesGridViewModel.getExpensesGridState
    }.collectAsState(ExpensesGridStates.Loading)

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
            Text(text = "Loading...")
        }
    }
}
