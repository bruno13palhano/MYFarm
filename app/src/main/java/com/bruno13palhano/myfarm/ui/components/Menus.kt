package com.bruno13palhano.myfarm.ui.components

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.bruno13palhano.myfarm.R
import com.bruno13palhano.myfarm.ui.navigation.HomeRoute
import com.bruno13palhano.myfarm.ui.navigation.ItemsRoute
import com.bruno13palhano.myfarm.ui.navigation.SettingsRoute

@Composable
fun MoreOptionsMenu(
    items: Array<String>,
    expanded: Boolean,
    onDismissRequest: (expanded: Boolean) -> Unit,
    onClick: (index: Int) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onDismissRequest(false) }
    ) {
        items.forEachIndexed { index, item ->
            DropdownMenuItem(
                text = { Text(text = item) },
                onClick = {
                    onClick(index)
                    onDismissRequest(false)
                }
            )
        }
    }
}

@Composable
fun BottomMenu(navController: NavController) {
    val items = listOf(Screen.Home, Screen.Items, Screen.Settings)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = stringResource(id = screen.resourceId)
                    )
                },
                label = { Text(text = stringResource(id = screen.resourceId)) }
            )
        }
    }
}

sealed class Screen<T: Any>(val route: T, val icon: ImageVector, @StringRes val resourceId: Int) {
    data object Home: Screen<HomeRoute>(
        route = HomeRoute,
        icon = Icons.Filled.Home,
        resourceId = R.string.home_label
    )
    data object Items: Screen<ItemsRoute>(
        route = ItemsRoute,
        icon = Icons.AutoMirrored.Filled.List,
        resourceId = R.string.items_label
    )
    data object Settings: Screen<SettingsRoute>(
        route = SettingsRoute,
        icon = Icons.Filled.Settings,
        resourceId = R.string.settings_label
    )
}