package br.com.tick.ui.core

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import br.com.tick.ui.theme.textStyle

@Composable
fun TeiraFilledTonalButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    FilledTonalButton(
        modifier = modifier,
        colors = ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary
        ),
        onClick = { onClick() }
    ) {
        Text(
            text = text,
            color = Color.White,
            style = MaterialTheme.textStyle.h2small
        )
    }
}
