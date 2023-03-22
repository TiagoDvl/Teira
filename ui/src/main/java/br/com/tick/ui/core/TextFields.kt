package br.com.tick.ui.core

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import br.com.tick.ui.theme.textStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeiraBaseTextField(
    modifier: Modifier = Modifier,
    color: Color,
    value: String,
    label: String,
    onValueChanged: (String) -> Unit
) {
    TextField(
        modifier = modifier,
        label = {
            Text(
                text = label,
                style = MaterialTheme.textStyle.h2small
            )
        },
        colors = TextFieldDefaults.textFieldColors(containerColor = color, textColor = Color.White),
        value = value,
        singleLine = true,
        maxLines = 1,
        onValueChange = { onValueChanged(it) },
        textStyle = MaterialTheme.textStyle.h2
    )
}
