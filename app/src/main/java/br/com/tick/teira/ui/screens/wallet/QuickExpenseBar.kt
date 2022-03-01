package br.com.tick.teira.ui.screens.wallet

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.tick.teira.R
import br.com.tick.teira.ui.elements.TeiraBaseTextField
import br.com.tick.teira.ui.elements.TeiraOutlinedButton
import br.com.tick.teira.ui.screens.wallet.viewmodels.QuickExpenseBarViewModel
import br.com.tick.teira.ui.theme.Pink40
import br.com.tick.teira.ui.theme.Purple40
import br.com.tick.teira.ui.theme.Purple80
import br.com.tick.teira.ui.theme.spacing
import java.text.SimpleDateFormat
import java.util.Date

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
                colors = ButtonDefaults.textButtonColors(
                    containerColor = Purple40
                ),
                onClick = {
                    showAddCategoryDialogState.value = true
                }
            ) {
                Image(painter = painterResource(id = R.drawable.ic_add), contentDescription = "Add Category")
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            val expenseDate = Date().time

            QuickExpenseDate(
                modifier = Modifier.weight(0.5f),
                date = expenseDate
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
                            expenseDate
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
            .padding(MaterialTheme.spacing.large),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Add a quick expense"
        )
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
        Text(selectedCategoryName.value, modifier = Modifier.align(Alignment.Center), color = Color.White)
    }
    DropdownMenu(
        modifier = Modifier.width(200.dp),
        expanded = expenseCategoryExpanded.value,
        onDismissRequest = { expenseCategoryExpanded.value = false })
    {
        categoriesList.forEach { category ->
            DropdownMenuItem(
                onClick = {
                    selectedCategoryName.value = category.name
                    expenseCategoryExpanded.value = false
                    onClick(category.categoryId)
                }
            ) {
                Text(text = category.name)
            }
        }
    }
}

@Composable
fun QuickExpenseDate(
    modifier: Modifier = Modifier,
    date: Long
) {
    val formatted = SimpleDateFormat("dd/MM/yyyy").format(Date(date))
    Box(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.align(Alignment.BottomStart),
            text = "Date: $formatted",
            textDecoration = TextDecoration.Underline,
            fontSize = 14.sp,
            fontStyle = FontStyle.Italic
        )
    }
}
