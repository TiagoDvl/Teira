package br.com.tick.ui.core

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.tick.sdk.domain.CurrencyFormat
import br.com.tick.sdk.domain.ExpenseCategory
import br.com.tick.sdk.domain.ExpenseRisk
import br.com.tick.ui.R
import br.com.tick.ui.extensions.getLabelResource
import br.com.tick.ui.screens.wallet.models.ExpenseCard
import br.com.tick.ui.theme.spacing
import br.com.tick.ui.theme.textStyle

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@ExperimentalFoundationApi
@Composable
fun QuickExpenseCard(
    expenseCard: ExpenseCard,
    modifier: Modifier = Modifier,
    onQuickActionDelete: (expenseId: Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .height(160.dp)
                .padding(MaterialTheme.spacing.medium)
        ) {
            Column {
                Text(
                    text = expenseCard.name,
                    style = MaterialTheme.textStyle.h2bold,
                    color = MaterialTheme.colorScheme.onTertiary
                )
                Text(
                    text = expenseCard.risk.name,
                    style = MaterialTheme.textStyle.h3,
                    color = MaterialTheme.colorScheme.onTertiary
                )
            }

            val currency = stringResource(id = expenseCard.currency.getLabelResource())
            Text(
                modifier = Modifier.align(Alignment.BottomStart),
                text = "$currency${expenseCard.value}",
                style = MaterialTheme.textStyle.h3small,
                color = MaterialTheme.colorScheme.onTertiary
            )

            AnimatedContent(
                modifier = Modifier.align(Alignment.BottomEnd),
                targetState = expanded
            ) { targetExpanded ->
                if (targetExpanded) {
                    ExpandedCardIcons(
                        onQuickActionDelete = { onQuickActionDelete(expenseCard.id) }
                    ) {
                        expanded = !expanded
                    }
                } else {
                    CollapsedCardIcons {
                        expanded = !expanded
                    }
                }
            }
        }
    }
}

@Composable
fun CollapsedCardIcons(modifier: Modifier = Modifier, onExpandIcons: () -> Unit) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Icon(
            modifier = Modifier
                .size(24.dp)
                .clickable { onExpandIcons() },
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            painter = painterResource(id = R.drawable.ic_slide_left),
            contentDescription = "Open expense quick actions",
        )
    }
}

@Composable
fun ExpandedCardIcons(
    modifier: Modifier = Modifier,
    onQuickActionDelete: () -> Unit,
    collapse: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Icon(
            modifier = Modifier
                .size(24.dp)
                .clickable { collapse() },
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            painter = painterResource(id = R.drawable.ic_slide_right),
            contentDescription = "Open expense quick actions",
        )

        Icon(
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            painter = painterResource(id = R.drawable.ic_edit),
            contentDescription = "Edit expense"
        )

        Icon(
            modifier = Modifier
                .size(24.dp)
                .clickable { onQuickActionDelete() },
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            painter = painterResource(id = R.drawable.ic_delete),
            contentDescription = "Delete expense"
        )
    }
}

@Preview
@Composable
fun QuickExpenseCardPreview() {
    QuickExpenseCard(
        expenseCard = ExpenseCard(
            id = 0,
            name = "Expense 1",
            currency = CurrencyFormat.EURO,
            value = 50.0,
            category = ExpenseCategory(0, "Category 1"),
            risk = ExpenseRisk.HIGH
        )
    ) {

    }
}
