package br.com.tick.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.tick.R
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
            .padding(MaterialTheme.spacing.extraSmall),
        verticalArrangement = Arrangement.Center
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
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.small),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(id = R.string.settings_monthly_income_title),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.textStyle.h2
        )
        TextField(
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            value = monthlyIncomeState.value.toString(),
            onValueChange = onValueChange,
            textStyle = MaterialTheme.textStyle.h2small
        )
        Text(
            text = stringResource(id = R.string.settings_monthly_income_hint),
            fontSize = 12.sp,
            fontWeight = FontWeight.Thin,
            maxLines = 2,
            textAlign = TextAlign.Center,
            style = MaterialTheme.textStyle.h4,
        )
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    SettingField(monthlyIncomeState = MonthlyIncomeStates.Value(2000.0)) {

    }
}
