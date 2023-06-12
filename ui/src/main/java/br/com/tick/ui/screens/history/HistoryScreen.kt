@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package br.com.tick.ui.screens.history

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.tick.ui.R
import br.com.tick.ui.screens.history.states.ExpensesHistoryStates
import br.com.tick.ui.screens.history.viewmodels.HistoryScreenViewModel
import br.com.tick.ui.theme.spacing

@Composable
fun HistoryScreen(historyScreenViewModel: HistoryScreenViewModel = hiltViewModel()) {
    var chosenMonths by remember { mutableStateOf(listOf<String>()) }
    val availableHistoryMonths = historyScreenViewModel.availableHistoryMonths.collectAsStateWithLifecycle()
    val expensesHistoryStates = historyScreenViewModel.expensesHistoryStates.collectAsStateWithLifecycle()

    Column {
        HistoryGraph(chosenMonths, availableHistoryMonths.value) {
            chosenMonths = it
            historyScreenViewModel.getExpenses(chosenMonths)
        }
        ExpensesHistory(expensesHistoryStates.value)
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

@Composable
private fun HistoryMonthsChips(
    chosenMonths: List<String>,
    availableMonths: List<String>,
    selectedMonths: (List<String>) -> Unit
) {
    val scrollableState = rememberScrollState()

    Row(modifier = Modifier.fillMaxWidth().horizontalScroll(scrollableState)) {
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
fun ExpensesHistory(expensesHistoryStates: ExpensesHistoryStates) {
    when (expensesHistoryStates) {
        is ExpensesHistoryStates.Empty -> Text(text = stringResource(id = R.string.generic_loading))
        is ExpensesHistoryStates.Content -> {
            Column(modifier = Modifier.fillMaxSize()) {
                expensesHistoryStates.expenseHistory.forEach {
                    Text(text = it.month)
                }
            }
        }
    }
}