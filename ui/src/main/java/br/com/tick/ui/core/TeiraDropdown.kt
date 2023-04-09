package br.com.tick.ui.core

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import br.com.tick.R
import br.com.tick.ui.theme.spacing
import br.com.tick.ui.theme.textStyle

@OptIn(ExperimentalMaterial3Api::class)
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
            .border(width = MaterialTheme.spacing.smallest, color = borderColor)
            .defaultMinSize(
                minWidth = TextFieldDefaults.MinWidth,
                minHeight = TextFieldDefaults.MinHeight
            )
            .clickable(onClick = { isDropdownExpanded = true })
    ) {
        Text(
            text = label,
            modifier = Modifier.align(Alignment.Center),
            color = borderColor,
            style = MaterialTheme.textStyle.h3
        )
        DropdownMenu(
            modifier = Modifier.defaultMinSize(
                minWidth = TextFieldDefaults.MinWidth,
                minHeight = TextFieldDefaults.MinHeight
            ),
            expanded = isDropdownExpanded,
            onDismissRequest = { isDropdownExpanded = false },
        ) {
            dropdownItemLabels.forEachIndexed { index, label ->
                DropdownMenuItem(
                    onClick = {
                        selectedItemName = label
                        isDropdownExpanded = false
                        onItemSelected(index)
                    },
                    text = {
                        Text(
                            text = label,
                            style = MaterialTheme.textStyle.h3extra,
                            color = MaterialTheme.colorScheme.tertiary
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