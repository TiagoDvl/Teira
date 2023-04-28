package br.com.tick.ui.screens.wallet

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.tick.ui.R
import br.com.tick.ui.core.TeiraBaseTextField
import br.com.tick.ui.core.TeiraDropdown
import br.com.tick.ui.core.TeiraOutlinedButton
import br.com.tick.ui.screens.wallet.viewmodels.QuickExpenseBarViewModel
import br.com.tick.ui.theme.spacing
import br.com.tick.ui.theme.textStyle
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun QuickExpense(
    modifier: Modifier = Modifier,
    onAddNewCategoryClicked: () -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }

    AnimatedContent(
        modifier = modifier.fillMaxWidth(),
        targetState = isExpanded
    ) { targetState ->
        if (targetState) {
            ExpandedQuickExpense(onAddNewCategoryClicked = onAddNewCategoryClicked) {
                isExpanded = isExpanded.not()
            }
        } else {
            ClosedQuickExpense {
                isExpanded = isExpanded.not()
            }
        }
    }
}

@Composable
fun ExpandedQuickExpense(
    modifier: Modifier = Modifier,
    quickExpenseBarViewModel: QuickExpenseBarViewModel = hiltViewModel(),
    onAddNewCategoryClicked: () -> Unit,
    onClick: () -> Unit
) {
    var expenseName by remember { mutableStateOf("") }
    var expenseValue by remember { mutableStateOf(0.0) }
    var selectedCategoryId by remember { mutableStateOf(-1) }
    var localDateTime by remember { mutableStateOf(LocalDate.now()) }
    val categoriesList by remember { quickExpenseBarViewModel.categories }.collectAsState(initial = listOf())

    val label = if (selectedCategoryId == -1) {
        stringResource(id = R.string.wallet_quick_expense_select_category)
    } else {
        categoriesList[selectedCategoryId].name
    }

    var categoryLabel by remember { mutableStateOf(label) }

    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary)
            .padding(MaterialTheme.spacing.medium)
    ) {
        TeiraBaseTextField(
            value = expenseName,
            label = stringResource(id = R.string.wallet_quick_expense_name)
        ) {
            expenseName = it
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = MaterialTheme.spacing.extraSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TeiraBaseTextField(
                modifier = Modifier.weight(0.5f),
                value = expenseValue.toString(),
                label = stringResource(id = R.string.wallet_quick_expense_value),
                keyboardType = KeyboardType.NumberPassword
            ) {
                try {
                    expenseValue = it.toDouble()
                } catch (exception: NumberFormatException) {
                    Toast.makeText(context, "This value is not a value", Toast.LENGTH_SHORT).show()
                }
            }

            TeiraDropdown(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(start = MaterialTheme.spacing.extraSmall),
                label = categoryLabel,
                borderColor = MaterialTheme.colorScheme.onSecondary,
                dropdownItemLabels = categoriesList.map { it.name },
                onItemSelected = {
                    selectedCategoryId = it
                    categoryLabel = categoriesList[selectedCategoryId].name
                },
                lastItemLabel = stringResource(id = R.string.wallet_quick_expense_add_category),
                onLastItemSelected = {
                    onAddNewCategoryClicked()
                }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom
        ) {
            QuickExpenseDate(modifier = Modifier.weight(0.5f)) {
                localDateTime = it
            }
            TeiraOutlinedButton(
                modifier = Modifier.weight(0.5f),
                text = stringResource(id = R.string.wallet_quick_expense_save),
                onClick = {
                    onClick() // This will cause a recomposition
                    if (expenseName.isNotEmpty() &&
                        expenseValue != 0.0 &&
                        selectedCategoryId != -1
                    ) {
                        quickExpenseBarViewModel.saveQuickExpense(
                            categoriesList[selectedCategoryId].expenseCategoryId,
                            expenseName,
                            expenseValue,
                            localDateTime
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun ClosedQuickExpense(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary)
            .padding(MaterialTheme.spacing.medium),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.wallet_closed_quick_expense_title),
            style = MaterialTheme.textStyle.h2,
            color = MaterialTheme.colorScheme.onSecondary
        )
        TeiraOutlinedButton(
            text = stringResource(id = R.string.wallet_add_new_category_button_text),
            onClick = onClick
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickExpenseDate(
    modifier: Modifier = Modifier,
    onDateChanged: (LocalDate) -> Unit
) {
    var openDialog by remember { mutableStateOf(false) }
    var localDate by remember { mutableStateOf(LocalDate.now()) }

    if (openDialog) {
        val datePickerState = rememberDatePickerState()
        val confirmEnabled = derivedStateOf { datePickerState.selectedDateMillis != null }

        DatePickerDialog(
            onDismissRequest = { openDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog = false
                        localDate = LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(datePickerState.selectedDateMillis!!),
                            ZoneId.systemDefault()
                        ).toLocalDate()
                        onDateChanged(localDate)
                    },
                    enabled = confirmEnabled.value
                ) {
                    Text(text = stringResource(id = R.string.generic_ok))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { openDialog = false }
                ) {
                    Text(text = stringResource(id = R.string.generic_cancel))
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    titleContentColor = MaterialTheme.colorScheme.tertiary,
                    headlineContentColor = MaterialTheme.colorScheme.tertiary,
                    weekdayContentColor = MaterialTheme.colorScheme.primary,
                    dayContentColor = MaterialTheme.colorScheme.primary,
                    disabledDayContentColor = MaterialTheme.colorScheme.secondary,
                    selectedDayContentColor = MaterialTheme.colorScheme.tertiary,
                    selectedDayContainerColor = MaterialTheme.colorScheme.onSecondary,
                )
            )
        }
    }

    Box(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .clickable { openDialog = true },
            text = localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            textDecoration = TextDecoration.Underline,
            style = MaterialTheme.textStyle.h4,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}
