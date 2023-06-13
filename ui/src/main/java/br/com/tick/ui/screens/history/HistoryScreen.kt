@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package br.com.tick.ui.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.tick.ui.R
import br.com.tick.ui.screens.history.models.Expense
import br.com.tick.ui.screens.history.states.ExpensesHistoryStates
import br.com.tick.ui.screens.history.viewmodels.HistoryScreenViewModel
import br.com.tick.ui.theme.spacing
import br.com.tick.ui.theme.textStyle

@Composable
fun HistoryScreen(historyScreenViewModel: HistoryScreenViewModel = hiltViewModel()) {
    val chosenMonths = historyScreenViewModel.chosenMonths.collectAsStateWithLifecycle()
    val availableHistoryMonths = historyScreenViewModel.availableHistoryMonths.collectAsStateWithLifecycle()
    val expensesHistoryStates = historyScreenViewModel.expensesHistoryStates.collectAsStateWithLifecycle()

    Column {
        HistoryGraph(chosenMonths.value, availableHistoryMonths.value) {
            historyScreenViewModel.chosenMonths(it)
        }
        ExpensesHistory(expensesHistoryStates.value) {
            historyScreenViewModel.setMonthExpanded(it)
        }
    }
}

@Composable
private fun HistoryGraph(
    chosenMonths: List<String>,
    availableHistoryMonths: List<String>,
    selectedMonths: (List<String>) -> Unit
) {
    HistoryMonthsChips(chosenMonths, availableHistoryMonths, selectedMonths)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HistoryMonthsChips(
    chosenMonths: List<String>,
    availableMonths: List<String>,
    selectedMonths: (List<String>) -> Unit
) {
    val scrollableState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollableState)
    ) {
        availableMonths.forEach {
            FilterChip(
                modifier = Modifier.padding(MaterialTheme.spacing.extraSmall),
                selected = chosenMonths.contains(it),
                label = { Text(text = it) },
                onClick = {
                    val mutable = chosenMonths.toMutableList()
                    if (mutable.contains(it)) {
                        mutable.remove(it)
                    } else {
                        mutable.add(it)
                    }
                    selectedMonths(mutable)
                }
            )
        }
    }
}

@Composable
fun ExpensesHistory(
    expensesHistoryStates: ExpensesHistoryStates,
    onExpandedMonth: (String) -> Unit
) {
    when (expensesHistoryStates) {
        is ExpensesHistoryStates.Empty -> Text(text = stringResource(id = R.string.generic_loading))
        is ExpensesHistoryStates.Content -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.spacing.small)
            ) {
                items(expensesHistoryStates.expenseHistory) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onExpandedMonth(it.month) }
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(MaterialTheme.spacing.small)
                            .height(32.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val monthIcon = if (it.isExpanded) R.drawable.ic_shrink else R.drawable.ic_expand
                        Text(
                            modifier = Modifier.align(Alignment.CenterVertically),
                            text = it.month,
                            style = MaterialTheme.textStyle.h3,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Icon(
                            modifier = Modifier.align(Alignment.CenterVertically),
                            painter = painterResource(id = monthIcon),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    if (it.isExpanded) {
                        it.groupedExpenses.forEach { expense ->
                            GroupedExpensesRow(expense)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GroupedExpensesRow(expense: Expense) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary)
            .padding(MaterialTheme.spacing.extraSmall)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = expense.name,
                maxLines = 1,
                style = MaterialTheme.textStyle.h4extra,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = expense.categoryName,
                maxLines = 1,
                style = MaterialTheme.textStyle.h4,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = expense.date.toString(),
                maxLines = 1,
                style = MaterialTheme.textStyle.h4,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = expense.value.toString(),
                maxLines = 1,
                style = MaterialTheme.textStyle.h4,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}
