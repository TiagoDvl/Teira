package br.com.tick.ui.core

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.tick.ui.R
import br.com.tick.ui.theme.textStyle

@Composable
fun TeiraDropdown(
    modifier: Modifier = Modifier,
    label: String,
    borderColor: Color,
    dropdownItemLabels: List<String>,
    onItemSelected: (Int) -> Unit,
    dropdownItemColors: List<Int?>? = null,
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
                val dropdownItemColor = dropdownItemColors?.get(index)?.let {
                    Color(it)
                } ?: MaterialTheme.colorScheme.secondary

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
                            style = MaterialTheme.textStyle.h3extra,
                            color = dropdownItemColor
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