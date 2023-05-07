package br.com.tick.ui.screens.wallet

import androidx.compose.foundation.clickable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import br.com.tick.ui.R
import br.com.tick.ui.theme.textStyle

@Composable
fun AddNewCategoryDialog(
    onAddNewCategory: (String) -> Unit,
    dismiss: () -> Unit
) {
    var newCategoryName by remember { mutableStateOf(TextFieldValue()) }
    val focusRequester = FocusRequester()

    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }

    AlertDialog(
        modifier = Modifier.focusRequester(focusRequester),
        onDismissRequest = { dismiss() },
        confirmButton = {
            Text(
                modifier = Modifier.clickable {
                    if (newCategoryName.text.isNotEmpty()) {
                        onAddNewCategory(newCategoryName.text)
                        dismiss()
                    }
                },
                text = stringResource(id = R.string.wallet_add_new_category_button_text),
                style = MaterialTheme.textStyle.h2small
            )
        },
        dismissButton = {
            Text(
                modifier = Modifier.clickable {
                    dismiss()
                },
                text = stringResource(id = R.string.generic_cancel),
                style = MaterialTheme.textStyle.h2small
            )
        },
        icon = {
            Icon(painter = painterResource(id = R.drawable.ic_add), contentDescription = null)
        },
        title = {
            Text(text = stringResource(id = R.string.wallet_add_new_category_label))
        },
        text = {
            OutlinedTextField(
                value = newCategoryName,
                onValueChange = {
                    newCategoryName = it
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.onSecondary,
        titleContentColor = MaterialTheme.colorScheme.tertiary,
        textContentColor = MaterialTheme.colorScheme.primary,
        iconContentColor = MaterialTheme.colorScheme.onTertiary
    )
}
