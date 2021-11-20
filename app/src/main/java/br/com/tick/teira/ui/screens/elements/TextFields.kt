package br.com.tick.teira.ui.screens.elements

import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
fun QuickExpenseTextField(
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
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            backgroundColor = color
        ),
        modifier = modifier,
        value = value,
        singleLine = true,
        maxLines = 1,
        onValueChange = { onValueChanged(it) }
    )
}
