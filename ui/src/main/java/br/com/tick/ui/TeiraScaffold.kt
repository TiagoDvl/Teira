package br.com.tick.ui

import android.Manifest
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import br.com.tick.R
import br.com.tick.sdk.domain.NotificationPeriodicity
import br.com.tick.sdk.extensions.getPeriodicityTimeDiff
import br.com.tick.ui.extensions.collectAsEffect
import br.com.tick.ui.screens.analysis.AnalysisScreen
import br.com.tick.ui.screens.settings.SettingsScreen
import br.com.tick.ui.screens.wallet.WalletScreen
import br.com.tick.ui.theme.textStyle
import br.com.tick.ui.worker.PeriodicWorker
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun TeiraScaffold(
    viewModel: TeiraScaffoldViewModel = hiltViewModel()
) {
    val navHostController = rememberNavController()
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name), style = MaterialTheme.textStyle.h1)
                },
                colors =  TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
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
                val navBackStackEntry by navHostController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
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
                        onClick = {
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
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = navHostController,
                startDestination = NavigationItem.Wallet.route
            ) {
                composable(NavigationItem.Settings.route) {
                    SettingsScreen {
                        handleNotificationPeriodicityChange(context, it)
                    }
                }
                composable(NavigationItem.Wallet.route) {
                    WalletScreen()
                }
                composable(NavigationItem.Analysis.route) {
                    AnalysisScreen()
                }
            }
        }
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
        .setInitialDelay(LocalDateTime.now().getPeriodicityTimeDiff(notificationPeriodicity), TimeUnit.MINUTES)
        .addTag(name)
        .build()
    workManager.enqueue(periodicWorker)
}
