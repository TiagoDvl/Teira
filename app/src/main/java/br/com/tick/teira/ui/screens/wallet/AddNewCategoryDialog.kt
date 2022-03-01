package br.com.tick.teira.ui.screens.wallet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.tick.teira.ui.elements.TeiraBaseTextField
import br.com.tick.teira.ui.elements.TeiraOutlinedButton
import br.com.tick.teira.ui.screens.wallet.viewmodels.QuickExpenseBarViewModel
import br.com.tick.teira.ui.theme.Pink40
import br.com.tick.teira.ui.theme.spacing

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
                TeiraOutlinedButton(
                    text = "Add"
                ) {
                    if (categoryName.isNotBlank()) {
                        viewModel.addCategory(categoryName)
                    }
                    onClick()
                }
            }
        }
    }
}
