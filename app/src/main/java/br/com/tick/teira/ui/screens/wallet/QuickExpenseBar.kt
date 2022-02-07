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
import androidx.compose.runtime.setValue
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
    var isExpanded by remember { mutableStateOf(false) }
    var quickExpenseComposableHeight by remember { mutableStateOf(80.dp) }
    val animatedSize by animateDpAsState(
        targetValue = quickExpenseComposableHeight,
        tween(
            durationMillis = 500
        )
    )

    Column(
        modifier = modifier
            .padding(MaterialTheme.spacing.extraSmall)
            .background(Purple80)
            .height(animatedSize)
    ) {
        if (isExpanded) {
            quickExpenseComposableHeight = 200.dp
            ExpandedQuickExpense(
                showAddCategoryDialogState = showAddCategoryDialogState
            ) {
                isExpanded = isExpanded.not()
            }
        } else {
            quickExpenseComposableHeight = 80.dp
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                ClosedQuickExpense {
                    isExpanded = isExpanded.not()
                }
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
    var expenseValue by remember { mutableStateOf("") }

    var expenseCategoryExpanded by remember { mutableStateOf(false) }
    var selectedCategoryName by remember { mutableStateOf("Select the Category") }
    var selectedCategoryId by remember { mutableStateOf(0) }
    val categoriesList by remember { quickExpenseBarViewModel.categories }.collectAsState(initial = listOf())

    val expenseDate = Date().time

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(MaterialTheme.spacing.medium)
    ) {
        Column(
            modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                TeiraBaseTextField(
                    modifier = Modifier.width(200.dp),
                    color = Pink40,
                    value = expenseName,
                    label = "Expense Name"
                ) {
                    expenseName = it
                }
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
                TeiraBaseTextField(
                    modifier = Modifier.width(200.dp),
                    color = Pink40,
                    value = expenseValue,
                    label = "Expense Value"
                ) {
                    expenseValue = it
                }
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
            ) {
                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .height(50.dp)
                        .clickable(onClick = { expenseCategoryExpanded = true })
                        .background(Purple40)
                ) {
                    Text(selectedCategoryName, modifier = Modifier.align(Alignment.Center), color = Color.White)
                }
                DropdownMenu(
                    modifier = Modifier.width(200.dp),
                    expanded = expenseCategoryExpanded,
                    onDismissRequest = { expenseCategoryExpanded = false })
                {
                    categoriesList.forEach { category ->
                        DropdownMenuItem(
                            onClick = {
                                selectedCategoryName = category.name
                                selectedCategoryId = category.categoryId
                                expenseCategoryExpanded = false
                            }
                        ) {
                            Text(text = category.name)
                        }
                    }
                }
                Spacer(modifier = Modifier.width(4.dp))
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
        }
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            QuickExpenseDate(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.BottomStart),
                date = expenseDate
            )
            TeiraOutlinedButton(
                modifier = Modifier.align(Alignment.BottomEnd),
                text = "Save",
                onClick = {
                    onClick() // This will cause a recomposition
                    if (expenseName.isNotEmpty() && expenseValue.isNotEmpty() && selectedCategoryId > 0) {
                        quickExpenseBarViewModel.saveQuickExpense(
                            selectedCategoryId,
                            expenseName,
                            expenseValue.toDouble(),
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
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.medium)
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterStart),
            text = "Add a quick expense"
        )
        TeiraOutlinedButton(
            modifier = Modifier.align(Alignment.CenterEnd),
            text = "Add",
            onClick = onClick
        )
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
