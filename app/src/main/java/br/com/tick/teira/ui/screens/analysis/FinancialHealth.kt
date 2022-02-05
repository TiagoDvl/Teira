package br.com.tick.teira.ui.screens.analysis

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Slider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.tick.teira.ui.screens.analysis.states.FinancialHealth
import br.com.tick.teira.ui.screens.analysis.viewmodels.AnalysisScreenViewModel
import br.com.tick.teira.ui.theme.spacing

@Composable
fun FinancialHealthComposable(
    modifier: Modifier = Modifier,
    viewModel: AnalysisScreenViewModel = hiltViewModel()
) {
    Column(modifier = modifier.padding(MaterialTheme.spacing.small)) {
        val sliderPosition by remember {
            viewModel.financialHealthSituation
        }.collectAsState(initial = FinancialHealth.Empty)

        Text("Financial Health")
        Slider(
            value = sliderPosition.percentageOfCompromisedIncome / 100,
            onValueChange = { }
        )
    }
}
