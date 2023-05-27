package br.com.tick.ui.core

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import br.com.tick.ui.R
import br.com.tick.ui.theme.spacing
import br.com.tick.ui.theme.textStyle

@Composable
fun TeiraLoadingState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(MaterialTheme.spacing.extraSmall),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CircularProgressIndicator()
            Text(
                modifier = Modifier.padding(start = MaterialTheme.spacing.small),
                text = stringResource(id = R.string.generic_loading),
                style = MaterialTheme.textStyle.h2,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
fun TeiraErrorState(
    modifier: Modifier = Modifier
) {
    DashedScreenState(modifier, R.drawable.ic_frown, R.string.generic_error)
}

@Composable
fun TeiraEmptyState(
    modifier: Modifier = Modifier
) {
    DashedScreenState(modifier, R.drawable.ic_empty_list, R.string.generic_empty_state)
}

@Composable
fun TeiraNoAvailableDataState(
    modifier: Modifier = Modifier
) {
    DashedScreenState(modifier, R.drawable.ic_frown, R.string.generic_no_data_available)
}

@Composable
private fun DashedScreenState(
    modifier: Modifier = Modifier,
    iconId: Int,
    textId: Int,
    color: Color = MaterialTheme.colorScheme.tertiary,
    textColor: Color = MaterialTheme.colorScheme.tertiary
) {
    val stroke = Stroke(
        width = 2f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )

    Column(
        modifier = modifier.drawBehind { drawRoundRect(color = color, style = stroke) },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.padding(MaterialTheme.spacing.small),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = iconId),
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = null
            )
            Text(
                modifier = Modifier.padding(top = MaterialTheme.spacing.smallest),
                text = stringResource(id = textId),
                color = textColor,
                style = MaterialTheme.textStyle.h3
            )
        }
    }
}
