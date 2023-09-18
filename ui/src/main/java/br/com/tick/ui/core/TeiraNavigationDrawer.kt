package br.com.tick.ui.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import br.com.tick.ui.NavigationItem
import br.com.tick.ui.R
import br.com.tick.ui.theme.spacing
import br.com.tick.ui.theme.textStyle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TeiraNavigationDrawer(
    drawerState: DrawerState,
    navBackStackEntry: NavBackStackEntry?,
    navigateToParentRoute: (NavigationItem) -> Unit,
    navigateToRoute: (NavigationItem) -> Unit,
    content: @Composable () -> Unit
) {

    val currentRoute = navBackStackEntry?.destination?.route

    DismissibleNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DismissibleDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                drawerContentColor = MaterialTheme.colorScheme.onSurface
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(MaterialTheme.spacing.large),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            modifier = Modifier.padding(MaterialTheme.spacing.large),
                            text = stringResource(id = R.string.app_name),
                            style = MaterialTheme.textStyle.h1extra,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
                        TeiraNavigationDrawerItem(
                            drawerState = drawerState,
                            painter = painterResource(id = NavigationItem.Settings.iconResource),
                            text = stringResource(id = NavigationItem.Settings.titleResource),
                            isCurrentRoute = currentRoute == NavigationItem.Settings.route
                        ) {
                            navigateToRoute(NavigationItem.Settings)
                        }
                        TeiraNavigationDrawerItem(
                            drawerState = drawerState,
                            painter = painterResource(id = NavigationItem.Wallet.iconResource),
                            text = stringResource(id = NavigationItem.Wallet.titleResource),
                            isCurrentRoute = currentRoute == NavigationItem.Wallet.route
                        ) {
                            navigateToRoute(NavigationItem.Wallet)
                        }
                        TeiraNavigationDrawerItem(
                            drawerState = drawerState,
                            painter = painterResource(id = NavigationItem.Analysis.iconResource),
                            text = stringResource(id = NavigationItem.Analysis.titleResource),
                            isCurrentRoute = currentRoute == NavigationItem.Analysis.route
                        ) {
                            navigateToRoute(NavigationItem.Analysis)
                        }
                        Spacer(
                            modifier = Modifier
                                .padding(horizontal = MaterialTheme.spacing.medium, vertical = MaterialTheme.spacing.small)
                                .fillMaxWidth()
                                .height(0.5.dp)
                                .background(MaterialTheme.colorScheme.tertiary)
                        )
                        TeiraNavigationDrawerItem(
                            drawerState = drawerState,
                            painter = painterResource(id = NavigationItem.History.iconResource),
                            text = stringResource(id = NavigationItem.History.titleResource),
                            isCurrentRoute = currentRoute == NavigationItem.History.route
                        ) {
                            navigateToRoute(NavigationItem.History)
                        }
                        TeiraNavigationDrawerItem(
                            drawerState = drawerState,
                            painter = painterResource(id = NavigationItem.Expense.iconResource),
                            text = stringResource(id = NavigationItem.Expense.titleResource),
                            isCurrentRoute = currentRoute == NavigationItem.Expense.route
                        ) {
                            navigateToParentRoute(NavigationItem.Expense)
                        }
                    }
                    TeiraNavigationDrawerItem(
                        drawerState = drawerState,
                        painter = painterResource(id = R.drawable.ic_clear),
                        text = stringResource(id = R.string.generic_close),
                        isCurrentRoute = false
                    )
                }
            }
        },
        content = content
    )
}

@Composable
private fun TeiraNavigationDrawerItem(
    drawerState: DrawerState,
    painter: Painter,
    text: String,
    isCurrentRoute: Boolean,
    onDrawerItemClick: (() -> Unit)? = null
) {
    val coroutineScope = rememberCoroutineScope()

    NavigationDrawerItem(
        icon = {
            Icon(painter = painter, contentDescription = null)
        },
        label = { Text(text = text) },
        selected = isCurrentRoute,
        onClick = {
            onDrawerItemClick?.invoke()
            coroutineScope.launch { drawerState.close() }
        },
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = MaterialTheme.colorScheme.onPrimary,
            selectedIconColor = MaterialTheme.colorScheme.onTertiary,
            selectedTextColor = MaterialTheme.colorScheme.onTertiary,
            unselectedTextColor = MaterialTheme.colorScheme.primary,
            unselectedIconColor = MaterialTheme.colorScheme.primary
        )
    )
}