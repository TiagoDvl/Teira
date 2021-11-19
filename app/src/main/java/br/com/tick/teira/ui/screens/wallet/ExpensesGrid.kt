package br.com.tick.teira.ui.screens.wallet

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.tick.teira.ui.datasource.databases.entities.Expense
import br.com.tick.teira.ui.screens.wallet.states.ExpensesGridStates
import br.com.tick.teira.ui.screens.wallet.viewmodels.ExpensesGridViewModel

@Composable
fun ExpensesGrid(
    modifier: Modifier = Modifier,
    expensesGridViewModel: ExpensesGridViewModel = hiltViewModel()
) {
    val expensesListState by expensesGridViewModel.expensesList.collectAsState()
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
        cells = GridCells.Adaptive(minSize = 128.dp)
    ) {
        items(expensesList) { expense ->
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = expense.name)
                Text(text = expense.value)
                Text(text = expense.category)
            }
        }
    }
}