package br.com.tick.ui.screens.expense

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import br.com.tick.ui.R
import br.com.tick.ui.core.TeiraDatePicker
import br.com.tick.ui.core.TeiraDropdown
import br.com.tick.ui.screens.expense.viewmodels.ExpenseViewModel
import br.com.tick.ui.screens.shared.AddCategoryDialog
import br.com.tick.ui.theme.spacing
import br.com.tick.ui.theme.textStyle
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseScreen(
    navHostController: NavHostController,
    expenseViewModel: ExpenseViewModel = hiltViewModel(),
    expenseId: Int? = null
) {
    var showAddNewCategoryDialog by remember { mutableStateOf(false) }

    val expense by expenseViewModel.categorizedExpense.collectAsStateWithLifecycle()
    val categoriesList by expenseViewModel.categories.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        expenseId?.let { expenseViewModel.getExpense(it) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onSecondary),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navHostController.navigateUp() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        if (expense != null) {
                            Text(
                                text = stringResource(id = R.string.expense_title),
                                style = MaterialTheme.textStyle.h2
                            )
                            Text(
                                text = stringResource(id = R.string.expense_save),
                                style = MaterialTheme.textStyle.h2bold
                            )
                        } else {
                            Text(
                                text = stringResource(id = R.string.expense_add_title),
                                style = MaterialTheme.textStyle.h2
                            )
                            Text(
                                text = stringResource(id = R.string.expense_add),
                                style = MaterialTheme.textStyle.h2bold
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    titleContentColor = MaterialTheme.colorScheme.onSecondary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSecondary
                )
            )

            var expenseName by remember { mutableStateOf(TextFieldValue()) }
            var expenseCost by remember { mutableStateOf(TextFieldValue()) }
            var isInvalidValue by remember { mutableStateOf(false) }

            var selectedCategoryId by remember { mutableIntStateOf(-1) }
            val initialCategoryLabel = stringResource(id = R.string.expense_empty_category_title)
            var categoryLabel by remember { mutableStateOf(initialCategoryLabel) }

            var localDate by remember { mutableStateOf(LocalDate.now()) }

            if (showAddNewCategoryDialog) {
                AddCategoryDialog(onAddNewCategory = { categoryLabel = it }) {
                    showAddNewCategoryDialog = false
                }
            }


            expense?.let {
                expenseName = TextFieldValue(it.name)
                expenseCost = TextFieldValue(it.expenseValue.toString())
                categoryLabel = it.category.name
                selectedCategoryId = it.category.expenseCategoryId
                localDate = it.date
                Log.d("Tiago", expense.toString())
            }

            val expenseTextFieldColors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                focusedTextColor = MaterialTheme.colorScheme.secondary,
                unfocusedTextColor = MaterialTheme.colorScheme.secondary,
                errorBorderColor = MaterialTheme.colorScheme.tertiary,
                errorLabelColor = MaterialTheme.colorScheme.tertiary,
                errorTextColor = MaterialTheme.colorScheme.tertiary,
                errorTrailingIconColor = MaterialTheme.colorScheme.secondary
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.small)
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = expenseName,
                    label = {
                        Text(
                            text = stringResource(id = R.string.expense_expense_expense),
                            style = MaterialTheme.textStyle.h2small
                        )
                    },
                    onValueChange = {
                        expenseName = it
                    },
                    colors = expenseTextFieldColors
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = MaterialTheme.spacing.smallest),
                    value = expenseCost,
                    label = {
                        Text(
                            text = stringResource(id = R.string.expense_expense_cost),
                            style = MaterialTheme.textStyle.h2small
                        )
                    },
                    onValueChange = {
                        expenseCost = it
                        isInvalidValue = false
                        try {
                            expenseCost.text.toDouble()
                        } catch (exception: NumberFormatException) {
                            isInvalidValue = true
                        }
                    },
                    colors = expenseTextFieldColors
                )
                Row(modifier = Modifier.padding(top = MaterialTheme.spacing.small)) {
                    TeiraDropdown(
                        modifier = Modifier.weight(0.5f),
                        label = categoryLabel,
                        borderColor = MaterialTheme.colorScheme.primary,
                        dropdownItemLabels = categoriesList.map { it.name },
                        dropdownItemColors = categoriesList.map { it.color },
                        onItemSelected = {
                            selectedCategoryId = categoriesList[it].expenseCategoryId
                            categoryLabel = categoriesList[it].name
                        },
                        lastItemLabel = stringResource(id = R.string.expense_add_category),
                        onLastItemSelected = {
                            showAddNewCategoryDialog = true
                        }
                    )

                    Column(
                        modifier = Modifier
                            .weight(0.5f)
                            .height(56.dp),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.End
                    ) {
                        TeiraDatePicker(localDate = localDate) {
                            localDate = it
                        }
                    }
                }

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = MaterialTheme.spacing.medium),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.secondary
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = MaterialTheme.spacing.medium),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        painter = painterResource(id = R.drawable.ic_location),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                    Text(
                        modifier = Modifier.padding(start = MaterialTheme.spacing.extraSmall),
                        text = stringResource(id = R.string.expense_location),
                        style = MaterialTheme.textStyle.h2,
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Icon(
                            modifier = Modifier
                                .size(32.dp)
                                .align(Alignment.BottomEnd),
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }

                Text(
                    modifier = Modifier.padding(top = MaterialTheme.spacing.extraSmall),
                    text = "Rua Maria Feliciana, 210, Apartamento 608",
                    style = MaterialTheme.textStyle.h2small,
                    color = MaterialTheme.colorScheme.secondary
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = MaterialTheme.spacing.extraLarge),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        painter = painterResource(id = R.drawable.ic_add_photo),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                    Text(
                        modifier = Modifier.padding(start = MaterialTheme.spacing.extraSmall),
                        text = stringResource(id = R.string.expense_picture),
                        style = MaterialTheme.textStyle.h2,
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Icon(
                            modifier = Modifier
                                .size(32.dp)
                                .align(Alignment.BottomEnd),
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }

                Text(
                    modifier = Modifier.padding(top = MaterialTheme.spacing.extraSmall),
                    text = "temp/0/whatever/Teira/1837984.jpg",
                    style = MaterialTheme.textStyle.h2small,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            var showDeletionConfirmationDialog by remember { mutableStateOf(false) }

            if (showDeletionConfirmationDialog) {
                AlertDialog(
                    onDismissRequest = { showDeletionConfirmationDialog = false },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_priority_high),
                            contentDescription = null
                        )
                    },
                    confirmButton = {
                        Text(
                            modifier = Modifier.clickable {
                                // Remove the expense
                                showDeletionConfirmationDialog = false
                            },
                            text = stringResource(id = R.string.generic_ok).uppercase()
                        )
                    },
                    dismissButton = {
                        Text(
                            modifier = Modifier.clickable {
                                showDeletionConfirmationDialog = false
                            },
                            text = stringResource(id = R.string.generic_cancel).uppercase()
                        )
                    },
                    title = {
                        Text(text = stringResource(id = R.string.expense_delete_title))
                    },
                    text = {
                        Text(text = stringResource(id = R.string.expense_delete_description))
                    },
                    containerColor = MaterialTheme.colorScheme.onSecondary,
                    titleContentColor = MaterialTheme.colorScheme.tertiary,
                    textContentColor = MaterialTheme.colorScheme.primary,
                    iconContentColor = MaterialTheme.colorScheme.onTertiary
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.spacing.extraLarge)
            ) {
                if (expenseId != null) {
                    FloatingActionButton(
                        modifier = Modifier.align(Alignment.BottomEnd),
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        onClick = { showDeletionConfirmationDialog = true}
                    ) {
                        Image(painter = painterResource(id = R.drawable.ic_delete), contentDescription = null)
                    }
                }
            }
        }
    }
}