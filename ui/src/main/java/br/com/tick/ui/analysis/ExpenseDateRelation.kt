package br.com.tick.ui.analysis

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.tick.ui.analysis.states.AnalysisGraphStates
import br.com.tick.ui.analysis.viewmodels.AnalysisScreenViewModel
import br.com.tick.ui.theme.spacing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.Locale

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
    AndroidView(
        modifier = modifier.padding(MaterialTheme.spacing.large),
        factory = { context ->
            val entries = analysisGraph.expenses.map {
                Entry(
                    it.date.toFloat(),
                    it.value.toFloat()
                )
            }
            val dataSet = LineDataSet(entries, "Test Label")
            val lineData = LineData(dataSet)

            LineChart(context).apply {
                xAxis.valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return try {
                            SimpleDateFormat("dd/MM", Locale.US).format(value)
                        } catch (exception: Exception) {
                            ""
                        }
                    }
                }
                data = lineData
                invalidate()
            }
        },
        update = {}
    )
}

@Composable
fun GraphLoading() {
    Text(text = "Loading...")
}
