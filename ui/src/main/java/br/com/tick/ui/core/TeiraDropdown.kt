package br.com.tick.ui.core

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.tick.ui.R
import br.com.tick.ui.theme.spacing
import br.com.tick.ui.theme.textStyle

@Composable
fun TeiraDropdown(
    modifier: Modifier = Modifier,
    label: String,
    borderColor: Color,
    dropdownItemLabels: List<String>,
    onItemSelected: (Int) -> Unit,
    lastItemLabel: String? = null,
    onLastItemSelected: (() -> Unit)? = null
) {

    var isDropdownExpanded by remember { mutableStateOf(false) }
    var selectedItemName by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(8))
            .clickable(onClick = { isDropdownExpanded = true })
    ) {
        Text(
            text = label,
            modifier = Modifier.align(Alignment.Center),
            color = borderColor,
            style = MaterialTheme.textStyle.h3
        )
        DropdownMenu(
            modifier = Modifier.background(MaterialTheme.colorScheme.onSecondary),
            expanded = isDropdownExpanded,
            onDismissRequest = { isDropdownExpanded = false }
        ) {
            dropdownItemLabels.forEachIndexed { index, label ->
                DropdownMenuItem(
                    modifier = Modifier.background(MaterialTheme.colorScheme.onSecondary),
                    colors = MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.primary
                    ),
                    onClick = {
                        selectedItemName = label
                        isDropdownExpanded = false
                        onItemSelected(index)
                    },
                    text = {
                        Text(
                            text = label,
                            style = MaterialTheme.textStyle.h3extra
                        )
                    }
                )
            }

            if (lastItemLabel != null && onLastItemSelected != null) {
                DropdownMenuItem(
                    onClick = {
                        onLastItemSelected()
                        isDropdownExpanded = false
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add),
                            tint = MaterialTheme.colorScheme.tertiary,
                            contentDescription = stringResource(id = R.string.wallet_quick_expense_add_category)
                        )
                    },
                    text = {
                        Text(
                            text = lastItemLabel,
                            style = MaterialTheme.textStyle.h3extra,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                )
            }
        }
    }
}