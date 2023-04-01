package br.com.tick.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.tick.R
import br.com.tick.sdk.domain.NotificationPeriodicity
import br.com.tick.ui.core.TeiraBaseTextField
import br.com.tick.ui.core.TeiraDropdown
import br.com.tick.ui.screens.settings.states.MonthlyIncomeStates
import br.com.tick.ui.screens.settings.states.SettingsNotificationPeriodicityStates
import br.com.tick.ui.screens.settings.states.getNotificationPeriodicityLabel
import br.com.tick.ui.screens.settings.viewmodels.SettingsScreenViewModel
import br.com.tick.ui.theme.spacing
import br.com.tick.ui.theme.textStyle

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsScreenViewModel = hiltViewModel(),
    onPeriodicNotificationStateChanged: (NotificationPeriodicity) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(MaterialTheme.spacing.extraSmall)
    ) {
        val monthlyIncome by viewModel.monthlyIncomeFlow.collectAsState(MonthlyIncomeStates.Loading)
        val notificationPeriodicity by viewModel.notificationPeriodicity.collectAsState(
            SettingsNotificationPeriodicityStates.Loading
        )

        SettingField(monthlyIncomeState = monthlyIncome) {
            viewModel.saveMonthlyIncome(it.toDouble())
        }
        NotificationsSetting(notificationPeriodicity = notificationPeriodicity) {
            onPeriodicNotificationStateChanged(it)
            viewModel.setNotificationPeriodicity(it)
        }
    }
}

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
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.settings_monthly_income_hint),
            textAlign = TextAlign.Center,
            style = MaterialTheme.textStyle.h4,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}

@Composable
fun NotificationsSetting(
    modifier: Modifier = Modifier,
    notificationPeriodicity: SettingsNotificationPeriodicityStates,
    onNotificationPeriodicitySelected: (NotificationPeriodicity) -> Unit
) {

    val context = LocalContext.current

    // This naming strategy is kinda bad. Rethink it later please.
    val dropdownItemLabels = NotificationPeriodicity.values().map {
        stringResource(
            when (it) {
                NotificationPeriodicity.DAILY -> R.string.settings_notifications_periodicity_daily
                NotificationPeriodicity.WEEKLY -> R.string.settings_notifications_periodicity_weekly
                NotificationPeriodicity.CANCELED -> R.string.settings_notifications_periodicity_canceled
            }
        )
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.small)
    ) {
        Text(
            text = stringResource(id = R.string.settings_notifications_title),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.textStyle.h2
        )
        Row(modifier = Modifier.padding(top = MaterialTheme.spacing.small)) {
            Text(
                modifier = Modifier.weight(0.6f).align(Alignment.CenterVertically),
                text = stringResource(id = R.string.settings_notifications_explanation),
                textAlign = TextAlign.Start,
                style = MaterialTheme.textStyle.h4,
                color = MaterialTheme.colorScheme.tertiary
            )
            TeiraDropdown(
                modifier = Modifier.weight(0.4f).padding(start = MaterialTheme.spacing.extraSmall),
                label = notificationPeriodicity.getNotificationPeriodicityLabel(context),
                borderColor = MaterialTheme.colorScheme.primary,
                dropdownItemLabels = dropdownItemLabels,
                onItemSelected = {
                    onNotificationPeriodicitySelected(NotificationPeriodicity.values()[it])
                }
            )
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    SettingField(monthlyIncomeState = MonthlyIncomeStates.Value(2000.0)) {

    }
}
