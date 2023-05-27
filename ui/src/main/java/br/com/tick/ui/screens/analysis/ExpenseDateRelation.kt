package br.com.tick.ui.screens.analysis

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.tick.ui.R
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
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.entry.entryOf

@Composable
fun ExpenseDateRelation(
    modifier: Modifier = Modifier,
    viewModel: AnalysisScreenViewModel = hiltViewModel()
) {
    val analysisGraph by remember { viewModel.graphStates }.collectAsState(AnalysisGraphStates.NoDataAvailable)

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
            var maxNumber = 100
            var entries = listOf(entryOf(0, 0))

            Box(modifier = Modifier.fillMaxSize()) {
                when (val graphStates = analysisGraph) {
                    is AnalysisGraphStates.AnalysisGraph -> {
                        maxNumber = graphStates.expenses.maxOf { it.value.toInt() }
                        entries = graphStates.expenses.map {
                            entryOf(it.key.dayOfMonth, it.value)
                        }
                    }

                    AnalysisGraphStates.NoDataAvailable -> {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = stringResource(id = R.string.generic_no_data_available),
                            style = MaterialTheme.textStyle.h3,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }

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
}
