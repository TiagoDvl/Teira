package br.com.tick.ui.screens.wallet

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.tick.ui.core.TeiraBaseTextField
import br.com.tick.ui.core.TeiraOutlinedButton
import br.com.tick.ui.screens.wallet.viewmodels.QuickExpenseBarViewModel
import br.com.tick.ui.theme.Pink40
import br.com.tick.ui.theme.spacing

@Composable
fun AddNewCategoryDialog(
    viewModel: QuickExpenseBarViewModel = hiltViewModel(),
    onClick: () -> Unit
) {
    Dialog(onDismissRequest = { onClick() }) {
        Card(
            modifier = Modifier
                .height(200.dp)
                .width(300.dp)
                .padding(MaterialTheme.spacing.extraSmall),
            shape = RoundedCornerShape(CornerSize(5.dp))
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                var categoryName by remember { mutableStateOf("") }

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
                    onClick()
                }
            }
        }
    }
}
