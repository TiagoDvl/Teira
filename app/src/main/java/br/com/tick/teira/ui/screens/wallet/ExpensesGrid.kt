package br.com.tick.teira.ui.screens.wallet

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
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
import br.com.tick.teira.ui.screens.elements.FlipCard
import br.com.tick.teira.ui.screens.wallet.models.ExpenseCard
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
        ExpensesGridStates.Loading -> LoadingGrid(modifier = modifier.fillMaxSize())
        is ExpensesGridStates.Success -> BodyGrid(modifier, pleaseFixMe.expensesList)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BodyGrid(
    modifier: Modifier = Modifier,
    expensesList: List<ExpenseCard>
) {
    LazyVerticalGrid(
        modifier = modifier.padding(4.dp),
        cells = GridCells.Adaptive(minSize = 128.dp)
    ) {
        items(expensesList) { expense ->
            FlipCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
                back = {
                    Column(
                        modifier = Modifier.height(120.dp).padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = expense.name)
                        Text(text = expense.value.toString())
                        Text(text = expense.category.name)
                        Text(text = expense.risk.name)
                    }
                },
                front = {
                    Column(
                        modifier = Modifier.height(120.dp).padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = expense.name)
                        Text(text = expense.value.toString())
                        Text(text = expense.category.name)
                        Text(text = expense.risk.name)
                    }
                }
            )
        }
    }
}

@Composable
fun LoadingGrid(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(4.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Loading...")
        }

    }
}
