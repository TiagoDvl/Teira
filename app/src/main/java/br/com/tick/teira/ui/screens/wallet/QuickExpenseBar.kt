package br.com.tick.teira.ui.screens.wallet

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.tick.teira.ui.screens.elements.QuickExpenseTextField
import br.com.tick.teira.ui.screens.elements.TeiraOutlinedButton
import br.com.tick.teira.ui.screens.wallet.viewmodels.QuickExpenseBarViewModel
import br.com.tick.teira.ui.theme.Pink40
import br.com.tick.teira.ui.theme.Purple80

@Composable
fun QuickExpense(
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    var quickExpenseComposableHeight by remember { mutableStateOf(50.dp) }
    val animatedSize by animateDpAsState(
        targetValue = quickExpenseComposableHeight,
        tween(
            durationMillis = 500
        )
    )

    Column(
        modifier = modifier
            .padding(4.dp)
            .background(Purple80)
            .height(animatedSize)
    ) {
        if (isExpanded) {
            quickExpenseComposableHeight = 200.dp
            ExpandedQuickExpense(modifier) {
                isExpanded = isExpanded.not()
            }

        } else {
            quickExpenseComposableHeight = 50.dp
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
    quickExpenseBarViewModel: QuickExpenseBarViewModel = hiltViewModel(),
    onClick: () -> Unit
) {
    var expenseName by remember { mutableStateOf("") }
    var expenseValue by remember { mutableStateOf("") }
    var expenseCategory by remember { mutableStateOf("") }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        QuickExpenseTextField(
            modifier = Modifier.width(200.dp),
            color = Pink40,
            value = expenseName,
            label = "Expense Name"
        ) {
            expenseName = it
        }
        Spacer(modifier = Modifier.width(2.dp))
        QuickExpenseTextField(
            modifier = Modifier.width(200.dp),
            color = Pink40,
            value = expenseValue,
            label = "Expense Value"
        ) {
            expenseValue = it
        }
    }
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
    ) {
        QuickExpenseTextField(
            modifier = Modifier.width(200.dp),
            color = Pink40,
            value = expenseCategory,
            label = "Expense Category"
        ) {
            expenseCategory = it
        }
    }
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        TeiraOutlinedButton(
            modifier = Modifier.align(Alignment.Bottom),
            text = "Save",
            onClick = {
                onClick() // This will cause a recomposition
                quickExpenseBarViewModel.saveQuickExpense(expenseName, expenseValue, expenseCategory)
            }
        )
    }
}

@Composable
fun ClosedQuickExpense(onClick: () -> Unit) {
    Text(text = "Add a quick expense")
    TeiraOutlinedButton(text = "Add", onClick = onClick)
}

