package br.com.tick.ui.core

import androidx.compose.foundation.background
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

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
        label = {
            Text(
                text = label,
                style = TextStyle(
                    color = Color.LightGray,
                    fontSize = 14.sp
                )
            )
        },
        colors = TextFieldDefaults.textFieldColors(textColor = Color.White),
        modifier = modifier.background(color),
        value = value,
        singleLine = true,
        maxLines = 1,
        onValueChange = { onValueChanged(it) }
    )
}
