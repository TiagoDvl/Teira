package br.com.tick.ui.screens.wallet

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.tick.ui.R
import br.com.tick.ui.core.TeiraDatePicker
import br.com.tick.ui.core.TeiraDropdown
import br.com.tick.ui.core.TeiraFilledTonalButton
import br.com.tick.ui.extensions.getLabelResource
import br.com.tick.ui.screens.shared.AddCategoryDialog
import br.com.tick.ui.screens.wallet.viewmodels.QuickExpenseBarViewModel
import br.com.tick.ui.theme.spacing
import br.com.tick.ui.theme.textStyle
import java.time.LocalDate

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun QuickExpense(
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    AnimatedContent(
        modifier = modifier.fillMaxWidth(),
        targetState = isExpanded,
        label = ""
    ) { targetState ->
        if (targetState) {
            ExpandedQuickExpense {
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
    closeExpandedDialog: () -> Unit
) {
    var expenseName by remember { mutableStateOf(TextFieldValue()) }
    var expenseValue by remember { mutableStateOf(TextFieldValue()) }
    var isInvalidValue by remember { mutableStateOf(false) }
    var selectedCategoryId by remember { mutableIntStateOf(-1) }
    var localDateTime by remember { mutableStateOf(LocalDate.now()) }
    var showAddNewCategoryDialog by remember { mutableStateOf(false) }
    val label = stringResource(id = R.string.wallet_quick_expense_select_category)
    var categoryLabel by remember { mutableStateOf(label) }

    val currency by quickExpenseBarViewModel.currency.collectAsStateWithLifecycle()
    val categoriesList by quickExpenseBarViewModel.categories.collectAsStateWithLifecycle()

    if (showAddNewCategoryDialog) {
        AddCategoryDialog(onAddNewCategory = { categoryLabel = it }) {
            showAddNewCategoryDialog = false
        }
    }

    LaunchedEffect(key1 = categoriesList) {
        selectedCategoryId = categoriesList.find { categories ->
            categories.name == categoryLabel
        }?.expenseCategoryId ?: -1
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary)
            .padding(MaterialTheme.spacing.medium)
    ) {
        val quickExpenseTextFieldColors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            focusedBorderColor = MaterialTheme.colorScheme.surface,
            unfocusedBorderColor = MaterialTheme.colorScheme.surface,
            focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSecondary,
            focusedTextColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedTextColor = MaterialTheme.colorScheme.surface,
            errorBorderColor = MaterialTheme.colorScheme.tertiary,
            errorLabelColor = MaterialTheme.colorScheme.onSecondary,
            errorTextColor = MaterialTheme.colorScheme.surface,
            errorTrailingIconColor = MaterialTheme.colorScheme.onSecondary
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = expenseName,
            label = {
                Text(
                    text = stringResource(id = R.string.wallet_quick_expense_name),
                    style = MaterialTheme.textStyle.h2small
                )
            },
            onValueChange = {
                expenseName = it
            },
            colors = quickExpenseTextFieldColors
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = MaterialTheme.spacing.extraSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(0.5f),
                value = expenseValue,
                onValueChange = {
                    expenseValue = it
                    isInvalidValue = false
                    try {
                        expenseValue.text.toDouble()
                    } catch (exception: NumberFormatException) {
                        isInvalidValue = true
                    }
                },
                label = {
                    Text(
                        text = stringResource(id = R.string.wallet_quick_expense_value),
                        style = MaterialTheme.textStyle.h2small
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done
                ),
                prefix = {
                    Text(text = stringResource(id = currency.getLabelResource()))
                },
                trailingIcon = {
                    if (expenseValue.text.isNotEmpty()) {
                        Icon(
                            modifier = Modifier.clickable { expenseValue = TextFieldValue() },
                            painter = painterResource(id = R.drawable.ic_clear),
                            contentDescription = null
                        )
                    }
                },
                colors = quickExpenseTextFieldColors,
                isError = isInvalidValue
            )

            TeiraDropdown(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(top = 7.dp, start = MaterialTheme.spacing.extraSmall),
                label = categoryLabel,
                borderColor = MaterialTheme.colorScheme.onSecondary,
                dropdownItemLabels = categoriesList.map { it.name },
                dropdownItemColors = categoriesList.map { it.color },
                onItemSelected = {
                    selectedCategoryId = categoriesList[it].expenseCategoryId
                    categoryLabel = categoriesList[it].name
                },
                lastItemLabel = stringResource(id = R.string.wallet_quick_expense_add_category),
                onLastItemSelected = {
                    showAddNewCategoryDialog = true
                }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom
        ) {
            TeiraDatePicker(
                modifier = Modifier.weight(0.5f),
                color = MaterialTheme.colorScheme.onPrimary,
                localDate = localDateTime
            ) {
                localDateTime = it
            }
            TeiraFilledTonalButton(
                modifier = Modifier.weight(0.5f),
                text = stringResource(id = R.string.wallet_quick_expense_save),
                onClick = {
                    if (!isInvalidValue) {
                        closeExpandedDialog()
                        val selectedCategory = categoriesList.find { it.expenseCategoryId == selectedCategoryId }

                        if (expenseName.text.isNotEmpty() &&
                            (expenseValue.text.isNotEmpty() && expenseValue.text.toDouble() != 0.0) &&
                            selectedCategory != null
                        ) {
                            quickExpenseBarViewModel.saveQuickExpense(
                                selectedCategory.expenseCategoryId,
                                expenseName.text,
                                expenseValue.text.toDouble(),
                                localDateTime
                            )
                        }
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
        TeiraFilledTonalButton(
            text = stringResource(id = R.string.wallet_add_new_category_button_text),
            onClick = onClick
        )
    }
}
