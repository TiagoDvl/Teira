package br.com.tick.ui.core

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import br.com.tick.ui.theme.Purple40
import br.com.tick.ui.theme.textStyle

@Composable
fun TeiraOutlinedButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    FilledTonalButton(
        modifier = modifier,
        colors = ButtonDefaults.textButtonColors(containerColor = Purple40),
        onClick = { onClick() }
    ) {
        Text(
            text = text,
            color = Color.White,
            style = MaterialTheme.textStyle.h2small
        )
    }
}
