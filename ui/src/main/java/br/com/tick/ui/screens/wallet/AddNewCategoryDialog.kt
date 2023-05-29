package br.com.tick.ui.screens.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import br.com.tick.ui.R
import br.com.tick.ui.extensions.conditional
import br.com.tick.ui.theme.spacing
import br.com.tick.ui.theme.textStyle
import com.godaddy.android.colorpicker.ClassicColorPicker
import com.godaddy.android.colorpicker.HsvColor
import com.godaddy.android.colorpicker.toColorInt

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddNewCategoryDialog(
    colors: List<Int>,
    onNewColor: (Int) -> Unit,
    onAddNewCategory: (String, Int?) -> Unit,
    dismiss: () -> Unit
) {
    var newCategoryName by remember { mutableStateOf(TextFieldValue()) }
    var showColorPicker by remember { mutableStateOf(false) }
    var selectedCategoryColorIndex by remember { mutableStateOf(-1) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = FocusRequester()
    var chosenColor = HsvColor.DEFAULT.toColorInt()

    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }
    AlertDialog(
        modifier = Modifier.focusRequester(focusRequester),
        onDismissRequest = { dismiss() },
        confirmButton = {
            Text(
                modifier = Modifier.clickable {
                    if (newCategoryName.text.isNotEmpty()) {
                        val color = if (selectedCategoryColorIndex >= 0 && selectedCategoryColorIndex < colors.size) {
                            colors[selectedCategoryColorIndex]
                        } else {
                            null
                        }
                        onAddNewCategory(newCategoryName.text, color)
                        dismiss()
                    }
                },
                text = stringResource(id = R.string.wallet_add_new_category_button_text),
                style = MaterialTheme.textStyle.h2small
            )
        },
        dismissButton = {
            Text(
                modifier = Modifier.clickable {
                    dismiss()
                },
                text = stringResource(id = R.string.generic_cancel),
                style = MaterialTheme.textStyle.h2small
            )
        },
        icon = {
            Icon(painter = painterResource(id = R.drawable.ic_add), contentDescription = null)
        },
        title = {
            Text(text = stringResource(id = R.string.wallet_add_new_category_label))
        },
        text = {
            Column {
                OutlinedTextField(
                    value = newCategoryName,
                    onValueChange = {
                        newCategoryName = it
                    }
                )
                Text(
                    modifier = Modifier.padding(top = MaterialTheme.spacing.small),
                    text = stringResource(id = R.string.wallet_add_new_category_color),
                    style = MaterialTheme.textStyle.h3small,
                    color = MaterialTheme.colorScheme.tertiary
                )

                Row(
                    modifier = Modifier
                        .padding(top = MaterialTheme.spacing.small)
                        .horizontalScroll(rememberScrollState())
                ) {
                    colors.forEachIndexed { index, categoryColor ->
                        val borderColor = MaterialTheme.colorScheme.tertiary
                        val isLastColor = index == colors.size - 1

                        if (isLastColor) {
                            selectedCategoryColorIndex = index
                        }

                        val modifier = Modifier
                            .size(42.dp)
                            .clickable {
                                selectedCategoryColorIndex = index
                            }
                            .clip(RoundedCornerShape(4.dp))
                            .background(color = Color(categoryColor))
                            .conditional(selectedCategoryColorIndex == index || isLastColor) {
                                border(
                                    width = 2.dp,
                                    color = borderColor,
                                    shape = RoundedCornerShape(4.dp)
                                )
                            }

                        Box(modifier = modifier)
                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
                    }
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clickable {
                                showColorPicker = true
                                focusRequester.freeFocus()
                                keyboardController?.hide()
                            }
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.White)
                            .border(
                                width = 2.dp,
                                color = Color.White,
                                shape = RoundedCornerShape(4.dp)
                            )
                    ) {
                        Icon(
                            modifier = Modifier.align(Alignment.Center),
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = null
                        )
                    }
                }

                if (showColorPicker) {
                    AlertDialog(
                        onDismissRequest = { showColorPicker = false },
                        confirmButton = {
                            Text(
                                modifier = Modifier.clickable {
                                    onNewColor(chosenColor)
                                    showColorPicker = false
                                },
                                text = stringResource(id = R.string.wallet_add_new_category_button_text),
                                style = MaterialTheme.textStyle.h2small
                            )
                        },
                        dismissButton = {
                            Text(
                                modifier = Modifier.clickable {
                                    showColorPicker = false
                                },
                                text = stringResource(id = R.string.generic_cancel),
                                style = MaterialTheme.textStyle.h2small
                            )
                        },
                        text = {
                            ClassicColorPicker(
                                modifier = Modifier.height(200.dp),
                                color = HsvColor.from(Color(chosenColor)),
                                onColorChanged = { color ->
                                    chosenColor = color.toColorInt()
                                }
                            )
                        },
                        icon = {
                            Icon(painter = painterResource(id = R.drawable.ic_pallete), contentDescription = null)
                        },
                        title = {
                            Text(text = stringResource(id = R.string.wallet_choose_category_color))
                        },
                        containerColor = MaterialTheme.colorScheme.onSecondary,
                        titleContentColor = MaterialTheme.colorScheme.tertiary,
                        textContentColor = MaterialTheme.colorScheme.primary,
                        iconContentColor = MaterialTheme.colorScheme.onTertiary
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.onSecondary,
        titleContentColor = MaterialTheme.colorScheme.tertiary,
        textContentColor = MaterialTheme.colorScheme.primary,
        iconContentColor = MaterialTheme.colorScheme.onTertiary
    )


}
