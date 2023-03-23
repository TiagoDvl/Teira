package br.com.tick.ui.core

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.tick.R
import br.com.tick.sdk.domain.ExpenseCategory
import br.com.tick.sdk.domain.ExpenseRisk
import br.com.tick.ui.screens.wallet.models.ExpenseCard
import br.com.tick.ui.theme.spacing
import br.com.tick.ui.theme.textStyle

@ExperimentalFoundationApi
@Composable
fun QuickExpenseCard(
    expenseCard: ExpenseCard,
    modifier: Modifier = Modifier,
    onQuickActionDelete: (expenseId: Int) -> Unit
) {
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

            Text(
                modifier = Modifier.align(Alignment.BottomStart),
                text = "€${expenseCard.value}",
                style = MaterialTheme.textStyle.h3small,
                color = MaterialTheme.colorScheme.onTertiary
            )

            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .width(100.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
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
                        .clickable { onQuickActionDelete(expenseCard.id) },
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "Delete expense"
                )
            }
        }
    }
}

@Preview
@Composable
fun QuickExpenseCardPreview() {
    QuickExpenseCard(
        expenseCard = ExpenseCard(
            id = 0,
            name = "Expense 1",
            value = 50.0,
            category = ExpenseCategory(0, "Category 1"),
            risk = ExpenseRisk.HIGH
        )
    ) {

    }
}
