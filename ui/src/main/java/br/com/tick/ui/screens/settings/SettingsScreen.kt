package br.com.tick.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.tick.R
import br.com.tick.ui.core.TeiraBaseTextField
import br.com.tick.ui.screens.settings.states.MonthlyIncomeStates
import br.com.tick.ui.screens.settings.viewmodels.SettingsScreenViewModel
import br.com.tick.ui.theme.spacing
import br.com.tick.ui.theme.textStyle

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsScreenViewModel = hiltViewModel()
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(MaterialTheme.spacing.extraSmall)
    ) {
        val state by viewModel.monthlyIncomeFlow.collectAsState(MonthlyIncomeStates.Loading)

        SettingField(monthlyIncomeState = state) {
            viewModel.saveMonthlyIncome(it.toDouble())
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingField(
    modifier: Modifier = Modifier,
    monthlyIncomeState: MonthlyIncomeStates,
    onValueChanged: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.small)
    ) {
        Text(
            text = stringResource(id = R.string.settings_monthly_income_title),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.textStyle.h2
        )
        TeiraBaseTextField(
            modifier = Modifier.padding(top = MaterialTheme.spacing.medium),
            value = monthlyIncomeState.value.toString(),
            onValueChanged = onValueChanged,
            keyboardType = KeyboardType.Decimal,
            label = stringResource(id = R.string.settings_monthly_income_label)
        )
        Text(
            text = stringResource(id = R.string.settings_monthly_income_hint),
            textAlign = TextAlign.Center,
            style = MaterialTheme.textStyle.h4,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    SettingField(monthlyIncomeState = MonthlyIncomeStates.Value(2000.0)) {

    }
}
