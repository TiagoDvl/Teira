package br.com.tick.teira.ui.screens.wallet

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.tick.teira.ui.datasource.databases.entities.Expense
import br.com.tick.teira.ui.screens.wallet.states.ExpensesGridStates
import br.com.tick.teira.ui.screens.wallet.viewmodels.ExpensesGridViewModel

@Composable
fun ExpensesGrid(
    modifier: Modifier = Modifier,
    numberOfExpensesShown: Int,
    expensesGridViewModel: ExpensesGridViewModel = hiltViewModel()
) {
    val expensesListState by remember(expensesGridViewModel) {
        expensesGridViewModel.getExpensesGridState(numberOfExpensesShown)
    }.collectAsState(ExpensesGridStates.Loading)

    when (val pleaseFixMe = expensesListState) {
        ExpensesGridStates.Empty,
        ExpensesGridStates.Error,
        ExpensesGridStates.Loading -> Text(text = "Loading")
        is ExpensesGridStates.Success -> BodyGrid(modifier, pleaseFixMe.expensesList)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BodyGrid(
    modifier: Modifier = Modifier,
    expensesList: List<Expense>
) {
    LazyVerticalGrid(
        modifier = modifier.padding(4.dp),
        cells = GridCells.Adaptive(minSize = 128.dp)
    ) {
        items(expensesList) { expense ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
                backgroundColor = MaterialTheme.colorScheme.onSecondary,
                elevation = 2.dp
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(text = expense.name)
                    Text(text = expense.value)
                    Text(text = expense.category)
                }

            }
        }
    }
}
