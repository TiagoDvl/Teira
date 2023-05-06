package br.com.tick.ui.core

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import br.com.tick.ui.theme.textStyle

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TeiraBaseTextField(
    modifier: Modifier = Modifier,
    initialValue: String,
    label: String? = null,
    placeholderText: String? = null,
    prefixText: String? = null,
    isError: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    onImeDoneAction: ((String) -> Unit)? = null,
    onValueChanged: ((String) -> Unit)? = null
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var valueState by remember { mutableStateOf(initialValue) }

    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = valueState,
        onValueChange = {
            valueState = it
            onValueChanged?.invoke(valueState)
        },
        textStyle = MaterialTheme.textStyle.h2,
        label = {
            label?.let {
                Text(
                    text = it,
                    style = MaterialTheme.textStyle.h2small
                )
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onImeDoneAction?.invoke(valueState)
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        ),
        trailingIcon = trailingIcon,
        isError = isError
    )
}
