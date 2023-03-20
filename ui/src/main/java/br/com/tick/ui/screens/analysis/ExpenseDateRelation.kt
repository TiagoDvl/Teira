package br.com.tick.ui.screens.analysis

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.tick.ui.screens.analysis.states.AnalysisGraphStates
import br.com.tick.ui.screens.analysis.viewmodels.AnalysisScreenViewModel
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.horizontal.topAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.entry.entryOf

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

    Chart(
        modifier = modifier,
        chart = lineChart(),
        model = entryModelOf(entries),
        startAxis = startAxis(maxLabelCount = maxNumber),
        bottomAxis = bottomAxis(),
    )
}

@Composable
fun GraphLoading() {
    Text(text = "Loading...")
}
