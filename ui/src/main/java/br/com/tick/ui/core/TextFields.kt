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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun TeiraBaseTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    onImeDoneAction: ((String) -> Unit)? = null,
    onValueChanged: ((String) -> Unit)? = null
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var valueState by remember { mutableStateOf("") }

    LaunchedEffect(key1 = value) {
        valueState = value
    }

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
        colors = TextFieldDefaults.colors(
            cursorColor = MaterialTheme.colorScheme.tertiary,
            focusedLabelColor = MaterialTheme.colorScheme.tertiary,
            unfocusedLabelColor = MaterialTheme.colorScheme.primary
        ),
        value = valueState,
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
        onValueChange = {
            valueState = it
            onValueChanged?.invoke(it)
        },
        textStyle = MaterialTheme.textStyle.h2
    )
}
