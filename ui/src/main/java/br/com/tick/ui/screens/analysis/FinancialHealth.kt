package br.com.tick.ui.screens.analysis

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.tick.sdk.domain.CurrencyFormat
import br.com.tick.ui.R
import br.com.tick.ui.core.TeiraNoAvailableDataState
import br.com.tick.ui.screens.analysis.states.FinancialHealth
import br.com.tick.ui.screens.analysis.viewmodels.AnalysisScreenViewModel
import br.com.tick.ui.theme.spacing
import br.com.tick.ui.theme.textStyle

@Composable
fun FinancialHealthComposable(
    modifier: Modifier = Modifier,
    viewModel: AnalysisScreenViewModel = hiltViewModel()
) {
    val financialHealth by viewModel.financialHealthSituation.collectAsState(initial = FinancialHealth.NoDataAvailable)

    Column(modifier = modifier.padding(MaterialTheme.spacing.small)) {
        Text(
            text = stringResource(id = R.string.analysis_financial_health_title),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.textStyle.h2
        )
    }
    when (val state = financialHealth) {
        is FinancialHealth.NoDataAvailable -> TeiraNoAvailableDataState(modifier = modifier)
        is FinancialHealth.Situation -> FinancialHealthBody(modifier, state)
    }
}

@Composable
fun FinancialHealthBody(
    modifier: Modifier = Modifier,
    state: FinancialHealth.Situation
) {
    val indicativeText = when (state) {
        is FinancialHealth.Situation.Safe -> stringResource(id = R.string.analysis_financial_health_safe)
        is FinancialHealth.Situation.Caution -> stringResource(id = R.string.analysis_financial_health_caution)
        is FinancialHealth.Situation.Dangerous -> stringResource(id = R.string.analysis_financial_health_dangerous)
    }

    val informationText = stringResource(
        id = R.string.analysis_financial_health_information,
        state.percentageOfCompromisedIncome,
        indicativeText
    )


    Column(modifier = modifier) {
        Slider(
            modifier = Modifier.padding(top = MaterialTheme.spacing.medium),
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.tertiary,
                inactiveTrackColor = MaterialTheme.colorScheme.tertiary
            ),
            value = state.percentageOfCompromisedIncome / 100,
            onValueChange = { }
        )
        Text(
            modifier = Modifier.align(Alignment.End),
            text = informationText,
            color = MaterialTheme.colorScheme.tertiary,
            style = MaterialTheme.textStyle.h4extra
        )
    }
}
