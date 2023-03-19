package br.com.tick.ui.core

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import br.com.tick.R
import br.com.tick.ui.screens.wallet.models.ExpenseCard
import br.com.tick.ui.theme.PurpleGrey80
import br.com.tick.ui.theme.spacing

@ExperimentalFoundationApi
@Composable
fun QuickExpenseCard(
    expenseCard: ExpenseCard,
    modifier: Modifier = Modifier,
    onQuickActionDelete: (expenseId: Int) -> Unit
) {
    Card(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PurpleGrey80)
                .height(160.dp)
                .padding(MaterialTheme.spacing.medium)
        ) {
            Column {
                Text(text = expenseCard.name, style = MaterialTheme.typography.bodyLarge)
                Text(text = expenseCard.risk.name, style = MaterialTheme.typography.bodySmall)
            }

            Text(
                modifier = Modifier.align(Alignment.BottomStart),
                text = "€${expenseCard.value}",
                style = MaterialTheme.typography.bodyLarge
            )

            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .width(100.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    tint = Color.White,
                    painter = painterResource(id = R.drawable.ic_slide_right),
                    contentDescription = "Open expense quick actions"
                )

                Icon(
                    modifier = Modifier.size(24.dp),
                    tint = Color.White,
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = "Edit expense"
                )

                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onQuickActionDelete(expenseCard.id) },
                    tint = Color.White,
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "Delete expense"
                )
            }
        }
    }
}
