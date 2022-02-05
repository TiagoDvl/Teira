package br.com.tick.teira.ui.screens.wallet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import br.com.tick.teira.ui.theme.spacing
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.tick.teira.ui.elements.TeiraBaseTextField
import br.com.tick.teira.ui.elements.TeiraOutlinedButton
import br.com.tick.teira.ui.screens.wallet.viewmodels.QuickExpenseBarViewModel
import br.com.tick.teira.ui.theme.Pink40

@Composable
fun WalletScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val showAddCategoryDialogState = remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.spacing.extraSmall)
        ) {
            if (showAddCategoryDialogState.value) {
                AddNewCategoryDialog(showAddCategoryDialogState)
            }
            QuickExpense(
                modifier = Modifier.fillMaxWidth(),
                showAddCategoryDialogState
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MaterialTheme.spacing.small)
            )
            ExpensesGrid(
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun AddNewCategoryDialog(
    showAddCategoryDialogState: MutableState<Boolean>,
    viewModel: QuickExpenseBarViewModel = hiltViewModel()
) {
    Dialog(onDismissRequest = { showAddCategoryDialogState.value = false }) {
        Card(
            modifier = Modifier.height(200.dp).width(300.dp),
            shape = RoundedCornerShape(CornerSize(5.dp))
        ) {
            var categoryName by remember { mutableStateOf("") }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                TeiraBaseTextField(
                    modifier = Modifier.width(200.dp),
                    color = Pink40,
                    value = categoryName,
                    label = "New Category Name"
                ) {
                    categoryName = it
                }
                TeiraOutlinedButton(text = "Add") {
                    if (categoryName.isNotBlank()) {
                        viewModel.addCategory(categoryName)
                    }
                    showAddCategoryDialogState.value = false
                }
            }
        }
    }
}
