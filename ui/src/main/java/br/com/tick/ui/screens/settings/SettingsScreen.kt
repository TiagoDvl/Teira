package br.com.tick.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.tick.sdk.domain.AccountingDate
import br.com.tick.sdk.domain.CurrencyFormat
import br.com.tick.sdk.domain.ExpenseCategory
import br.com.tick.sdk.domain.NotificationPeriodicity
import br.com.tick.ui.R
import br.com.tick.ui.core.TeiraDropdown
import br.com.tick.ui.extensions.getLabelResource
import br.com.tick.ui.extensions.twoDecimalPlacesFormat
import br.com.tick.ui.screens.settings.states.MonthlyIncomeStates
import br.com.tick.ui.screens.settings.states.SettingsAccountingDateStates
import br.com.tick.ui.screens.settings.states.SettingsCurrencyFormatStates
import br.com.tick.ui.screens.settings.states.SettingsNotificationPeriodicityStates
import br.com.tick.ui.screens.settings.states.getNotificationPeriodicityLabel
import br.com.tick.ui.screens.settings.viewmodels.SettingsScreenViewModel
import br.com.tick.ui.screens.shared.EditCategoryDialog
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
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
            .padding(MaterialTheme.spacing.extraSmall)
    ) {
        val monthlyIncome by viewModel.monthlyIncomeFlow.collectAsState(MonthlyIncomeStates.Loading)
        val notificationPeriodicity by viewModel.notificationPeriodicity.collectAsState(
            SettingsNotificationPeriodicityStates.Loading
        )
        val currencyFormat by viewModel.currencyFormat.collectAsState(initial = SettingsCurrencyFormatStates.Loading)
        val accountingDateState by viewModel.startDate.collectAsState(initial = SettingsAccountingDateStates.Loading)
        val currency by viewModel.currency.collectAsState(initial = CurrencyFormat.REAL)
        val categories by viewModel.categories.collectAsStateWithLifecycle()

        monthlyIncome.also {
            if (it is MonthlyIncomeStates.Value) {
                MonthlyIncomeSetting(
                    monthlyIncomeState = it,
                    currencyFormat = currency,
                    toggleMonthlyIncome = {
                        viewModel.toggleMonthlyIncomeVisibility()
                    }
                ) { value ->
                    viewModel.saveMonthlyIncome(value)
                }
            }
        }

        NotificationsSetting(notificationPeriodicity = notificationPeriodicity) {
            onPeriodicNotificationStateChanged(it)
            viewModel.setNotificationPeriodicity(it)
        }
        CurrencyFormatSetting(settingsCurrencyFormatStates = currencyFormat) {
            viewModel.setCurrencyFormat(it)
        }
        AccountingDateSetting(settingsAccountingDateStates = accountingDateState) {
            viewModel.setAccountingDate(it)
        }
        EditCategorySetting(categories = categories)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MonthlyIncomeSetting(
    modifier: Modifier = Modifier,
    monthlyIncomeState: MonthlyIncomeStates.Value,
    currencyFormat: CurrencyFormat,
    toggleMonthlyIncome: () -> Unit,
    saveIncome: (Double) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var monthlyIncomeText by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue()) }
    var errorState by remember { mutableStateOf(false) }
    var showTrailingIcons by remember { mutableStateOf(false) }
    var showConfirmationDialog by remember { mutableStateOf(false) }

    if (showConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmationDialog = false },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_priority_high),
                    contentDescription = null
                )
            },
            confirmButton = {
                Text(
                    modifier = Modifier.clickable {
                        showConfirmationDialog = false

                        errorState = try {
                            val parsedIncome = monthlyIncomeText.text.toDouble()
                            saveIncome(parsedIncome)
                            monthlyIncomeText = TextFieldValue()
                            focusManager.clearFocus()
                            false
                        } catch (exception: NumberFormatException) {
                            true
                        }
                    },
                    text = stringResource(id = R.string.generic_ok).uppercase()
                )
            },
            dismissButton = {
                Text(
                    modifier = Modifier.clickable {
                        showConfirmationDialog = false
                    },
                    text = stringResource(id = R.string.generic_cancel).uppercase()
                )
            },
            title = {
                Text(text = stringResource(id = R.string.settings_monthly_income_dialog_confirmation_title))
            },
            text = {
                Text(text = stringResource(id = R.string.settings_monthly_income_dialog_confirmation_text))
            },
            containerColor = MaterialTheme.colorScheme.onSecondary,
            titleContentColor = MaterialTheme.colorScheme.tertiary,
            textContentColor = MaterialTheme.colorScheme.primary,
            iconContentColor = MaterialTheme.colorScheme.onTertiary
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.small)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val currencyLabel = stringResource(id = currencyFormat.getLabelResource())
            val formattedMonthlyIncome = monthlyIncomeState.value.twoDecimalPlacesFormat()

            val label = if (monthlyIncomeState.showMonthlyIncome) {
                val incomeLabel = stringResource(id = R.string.settings_current_income_label)
                "$incomeLabel $currencyLabel$formattedMonthlyIncome"
            } else {
                stringResource(id = R.string.settings_current_income_hidden_label)
            }

            Text(
                text = stringResource(id = R.string.settings_monthly_income_title),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.textStyle.h2
            )
            Text(
                modifier = Modifier
                    .align(Bottom)
                    .clickable { toggleMonthlyIncome() }
                    .padding(end = MaterialTheme.spacing.small),
                text = label,
                style = MaterialTheme.textStyle.h3small,
                textDecoration = TextDecoration.Underline
            )
        }

        OutlinedTextField(
            modifier = Modifier
                .padding(top = MaterialTheme.spacing.medium)
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    showTrailingIcons = focusState.isFocused
                },
            value = monthlyIncomeText,
            prefix = { Text(text = stringResource(id = currencyFormat.getLabelResource())) },
            onValueChange = {
                monthlyIncomeText = it
                errorState = false
            },
            textStyle = MaterialTheme.textStyle.h2,
            label = {
                Text(
                    text = stringResource(id = R.string.settings_monthly_income_label),
                    style = MaterialTheme.textStyle.h2small
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    try {
                        val newIncomeValue = monthlyIncomeText.text.toDouble()

                        keyboardController?.hide()
                        focusManager.clearFocus()

                        if (newIncomeValue != monthlyIncomeState.value && monthlyIncomeText.text.isNotEmpty()) {
                            showConfirmationDialog = true
                        }
                    } catch (exception: NumberFormatException) {
                        errorState = true
                    }
                }
            ),
            trailingIcon = {
                if (showTrailingIcons) {
                    Row {
                        if (monthlyIncomeText.text.isNotEmpty()) {
                            IconButton(onClick = { monthlyIncomeText = TextFieldValue() }) {
                                Icon(painter = painterResource(id = R.drawable.ic_clear), contentDescription = null)
                            }
                        }
                    }
                }
            },
            isError = errorState,
            supportingText = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.settings_monthly_income_hint),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.textStyle.h4,
                    color = MaterialTheme.colorScheme.tertiary
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                focusedLabelColor = MaterialTheme.colorScheme.tertiary,
                unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                errorBorderColor = MaterialTheme.colorScheme.tertiary,
                focusedTextColor = MaterialTheme.colorScheme.onTertiary
            )
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
                modifier = Modifier
                    .weight(0.6f)
                    .align(Alignment.CenterVertically),
                text = stringResource(id = R.string.settings_notifications_explanation),
                textAlign = TextAlign.Start,
                style = MaterialTheme.textStyle.h4,
                color = MaterialTheme.colorScheme.tertiary
            )
            TeiraDropdown(
                modifier = Modifier
                    .weight(0.4f)
                    .padding(start = MaterialTheme.spacing.extraSmall),
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

@Composable
fun CurrencyFormatSetting(
    modifier: Modifier = Modifier,
    settingsCurrencyFormatStates: SettingsCurrencyFormatStates,
    onCurrencyFormatSelected: (CurrencyFormat) -> Unit
) {
    val dropdownItemLabels = CurrencyFormat.values().map {
        stringResource(it.getLabelResource())
    }

    val labelResourceId = when (settingsCurrencyFormatStates) {
        is SettingsCurrencyFormatStates.Loading -> R.string.generic_loading
        is SettingsCurrencyFormatStates.Content -> settingsCurrencyFormatStates.currencyFormat.getLabelResource()
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.small)
    ) {
        Text(
            text = stringResource(id = R.string.settings_currency_title),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.textStyle.h2
        )
        Row(modifier = Modifier.padding(top = MaterialTheme.spacing.small)) {
            Text(
                modifier = Modifier
                    .weight(0.6f)
                    .align(Alignment.CenterVertically),
                text = stringResource(id = R.string.settings_currency_explanation),
                textAlign = TextAlign.Start,
                style = MaterialTheme.textStyle.h4,
                color = MaterialTheme.colorScheme.tertiary
            )
            TeiraDropdown(
                modifier = Modifier
                    .weight(0.4f)
                    .padding(start = MaterialTheme.spacing.extraSmall),
                label = stringResource(id = labelResourceId),
                borderColor = MaterialTheme.colorScheme.primary,
                dropdownItemLabels = dropdownItemLabels,
                onItemSelected = {
                    onCurrencyFormatSelected(CurrencyFormat.values()[it])
                }
            )
        }
    }
}

@Composable
fun AccountingDateSetting(
    modifier: Modifier = Modifier,
    settingsAccountingDateStates: SettingsAccountingDateStates,
    onStartDateSelected: (AccountingDate) -> Unit
) {
    val dropdownItemLabels = AccountingDate.values().map {
        stringResource(it.getLabelResource())
    }
    val labelResourceId = when (settingsAccountingDateStates) {
        is SettingsAccountingDateStates.Loading -> R.string.generic_loading
        is SettingsAccountingDateStates.Content -> settingsAccountingDateStates.accountingDate.getLabelResource()
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.small)
    ) {
        Text(
            text = stringResource(id = R.string.settings_accounting_date_title),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.textStyle.h2
        )
        Row(modifier = Modifier.padding(top = MaterialTheme.spacing.small)) {
            Text(
                modifier = Modifier
                    .weight(0.6f)
                    .align(Alignment.CenterVertically),
                text = stringResource(id = R.string.settings_accounting_date_explanation),
                textAlign = TextAlign.Start,
                style = MaterialTheme.textStyle.h4,
                color = MaterialTheme.colorScheme.tertiary
            )
            TeiraDropdown(
                modifier = Modifier
                    .weight(0.4f)
                    .padding(start = MaterialTheme.spacing.extraSmall),
                label = stringResource(id = labelResourceId),
                borderColor = MaterialTheme.colorScheme.primary,
                dropdownItemLabels = dropdownItemLabels,
                onItemSelected = {
                    onStartDateSelected(AccountingDate.values()[it])
                }
            )
        }
    }
}

@Composable
fun EditCategorySetting(
    modifier: Modifier = Modifier,
    categories: List<ExpenseCategory>
) {
    var showColorPickerDialog by remember { mutableStateOf(Pair(false, -1)) }

    if (showColorPickerDialog.first) {
        EditCategoryDialog(expenseCategory = categories[showColorPickerDialog.second]) {
            showColorPickerDialog = false to -1
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.small)
    ) {
        Text(
            text = stringResource(id = R.string.settings_edit_category_title),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.textStyle.h2
        )
        Row(modifier = Modifier.padding(top = MaterialTheme.spacing.small)) {
            Text(
                modifier = Modifier
                    .weight(0.6f)
                    .align(Alignment.CenterVertically),
                text = stringResource(id = R.string.settings_edit_category_explanation),
                textAlign = TextAlign.Start,
                style = MaterialTheme.textStyle.h4,
                color = MaterialTheme.colorScheme.tertiary
            )
            TeiraDropdown(
                modifier = Modifier
                    .weight(0.4f)
                    .padding(start = MaterialTheme.spacing.extraSmall),
                label = stringResource(id = R.string.settings_edit_category_label),
                borderColor = MaterialTheme.colorScheme.primary,
                dropdownItemLabels = categories.map { it.name },
                onItemSelected = {
                    showColorPickerDialog = true to it
                }
            )
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    MonthlyIncomeSetting(
        monthlyIncomeState = MonthlyIncomeStates.Value(2000.0, true),
        currencyFormat = CurrencyFormat.REAL,
        toggleMonthlyIncome = {}
    ) {

    }
}
