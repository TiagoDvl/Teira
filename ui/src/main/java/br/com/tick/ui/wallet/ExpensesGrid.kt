package br.com.tick.ui.wallet

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.tick.ui.core.FlipCard
import br.com.tick.ui.theme.spacing
import br.com.tick.ui.wallet.models.ExpenseCard
import br.com.tick.ui.wallet.states.ExpensesGridStates
import br.com.tick.ui.wallet.viewmodels.ExpensesGridViewModel

@Composable
fun ExpensesGrid(
    modifier: Modifier = Modifier,
    expensesGridViewModel: ExpensesGridViewModel = hiltViewModel()
) {
    val expensesListState by remember {
        expensesGridViewModel.getExpensesGridState
    }.collectAsState(ExpensesGridStates.Loading)

    Body(modifier, expensesListState)
}

@Composable
fun Body(modifier: Modifier, expensesListState: ExpensesGridStates) {
    when (expensesListState) {
        ExpensesGridStates.Empty,
        ExpensesGridStates.Error,
        ExpensesGridStates.Loading -> LoadingGrid(modifier = modifier.fillMaxSize())
        is ExpensesGridStates.Success -> BodyGrid(modifier, expensesListState.expensesList)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BodyGrid(
    modifier: Modifier = Modifier,
    expensesList: List<ExpenseCard>
) {
    LazyVerticalGrid(
        modifier = modifier.padding(MaterialTheme.spacing.extraSmall),
        columns = GridCells.Adaptive(minSize = 120.dp)
    ) {
        items(expensesList) { expense ->
            FlipCard(
                expenseCard = expense,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.extraSmall)
            )
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
