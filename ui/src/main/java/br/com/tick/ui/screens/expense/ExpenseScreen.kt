@file:OptIn(ExperimentalPermissionsApi::class)

package br.com.tick.ui.screens.expense

import android.Manifest
import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import br.com.tick.sdk.domain.CategorizedExpense
import br.com.tick.sdk.domain.CurrencyFormat
import br.com.tick.sdk.domain.ExpenseCategory
import br.com.tick.ui.R
import br.com.tick.ui.core.TeiraDatePicker
import br.com.tick.ui.core.TeiraDropdown
import br.com.tick.ui.core.TeiraEmptyState
import br.com.tick.ui.extensions.getLabelResource
import br.com.tick.ui.screens.expense.viewmodels.ExpenseViewModel
import br.com.tick.ui.screens.shared.AddCategoryDialog
import br.com.tick.ui.theme.spacing
import br.com.tick.ui.theme.textStyle
import br.com.tick.utils.ComposeFileProvider
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseScreen(
    navHostController: NavHostController,
    expenseViewModel: ExpenseViewModel = hiltViewModel(),
    expenseId: Int? = null
) {
    var showDeletionConfirmationDialog by remember { mutableStateOf(false) }

    val expense by expenseViewModel.categorizedExpense.collectAsStateWithLifecycle()
    val currency by expenseViewModel.currency.collectAsStateWithLifecycle()
    val categoriesList by expenseViewModel.categories.collectAsStateWithLifecycle()

    var expenseName by remember { mutableStateOf("") }
    var expenseValue by remember { mutableDoubleStateOf(0.0) }
    var isInvalidValue by remember { mutableStateOf(false) }
    var expenseCategoryId by remember { mutableIntStateOf(-1) }
    var expenseDate by remember { mutableStateOf(LocalDate.now()) }
    var location by remember { mutableStateOf<LatLng?>(null) }
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        expenseId?.let { expenseViewModel.getExpense(it) }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
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
                                modifier = Modifier.clickable {
                                    if (isInvalidValue) {
                                        scope.launch {
                                            snackbarHostState
                                                .showSnackbar(
                                                    message = context.getString(R.string.expense_save_invalid_message),
                                                    duration = SnackbarDuration.Short
                                                )
                                        }
                                    } else {
                                        expenseViewModel.handleExpense(
                                            expenseId = expenseId,
                                            categoryId = expenseCategoryId,
                                            name = expenseName,
                                            value = expenseValue,
                                            expenseDate = expenseDate,
                                            location = location,
                                            photoUri = photoUri
                                        )
                                        navHostController.navigateUp()
                                    }
                                },
                                text = stringResource(id = R.string.expense_save),
                                style = MaterialTheme.textStyle.h2bold
                            )
                        } else {
                            Text(
                                text = stringResource(id = R.string.expense_add_title),
                                style = MaterialTheme.textStyle.h2
                            )
                            Text(
                                modifier = Modifier.clickable {
                                    if (isInvalidValue) {
                                        scope.launch {
                                            snackbarHostState
                                                .showSnackbar(
                                                    message = context.getString(R.string.expense_add_invalid_message),
                                                    duration = SnackbarDuration.Short
                                                )
                                        }
                                    } else {
                                        expenseViewModel.handleExpense(
                                            expenseId = expenseId,
                                            categoryId = expenseCategoryId,
                                            name = expenseName,
                                            value = expenseValue,
                                            expenseDate = expenseDate,
                                            location = location,
                                            photoUri = photoUri
                                        )
                                        navHostController.navigateUp()
                                    }
                                },
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
        },
        floatingActionButton = {
            if (expenseId != null) {
                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    onClick = { showDeletionConfirmationDialog = true }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = null
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(MaterialTheme.spacing.extraSmall)
        ) {
            BaseExpense(
                expense = expense,
                currencyFormat = currency,
                categoriesList = categoriesList,
                onExpenseNameChanged = { expenseName = it },
                onExpenseValueChanged = { expenseValue = it },
                onExpenseCategoryChanged = { expenseCategoryId = it },
                onExpenseDateChanged = { expenseDate = it },
                onInvalidValue = { isInvalidValue = it }
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = MaterialTheme.spacing.medium),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.secondary
            )
            ExpenseLocation(expense?.location) { location = it }
            ExpensePhoto(expense?.name, expense?.picture) { photoUri = it }
        }


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
                            showDeletionConfirmationDialog = false
                            navHostController.navigateUp()
                            expenseId?.let { expenseViewModel.removeExpense(it) }
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
    }
}

@Composable
fun BaseExpense(
    expense: CategorizedExpense?,
    currencyFormat: CurrencyFormat,
    categoriesList: List<ExpenseCategory>,
    onExpenseNameChanged: (String) -> Unit,
    onExpenseValueChanged: (Double) -> Unit,
    onExpenseCategoryChanged: (Int) -> Unit,
    onExpenseDateChanged: (LocalDate) -> Unit,
    onInvalidValue: (Boolean) -> Unit
) {
    var name by remember { mutableStateOf(TextFieldValue()) }
    var value by remember { mutableStateOf(TextFieldValue()) }
    var isInvalidValue by remember { mutableStateOf(false) }

    var selectedCategoryId by remember { mutableIntStateOf(-1) }
    val initialCategoryLabel = stringResource(id = R.string.expense_empty_category_title)
    var categoryLabel by remember { mutableStateOf(initialCategoryLabel) }
    var showAddNewCategoryDialog by remember { mutableStateOf(false) }

    var date by remember { mutableStateOf(LocalDate.now()) }

    LaunchedEffect(key1 = expense) {
        expense?.let {
            name = TextFieldValue(it.name)
            onExpenseNameChanged(it.name)

            value = TextFieldValue(it.expenseValue.toString())
            onExpenseValueChanged(it.expenseValue)

            categoryLabel = it.category.name
            selectedCategoryId = it.category.expenseCategoryId
            onExpenseCategoryChanged(it.category.expenseCategoryId)

            date = it.date
            onExpenseDateChanged(it.date)
        }
    }

    LaunchedEffect(key1 = categoriesList) {
        selectedCategoryId = categoriesList.find { categories ->
            categories.name == categoryLabel
        }?.expenseCategoryId ?: -1
        onExpenseCategoryChanged(selectedCategoryId)
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

    if (showAddNewCategoryDialog) {
        AddCategoryDialog(onAddNewCategory = { categoryLabel = it }) {
            showAddNewCategoryDialog = false
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            label = {
                Text(
                    text = stringResource(id = R.string.expense_expense_expense),
                    style = MaterialTheme.textStyle.h2small
                )
            },
            onValueChange = {
                name = it
                onExpenseNameChanged(it.text)
            },
            colors = expenseTextFieldColors
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = MaterialTheme.spacing.smallest),
            value = value,
            label = {
                Text(
                    text = stringResource(id = R.string.expense_expense_cost),
                    style = MaterialTheme.textStyle.h2small
                )
            },
            isError = isInvalidValue,
            prefix = {
                Text(text = stringResource(id = currencyFormat.getLabelResource()))
            },
            onValueChange = {
                value = it
                isInvalidValue = false
                try {
                    val parsedValue = value.text.toDouble()
                    onInvalidValue(false)
                    onExpenseValueChanged(parsedValue)
                } catch (exception: NumberFormatException) {
                    isInvalidValue = true
                    onInvalidValue(true)
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
                    categoryLabel = categoriesList[it].name
                    onExpenseCategoryChanged(categoriesList[it].expenseCategoryId)
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
                TeiraDatePicker(localDate = date) {
                    date = it
                    onExpenseDateChanged(date)
                }
            }
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun ExpenseLocation(
    expenseLocation: LatLng?,
    onExpenseLocationChanged: (LatLng?) -> Unit
) {
    var expenseLocationState by remember { mutableStateOf(expenseLocation) }

    LaunchedEffect(key1 = expenseLocation) {
        expenseLocation?.let {
            expenseLocationState = it
            onExpenseLocationChanged(expenseLocationState)
        }
    }

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
    }

    val context = LocalContext.current
    when (val location = expenseLocationState) {
        null -> {
            val locationPermissionState = rememberMultiplePermissionsState(
                listOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
            val hasPermissions = locationPermissionState.permissions.all { it.status.isGranted }
            val emptyStateText = if (hasPermissions) {
                R.string.expense_location_empty_state_label
            } else {
                R.string.expense_location_empty_no_permission_state_label
            }

            var capturedLocation by remember { mutableStateOf<LatLng?>(null) }

            if (capturedLocation != null) {
                capturedLocation?.let {
                    ExpenseLocationMap(
                        location = it,
                        onDelete = {
                            expenseLocationState = null
                            capturedLocation = null
                            onExpenseLocationChanged(null)
                        },
                        onExpenseLocationChanged = onExpenseLocationChanged
                    )
                }
            } else {
                TeiraEmptyState(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .padding(top = MaterialTheme.spacing.small)
                        .clickable {
                            if (hasPermissions) {
                                val locationProvider: FusedLocationProviderClient =
                                    LocationServices.getFusedLocationProviderClient(context)
                                locationProvider.lastLocation.addOnCompleteListener {
                                    val lastKnownLocation = it.result
                                    val location = LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude)
                                    capturedLocation = location
                                    onExpenseLocationChanged(location)
                                }
                            } else {
                                locationPermissionState.launchMultiplePermissionRequest()
                            }
                        },
                    emptyStateLabel = emptyStateText
                )
            }
        }

        else -> {
            ExpenseLocationMap(
                location = location,
                onDelete = {
                    Log.d("Tiago", "OnDelete Clicked")
                    expenseLocationState = null
                    onExpenseLocationChanged(null)
                },
                onExpenseLocationChanged = onExpenseLocationChanged
            )
        }

    }
}

@Composable
fun ExpenseLocationMap(
    location: LatLng,
    onDelete: () -> Unit,
    onExpenseLocationChanged: (LatLng) -> Unit
) {
    val minZoomPossible = 12f
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, minZoomPossible)
    }
    val mapProperties by remember {
        mutableStateOf(
            MapProperties(maxZoomPreference = 21f, minZoomPreference = minZoomPossible)
        )
    }
    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(mapToolbarEnabled = false, zoomControlsEnabled = false)
        )
    }
    val markerState = MarkerState(position = location)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(top = MaterialTheme.spacing.small)
    ) {
        Box {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                properties = mapProperties,
                uiSettings = mapUiSettings,
                cameraPositionState = cameraPositionState,
                onMapClick = {
                    markerState.position = it
                    onExpenseLocationChanged(it)
                }
            ) {
                Marker(
                    state = markerState,
                    title = stringResource(id = R.string.expense_expense_expense)
                )
            }
            IconButton(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = { onDelete() }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun ExpensePhoto(
    expenseName: String?,
    expensePhotoUri: Uri?,
    onPictureSelected: (Uri?) -> Unit
) {

    val context = LocalContext.current

    var imageUri by remember { mutableStateOf(expensePhotoUri) }
    var hasImage by remember { mutableStateOf(expensePhotoUri != null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            hasImage = success
            onPictureSelected(imageUri)
        }
    )

    LaunchedEffect(key1 = expensePhotoUri) {
        expensePhotoUri?.let {
            imageUri = it
            hasImage = true
            onPictureSelected(imageUri)
        }
    }

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
    }

    if (hasImage) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .padding(top = MaterialTheme.spacing.small)
        ) {
            if (imageUri != null) {
                Box(modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = imageUri,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Selected image",
                    )
                    IconButton(
                        modifier = Modifier.align(Alignment.TopEnd),
                        onClick = {
                            hasImage = false
                            imageUri = null
                            onPictureSelected(null)
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_delete),
                            contentDescription = "",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    } else {
        TeiraEmptyState(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .padding(top = MaterialTheme.spacing.small)
                .clickable {
                    val uri = ComposeFileProvider.getImageUri(
                        expenseName ?: LocalDate
                            .now()
                            .toString(), context
                    )
                    imageUri = uri
                    cameraLauncher.launch(uri)
                },
            emptyStateLabel = R.string.expense_picture_empty_state_label
        )
    }
}
