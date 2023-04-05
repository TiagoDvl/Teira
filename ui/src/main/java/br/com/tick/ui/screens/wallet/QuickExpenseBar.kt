package br.com.tick.ui.screens.wallet

import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import br.com.tick.ui.theme.spacing
import br.com.tick.ui.theme.textStyle
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
    var expenseName by remember { mutableStateOf("") }
    var expenseValue by remember { mutableStateOf(0.0) }
    var selectedCategoryId by remember { mutableStateOf(-1) }
    val localDateTime = LocalDate.now()
    val categoriesList by remember { quickExpenseBarViewModel.categories }.collectAsState(initial = listOf())

    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
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
            val label = if (selectedCategoryId == -1) stringResource(id = R.string.wallet_quick_expense_select_category) else categoriesList[selectedCategoryId]
            TeiraDropdown(
                modifier = Modifier.weight(0.5f).padding(start = MaterialTheme.spacing.extraSmall),
                label = label,
                borderColor = MaterialTheme.colorScheme.onSecondary,
                dropdownItemLabels = categoriesList,
                onItemSelected = {
                    selectedCategoryId = it
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
                    if (expenseName.isNotEmpty() &&
                        expenseValue != 0.0 &&
                        selectedCategoryId > 0
                    ) {
                        quickExpenseBarViewModel.saveQuickExpense(
                            selectedCategoryId,
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
