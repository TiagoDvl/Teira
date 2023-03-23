package br.com.tick.ui.core

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import br.com.tick.ui.theme.textStyle

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun TeiraBaseTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    onImeDoneAction: (() -> Unit)? = null,
    onValueChanged: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    TextField(
        modifier = modifier.fillMaxWidth(),
        label = {
            if (label != null) {
                Text(
                    text = label,
                    style = MaterialTheme.textStyle.h2small
                )
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colorScheme.onTertiary,
            containerColor = MaterialTheme.colorScheme.onSecondary,
            cursorColor = MaterialTheme.colorScheme.tertiary,
            placeholderColor = MaterialTheme.colorScheme.tertiary,
            focusedLabelColor = MaterialTheme.colorScheme.tertiary,
            unfocusedLabelColor = MaterialTheme.colorScheme.primary
        ),
        value = value,
        singleLine = true,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onImeDoneAction?.invoke()
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        ),
        onValueChange = { onValueChanged(it) },
        textStyle = MaterialTheme.textStyle.h2
    )
}
