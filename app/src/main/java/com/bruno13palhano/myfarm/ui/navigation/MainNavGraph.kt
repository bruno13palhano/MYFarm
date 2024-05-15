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
import kotlinx.serialization.Serializable

@Composable
fun MainNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: MainRoute = MainRoute
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        navigation<MainRoute>( startDestination = HomeRoute) {
            composable<HomeRoute> { HomeRoute() }
            composable<ItemsRoute> { ItemsRoute() }
            composable<SettingsRoute> { SettingsRoute() }
        }
    }
}

@Serializable
object MainRoute

@Serializable
object HomeRoute

@Serializable
object ItemsRoute

@Serializable
object SettingsRoute