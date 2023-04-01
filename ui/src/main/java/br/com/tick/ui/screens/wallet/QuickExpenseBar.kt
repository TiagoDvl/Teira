package br.com.tick.ui.screens.wallet

import android.widget.Space
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.tick.R
import br.com.tick.ui.core.TeiraBaseTextField
import br.com.tick.ui.core.TeiraDropdown
import br.com.tick.ui.core.TeiraOutlinedButton
import br.com.tick.ui.screens.wallet.viewmodels.QuickExpenseBarViewModel
import br.com.tick.ui.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun QuickExpense(
    modifier: Modifier = Modifier,
    showAddCategoryDialogState: MutableState<Boolean>
) {
    val isExpanded = remember { mutableStateOf(false) }
    val quickExpenseComposableHeight = remember { mutableStateOf(70.dp) }
    val animatedSize by animateDpAsState(targetValue = quickExpenseComposableHeight.value)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(animatedSize)
    ) {
        if (isExpanded.value) {
            quickExpenseComposableHeight.value = 200.dp
            ExpandedQuickExpense(
                showAddCategoryDialogState = showAddCategoryDialogState
            ) {
                isExpanded.value = isExpanded.value.not()
            }
        } else {
            quickExpenseComposableHeight.value = 80.dp
            ClosedQuickExpense {
                isExpanded.value = isExpanded.value.not()
            }
        }
    }
}

@Composable
fun ExpandedQuickExpense(
    modifier: Modifier = Modifier,
    showAddCategoryDialogState: MutableState<Boolean>,
    quickExpenseBarViewModel: QuickExpenseBarViewModel = hiltViewModel(),
    onClick: () -> Unit
) {
    val expenseName = remember { mutableStateOf("") }
    val expenseValue = remember { mutableStateOf(0.0) }
    val selectedCategoryId = remember { mutableStateOf(0) }
    val localDateTime = LocalDate.now()
    val categoriesList by remember { quickExpenseBarViewModel.categories }.collectAsState(initial = listOf())

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary)
            .padding(MaterialTheme.spacing.medium)
    ) {
        TeiraBaseTextField(
            value = expenseName.value,
            label = stringResource(id = R.string.wallet_quick_expense_name)
        ) {
            expenseName.value = it
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = MaterialTheme.spacing.extraSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TeiraBaseTextField(
                modifier = Modifier.weight(0.5f),
                value = expenseValue.value.toString(),
                label = stringResource(id = R.string.wallet_quick_expense_value),
                keyboardType = KeyboardType.Decimal
            ) {
                expenseValue.value = it.toDouble()
            }
            TeiraDropdown(
                modifier = Modifier.weight(0.5f).padding(start = MaterialTheme.spacing.extraSmall),
                label = stringResource(id = R.string.wallet_quick_expense_select_category),
                borderColor = MaterialTheme.colorScheme.onSecondary,
                dropdownItemLabels = categoriesList,
                onItemSelected = {
                    selectedCategoryId.value = it
                },
                lastItemLabel = stringResource(id = R.string.wallet_quick_expense_add_category),
                onLastItemSelected = {
                    showAddCategoryDialogState.value = true
                }
            )
        }

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Bottom
        ) {
            QuickExpenseDate(
                modifier = Modifier.weight(0.5f),
                date = localDateTime
            )
            TeiraOutlinedButton(
                modifier = Modifier.weight(0.5f),
                text = stringResource(id = R.string.wallet_quick_expense_save),
                onClick = {
                    onClick() // This will cause a recomposition
                    if (expenseName.value.isNotEmpty() &&
                        expenseValue.value != 0.0 &&
                        selectedCategoryId.value > 0
                    ) {
                        quickExpenseBarViewModel.saveQuickExpense(
                            selectedCategoryId.value,
                            expenseName.value,
                            expenseValue.value,
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

@Composable
fun QuickExpenseDate(
    modifier: Modifier = Modifier,
    date: LocalDate
) {
    Box(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.align(Alignment.BottomStart),
            text = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            textDecoration = TextDecoration.Underline,
            style = MaterialTheme.textStyle.h4,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}
