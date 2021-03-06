package br.com.tick.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.tick.R
import br.com.tick.ui.screens.analysis.AnalysisScreen
import br.com.tick.ui.screens.configuration.ConfigurationScreen
import br.com.tick.ui.screens.wallet.WalletScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeiraScaffold() {
    val navHostController = rememberNavController()

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            val items = listOf(
                NavigationItem.Configuration,
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
                        label = {
                            Text(text = stringResource(id = navigationItem.titleResource))
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
                composable(NavigationItem.Configuration.route) {
                    ConfigurationScreen()
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
