package br.com.tick.ui.core

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavBackStackEntry
import br.com.tick.ui.NavigationItem
import br.com.tick.ui.R
import br.com.tick.ui.theme.spacing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TeiraNavigationDrawer(
    drawerState: DrawerState,
    navBackStackEntry: NavBackStackEntry?,
    navigateToRoute: (NavigationItem) -> Unit,
    content: @Composable () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val currentRoute = navBackStackEntry?.destination?.route

    DismissibleNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DismissibleDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                drawerContentColor = MaterialTheme.colorScheme.onSurface
            ) {
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
                TeiraNavigationDrawerItem(
                    drawerState = drawerState,
                    painter = painterResource(id = NavigationItem.Settings.iconResource),
                    text = stringResource(id = NavigationItem.Settings.titleResource),
                    coroutineScope = coroutineScope,
                    isCurrentRoute = currentRoute == NavigationItem.Settings.route
                ) {
                    navigateToRoute(NavigationItem.Settings)
                }
                TeiraNavigationDrawerItem(
                    drawerState = drawerState,
                    painter = painterResource(id = NavigationItem.Wallet.iconResource),
                    text = stringResource(id = NavigationItem.Wallet.titleResource),
                    coroutineScope = coroutineScope,
                    isCurrentRoute = currentRoute == NavigationItem.Wallet.route
                ) {
                    navigateToRoute(NavigationItem.Wallet)
                }
                TeiraNavigationDrawerItem(
                    drawerState = drawerState,
                    painter = painterResource(id = NavigationItem.Analysis.iconResource),
                    text = stringResource(id = NavigationItem.Analysis.titleResource),
                    coroutineScope = coroutineScope,
                    isCurrentRoute = currentRoute == NavigationItem.Analysis.route
                ) {
                    navigateToRoute(NavigationItem.Analysis)
                }
                TeiraNavigationDrawerItem(
                    drawerState = drawerState,
                    painter = painterResource(id = NavigationItem.History.iconResource),
                    text = stringResource(id = NavigationItem.History.titleResource),
                    coroutineScope = coroutineScope,
                    isCurrentRoute = currentRoute == NavigationItem.History.route
                ) {
                    navigateToRoute(NavigationItem.History)
                }
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))
                TeiraNavigationDrawerItem(
                    drawerState = drawerState,
                    painter = painterResource(id = R.drawable.ic_clear),
                    text = stringResource(id = R.string.generic_close),
                    coroutineScope = coroutineScope,
                    isCurrentRoute = false
                )
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
    coroutineScope: CoroutineScope,
    isCurrentRoute: Boolean,
    onDrawerItemClick: (() -> Unit)? = null
) {
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