package br.com.tick.ui.screens.analysis

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.tick.R
import br.com.tick.ui.screens.analysis.states.AnalysisGraphStates
import br.com.tick.ui.screens.analysis.viewmodels.AnalysisScreenViewModel
import br.com.tick.ui.theme.spacing
import br.com.tick.ui.theme.textStyle
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.m3.style.m3ChartStyle
import com.patrykandpatrick.vico.compose.style.LocalChartStyle
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.entry.entryOf
import java.time.LocalDate

@Composable
fun ExpenseDateRelation(
    modifier: Modifier = Modifier,
    viewModel: AnalysisScreenViewModel = hiltViewModel()
) {
    val analysisGraph by remember { viewModel.graphStates }.collectAsState(AnalysisGraphStates.Loading)

    when (analysisGraph) {
        is AnalysisGraphStates.AnalysisGraph -> ExpenseGraph(
            modifier = modifier,
            analysisGraph = analysisGraph as AnalysisGraphStates.AnalysisGraph
        )
        AnalysisGraphStates.Loading -> GraphLoading()
    }
}

@Composable
fun ExpenseGraph(
    modifier: Modifier = Modifier,
    analysisGraph: AnalysisGraphStates.AnalysisGraph
) {
    val maxNumber = analysisGraph.expenses.maxOf { it.value.toInt() }
    val entries = analysisGraph.expenses.map {
        entryOf(it.key.dayOfMonth.toFloat(), it.value)
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = R.string.analysis_graph_title),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.textStyle.h2
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(MaterialTheme.spacing.medium)
        )
        val chartStyle = m3ChartStyle(
            axisLabelColor = MaterialTheme.colorScheme.primary,
            axisLineColor = MaterialTheme.colorScheme.tertiary

        )
        CompositionLocalProvider(LocalChartStyle provides chartStyle) {
            Chart(
                modifier = modifier,
                chart = lineChart(),
                model = entryModelOf(entries),
                startAxis = startAxis(maxLabelCount = maxNumber),
                bottomAxis = bottomAxis()
            )
        }
    }
}

@Composable
fun GraphLoading() {
    Text(
        modifier = Modifier.fillMaxSize(),
        text = stringResource(id = R.string.generic_loading),
        style = MaterialTheme.textStyle.h4
    )
}

@Preview
@Composable
fun GraphLoadingPreview() {
    GraphLoading()
}

@Preview
@Composable
fun ExpenseGraphPreview() {
    ExpenseGraph(analysisGraph = AnalysisGraphStates.AnalysisGraph(mapOf(LocalDate.now() to 200.0)))
}
