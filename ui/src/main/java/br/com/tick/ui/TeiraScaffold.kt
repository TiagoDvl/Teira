package br.com.tick.ui

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import br.com.tick.sdk.domain.NotificationPeriodicity
import br.com.tick.sdk.extensions.getPeriodicityTimeDiff
import br.com.tick.ui.core.TeiraNavigationDrawer
import br.com.tick.ui.extensions.collectAsEffect
import br.com.tick.ui.screens.analysis.AnalysisScreen
import br.com.tick.ui.screens.expense.ExpenseScreen
import br.com.tick.ui.screens.history.HistoryScreen
import br.com.tick.ui.screens.settings.SettingsScreen
import br.com.tick.ui.screens.wallet.WalletScreen
import br.com.tick.ui.theme.textStyle
import br.com.tick.ui.worker.PeriodicWorker
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TeiraScaffold(
    viewModel: TeiraScaffoldViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val notificationPermissionState = rememberPermissionState(
            Manifest.permission.POST_NOTIFICATIONS
        )

        LaunchedEffect(Unit) {
            if (!notificationPermissionState.status.isGranted) {
                notificationPermissionState.launchPermissionRequest()
            }
        }
    }

    viewModel.initialPeriodicNotificationRegistration.collectAsEffect {
        val name = context.getString(R.string.teira_periodic_reminder_channel_name)
        val descriptionText = context.getString(R.string.teira_periodic_reminder_channel_description)
        val channelId = context.getString(R.string.teira_periodic_reminder_channel_id)

        viewModel.setupPeriodicNotification(name, descriptionText, channelId)
        setDelayedPeriodicWorker(context, WorkManager.getInstance(context), it)
    }

    val navHostController = rememberNavController()

    NavHost(
        navController = navHostController,
        startDestination = NavigationItem.Home.route
    ) {
        composable(NavigationItem.Home.route) {
            HomeScreen(navHostController)
        }
        composable(
            route = NavigationItem.Expense.route,
            arguments = listOf(
                navArgument(NavigationItem.Expense.NAVIGATION_EXPENSE_ID_TAG) {
                    type = NavType.IntType
                    defaultValue = -1
                }
            ),
            enterTransition = {
                slideIntoContainer(
                    animationSpec = tween(400, easing = EaseInOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.Up
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    animationSpec = tween(400, easing = EaseInOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.Down
                )
            }
        ) { navBackStackEntry ->
            navBackStackEntry.arguments?.getInt(NavigationItem.Expense.NAVIGATION_EXPENSE_ID_TAG)?.let {
                val expenseId = if (it == -1) null else it
                ExpenseScreen(navHostController = navHostController, expenseId = expenseId)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(parentNavController: NavHostController) {
    val navHostController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()

    val currentRoute = navBackStackEntry?.destination?.route
    val context = LocalContext.current

    TeiraNavigationDrawer(
        drawerState = drawerState,
        navBackStackEntry = navBackStackEntry,
        navigateToParentRoute = { navigateTo(parentNavController, it) },
        navigateToRoute = { navigateTo(navHostController, it) }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_menu),
                                contentDescription = null
                            )
                        }
                    },
                    title = {
                        Text(text = stringResource(id = R.string.app_name), style = MaterialTheme.textStyle.h1)
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            bottomBar = {
                val items = listOf(
                    NavigationItem.Settings,
                    NavigationItem.Wallet,
                    NavigationItem.Analysis
                )

                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ) {
                    items.forEach { navigationItem ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    ImageVector.vectorResource(id = navigationItem.iconResource),
                                    contentDescription = navigationItem.route
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.surface,
                                indicatorColor = MaterialTheme.colorScheme.surface,
                                unselectedIconColor = MaterialTheme.colorScheme.onPrimary,
                                unselectedTextColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            label = {
                                Text(
                                    text = stringResource(id = navigationItem.titleResource),
                                    style = MaterialTheme.textStyle.h2small
                                )
                            },
                            selected = currentRoute == navigationItem.route,
                            onClick = { navigateTo(navHostController, navigationItem) }
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                NavHost(
                    navController = navHostController,
                    startDestination = NavigationItem.Wallet.route,
                    enterTransition = { EnterTransition.None },
                    exitTransition = { ExitTransition.None }
                ) {
                    composable(NavigationItem.Settings.route) {
                        SettingsScreen {
                            handleNotificationPeriodicityChange(context, it)
                        }
                    }
                    composable(NavigationItem.Wallet.route) {
                        WalletScreen(parentNavController)
                    }
                    composable(NavigationItem.Analysis.route) {
                        AnalysisScreen()
                    }
                    composable(NavigationItem.History.route) {
                        HistoryScreen()
                    }
                }
            }
        }
    }
}

private fun navigateTo(navHostController: NavHostController, navigationItem: NavigationItem) {
    navHostController.navigate(navigationItem.route) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        navHostController.graph.startDestinationRoute?.let { route ->
            popUpTo(route) {
                saveState = true
            }
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }
}

private fun handleNotificationPeriodicityChange(context: Context, notificationPeriodicity: NotificationPeriodicity) {
    val workManager = WorkManager.getInstance(context)
    workManager.cancelAllWorkByTag(context.getString(R.string.teira_periodic_reminder_channel_name))

    when (notificationPeriodicity) {
        NotificationPeriodicity.CANCELED -> Unit
        else -> setDelayedPeriodicWorker(context, workManager, notificationPeriodicity)
    }
}

private fun setDelayedPeriodicWorker(
    context: Context,
    workManager: WorkManager,
    notificationPeriodicity: NotificationPeriodicity
) {
    val name = context.getString(R.string.teira_periodic_reminder_channel_name)
    val periodicWorker = OneTimeWorkRequestBuilder<PeriodicWorker>()
        .setInitialDelay(notificationPeriodicity.getPeriodicityTimeDiff(), TimeUnit.MINUTES)
        .addTag(name)
        .build()
    workManager.enqueue(periodicWorker)
}
