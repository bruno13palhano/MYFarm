package com.bruno13palhano.myfarm.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bruno13palhano.myfarm.ui.screens.home.HomeRoute
import com.bruno13palhano.myfarm.ui.screens.items.ItemsRoute
import com.bruno13palhano.myfarm.ui.screens.settings.SettingsRoute

@Composable
fun MainNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = MainDestinations.MAIN_ROUTE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        navigation(
            startDestination = MainDestinations.HOME_ROUTE,
            route = startDestination
        ) {
            composable(route = MainDestinations.HOME_ROUTE) { HomeRoute() }
            composable(route = MainDestinations.ITEMS_ROUTE) { ItemsRoute() }
            composable(route = MainDestinations.SETTINGS_ROUTE) { SettingsRoute() }
        }
    }
}

object MainDestinations {
    const val MAIN_ROUTE = "main_route"
    const val HOME_ROUTE = "home_route"
    const val ITEMS_ROUTE = "items_route"
    const val SETTINGS_ROUTE = "settings_route"
}