package br.com.tick.ui.screens.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.tick.ui.theme.spacing

@Composable
fun WalletScreen() {
    val showAddCategoryDialogState = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(MaterialTheme.spacing.small),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {
        if (showAddCategoryDialogState.value) {
            AddNewCategoryDialog {
                showAddCategoryDialogState.value = false
            }
        }
        QuickExpense(showAddCategoryDialogState = showAddCategoryDialogState)
        ExpensesGrid()
    }
}
