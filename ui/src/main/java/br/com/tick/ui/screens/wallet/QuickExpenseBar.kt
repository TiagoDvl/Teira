package br.com.tick.ui.screens.wallet

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.tick.R
import br.com.tick.ui.core.TeiraBaseTextField
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
    val quickExpenseComposableHeight = remember { mutableStateOf(80.dp) }
    val animatedSize by animateDpAsState(
        targetValue = quickExpenseComposableHeight.value,
        tween(
            durationMillis = 500
        )
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.extraSmall)
            .background(Purple80)
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
    val expenseValue = remember { mutableStateOf("") }
    val selectedCategoryId = remember { mutableStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(MaterialTheme.spacing.medium),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
        ) {
            TeiraBaseTextField(
                modifier = Modifier.width(200.dp),
                color = Pink40,
                value = expenseName.value,
                label = "Expense Name"
            ) {
                expenseName.value = it
            }
            TeiraBaseTextField(
                modifier = Modifier.width(200.dp),
                color = Pink40,
                value = expenseValue.value,
                label = "Expense Value"
            ) {
                expenseValue.value = it
            }
        }
        Row {
            CategoryDropdown {
                selectedCategoryId.value = it
            }
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
            FilledTonalButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                colors = ButtonDefaults.textButtonColors(containerColor = Purple40),
                onClick = { showAddCategoryDialogState.value = true }
            ) {
                Image(painter = painterResource(id = R.drawable.ic_add), contentDescription = "Add Category")
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            val localDateTime = LocalDate.now()

            QuickExpenseDate(
                modifier = Modifier.weight(0.5f),
                date = localDateTime
            )
            TeiraOutlinedButton(
                modifier = Modifier.weight(0.5f),
                text = "Save",
                onClick = {
                    onClick() // This will cause a recomposition
                    if (expenseName.value.isNotEmpty() &&
                        expenseValue.value.isNotEmpty() &&
                        selectedCategoryId.value > 0
                    ) {
                        quickExpenseBarViewModel.saveQuickExpense(
                            selectedCategoryId.value,
                            expenseName.value,
                            expenseValue.value.toDouble(),
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
            .padding(MaterialTheme.spacing.medium),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Add a quick expense", style = MaterialTheme.textStyle.h2)
        TeiraOutlinedButton(
            text = "Add",
            onClick = onClick
        )
    }
}

@Composable
fun CategoryDropdown(
    modifier: Modifier = Modifier,
    quickExpenseBarViewModel: QuickExpenseBarViewModel = hiltViewModel(),
    onClick: (Int) -> Unit
) {
    val expenseCategoryExpanded = remember { mutableStateOf(false) }
    val selectedCategoryName = remember { mutableStateOf("Select the Category") }
    val categoriesList by remember { quickExpenseBarViewModel.categories }.collectAsState(initial = listOf())

    Box(
        modifier = modifier
            .width(200.dp)
            .height(50.dp)
            .clickable(onClick = { expenseCategoryExpanded.value = true })
            .background(Purple40)
    ) {
        Text(
            text = selectedCategoryName.value,
            modifier = Modifier.align(Alignment.Center),
            color = Color.White,
            style = MaterialTheme.textStyle.h3
        )
    }
    DropdownMenu(
        modifier = Modifier.width(200.dp),
        expanded = expenseCategoryExpanded.value,
        onDismissRequest = { expenseCategoryExpanded.value = false },
    ) {
        categoriesList.forEach { category ->
            DropdownMenuItem(
                onClick = {
                    selectedCategoryName.value = category.name
                    expenseCategoryExpanded.value = false
                    onClick(category.expenseCategoryId)
                },
                text = {
                    Text(
                        text = category.name,
                        style = MaterialTheme.textStyle.h3extra
                    )
                }
            )
        }
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
            style = MaterialTheme.textStyle.h4
        )
    }
}
