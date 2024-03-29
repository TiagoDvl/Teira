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
import androidx.compose.material3.FilterChipDefaults
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
import br.com.tick.ui.core.TeiraEmptyState
import br.com.tick.ui.extensions.getLabelResource
import br.com.tick.ui.extensions.twoDecimalPlacesFormat
import br.com.tick.ui.screens.history.models.GroupedExpense
import br.com.tick.ui.screens.history.states.ExpensesHistoryGraphStates
import br.com.tick.ui.screens.history.states.ExpensesHistoryStates
import br.com.tick.ui.screens.history.viewmodels.HistoryScreenViewModel
import br.com.tick.ui.theme.spacing
import br.com.tick.ui.theme.textStyle
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.style.LocalChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.composed.ComposedChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.composed.plus

@Composable
fun HistoryScreen(historyScreenViewModel: HistoryScreenViewModel = hiltViewModel()) {
    val chosenMonths = historyScreenViewModel.chosenMonths.collectAsStateWithLifecycle()
    val availableHistoryMonths = historyScreenViewModel.availableHistoryMonths.collectAsStateWithLifecycle()
    val expensesHistoryStates = historyScreenViewModel.expensesHistoryStates.collectAsStateWithLifecycle()
    val expensesHistoryGraphStates = historyScreenViewModel.expensesHistoryGraphStates.collectAsStateWithLifecycle()

    Column(modifier = Modifier.padding(MaterialTheme.spacing.small)) {
        HistoryGraph(expensesHistoryGraphStates.value)
        HistoryMonthsChips(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = MaterialTheme.spacing.medium),
            chosenMonths = chosenMonths.value,
            availableMonths = availableHistoryMonths.value
        ) {
            historyScreenViewModel.chosenMonths(it)
        }
        ExpensesHistory(expensesHistoryStates.value) {
            historyScreenViewModel.setMonthExpanded(it)
        }
    }
}

@Composable
private fun HistoryGraph(
    state: ExpensesHistoryGraphStates
) {
    val models: MutableList<ChartEntryModelProducer> = mutableListOf()

    when (state) {
        is ExpensesHistoryGraphStates.Content -> state.monthlyMergedExpenses.forEach {
            models.add(ChartEntryModelProducer(it.mergedValues))
        }

        else -> models.add(ChartEntryModelProducer())
    }

    var composedChartEntryModelProducer = ComposedChartEntryModelProducer<ChartEntryModel>()

    models.ifEmpty { }
    models.forEach { composedChartEntryModelProducer += it }


    val lineChart = lineChart()
    Column {
        Text(
            text = stringResource(id = R.string.history_compare_past_months_title),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.textStyle.h2
        )
        ProvideChartStyle(chartStyle = LocalChartStyle.current) {
            
        }
        Chart(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = MaterialTheme.spacing.medium),
            chart = lineChart,
            chartModelProducer = composedChartEntryModelProducer,
            startAxis = startAxis(),
            bottomAxis = bottomAxis(),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HistoryMonthsChips(
    modifier: Modifier = Modifier,
    chosenMonths: List<String>,
    availableMonths: List<String>,
    selectedMonths: (List<String>) -> Unit
) {
    val scrollableState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollableState)
    ) {
        availableMonths.forEach {
            FilterChip(
                modifier = Modifier.padding(MaterialTheme.spacing.extraSmall),
                selected = chosenMonths.contains(it),
                label = { Text(text = it) },
                colors = FilterChipDefaults.filterChipColors(
                    labelColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.tertiary,
                    selectedContainerColor = MaterialTheme.colorScheme.surface
                ),
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
        is ExpensesHistoryStates.Empty -> TeiraEmptyState(
            modifier = Modifier.fillMaxSize(),
            emptyStateLabel = R.string.history_past_months_expenses_empty_list
        )
        is ExpensesHistoryStates.Content -> {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
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
fun GroupedExpensesRow(groupedExpense: GroupedExpense) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(MaterialTheme.spacing.extraSmall)
    ) {
        val currencyLabel = stringResource(id = groupedExpense.currency.getLabelResource())
        val formattedExpenseValue = groupedExpense.value.twoDecimalPlacesFormat()
        val formattedText = "$currencyLabel$formattedExpenseValue"

        GroupedExpenseCell(modifier = Modifier.weight(1f), text = groupedExpense.name)
        GroupedExpenseCell(modifier = Modifier.weight(1f), text = groupedExpense.categoryName)
        GroupedExpenseCell(modifier = Modifier.weight(1f), text = groupedExpense.date.toString())
        GroupedExpenseCell(modifier = Modifier.weight(1f), text = formattedText)
    }
}

@Composable
private fun GroupedExpenseCell(modifier: Modifier = Modifier, text: String) {
    Box(modifier = modifier) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = text,
            maxLines = 1,
            style = MaterialTheme.textStyle.h3small,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}
